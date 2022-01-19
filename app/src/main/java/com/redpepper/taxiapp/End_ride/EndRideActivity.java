package com.redpepper.taxiapp.End_ride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.redpepper.taxiapp.Http.ResponseModels.RideResponse;
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



    }

    @Override
    public void setRideInfo(RideResponse rideInfo) {

        binding.endRideTripIdTxt.setText(String.valueOf(rideInfo.getId()));
        binding.endRidePickAddressLine1.setText(rideInfo.getPickupAddress());
        binding.endRideDestAddressLine1.setText(rideInfo.getDestinationAddress());

        binding.endRideFareTxt.setText(getString(R.string.end_ride_price,rideInfo.getPaymentAmount()));
    }
}