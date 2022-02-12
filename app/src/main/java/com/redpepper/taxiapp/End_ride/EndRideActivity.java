package com.redpepper.taxiapp.End_ride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.redpepper.taxiapp.Http.ResponseModels.AcceptedDriverInfoResponse;
import com.redpepper.taxiapp.Http.ResponseModels.RideResponse;
import com.redpepper.taxiapp.Main.MainActivity;
import com.redpepper.taxiapp.R;
import com.redpepper.taxiapp.Root.App;
import com.redpepper.taxiapp.databinding.ActivityEndRideBinding;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class EndRideActivity extends Activity implements EndRideActivityMVP.View  {

    ActivityEndRideBinding binding;

    @Inject
    EndRideActivityMVP.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEndRideBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);

        ((App) getApplication()).getComponent().inject(this);

        presenter.setView(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();

        String driverId = intent.getStringExtra("driverID");

        int rideId = intent.getIntExtra("rideID",0);

        Picasso.get().load("http://pick-rides.eu/storage/dr_pr_img/dri_img_"+ driverId +".jpg")
               .into(binding.endRideDriverImg);


        presenter.getRideInfo(rideId);

        presenter.getDriverInfo(driverId);

        binding.endRideRatingbar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {

            presenter.sendDriverRating(rating);

        });

    }

    @Override
    public void setRideInfo(RideResponse rideInfo) {
        binding.endRideTripIdTxt.setText(String.valueOf(rideInfo.getId()));

        String[] pickAddArr = rideInfo.getPickupAddress().split(",");

        if(pickAddArr.length > 1){

            binding.endRidePickAddressLine1.setText(pickAddArr[0]);

            binding.endRidePickAddressLine2.setText(pickAddArr[1] + "," + pickAddArr[2]);

        }else{

            binding.endRidePickAddressLine1.setText(rideInfo.getPickupAddress());
        }

        String[] destAddArr = rideInfo.getDestinationAddress().split(",");

        if(destAddArr.length > 1){

            binding.endRideDestAddressLine1.setText(destAddArr[0]);

            binding.endRideDestAddressLine2.setText(destAddArr[1]+","+ destAddArr[2]);

        }else{

            binding.endRideDestAddressLine1.setText(rideInfo.getDestinationAddress());
        }

        binding.endRideFareTxt.setText(getString(R.string.end_ride_price,rideInfo.getPaymentAmount()));
    }

    @Override
    public void setDriverInfo(AcceptedDriverInfoResponse driverInfo) {
        binding.endRideDriverName.setText(driverInfo.getName());
        binding.endRideDriverCurrentRateTxt.setText(getString(R.string.main_coming_driver_rating,4));
        binding.endRideDriverPlateTxt.setText(driverInfo.getPlate());
        binding.endRideDriverMakeModelTxt.setText(getString(R.string.end_ride_car_make_model,driverInfo.getMake(),driverInfo.getModel()));
    }

    @Override
    public void moveToMainScreen() {
        Intent intent = new Intent(EndRideActivity.this, MainActivity.class);
        this.finish();
        startActivity(intent);
    }
}