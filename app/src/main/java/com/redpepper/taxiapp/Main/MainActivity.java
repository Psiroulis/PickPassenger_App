package com.redpepper.taxiapp.Main;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.maps.android.PolyUtil;
import com.redpepper.taxiapp.End_ride.EndRideActivity;
import com.redpepper.taxiapp.R;
import com.redpepper.taxiapp.databinding.ActivityMainBinding;
import com.redpepper.taxiapp.Http.ResponseModels.AcceptedDriverInfoResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripePaymentMethod;
import com.redpepper.taxiapp.Payments.PaymentMethodFragment;
import com.redpepper.taxiapp.Root.App;
import com.redpepper.taxiapp.Search_locations.Models.FoursquarePlace;
import com.redpepper.taxiapp.Search_locations.SearchLocationFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;


public class MainActivity extends FragmentActivity implements
        MainActivityMVP.View,
        OnMapReadyCallback,
        SearchLocationFragment.SearchLocationFragmentActionsListener,
        PaymentMethodFragment.PaymentFragmentListener {

    @Inject
    SharedPreferences prefs;
    @Inject
    MainActivityMVP.Presenter presenter;

    private ActivityMainBinding binding;

    private Location pickUpLocation;
    private Location destinationLocation;

    private Context context;
    private GoogleMap mMap;
    private String pickUpFullAddress;
    private String destinationFullAddress;

    private String currentRidePaymentId = "cash";
    private BroadcastReceiver fcmBroadcastReceiver = null;
    //private boolean stillSearchingForTaxi = false;


    private ConstraintSet addressBoxDefConstSet;
    private ConstraintSet bottomCarTypeLayDefSet;
    private ConstraintSet bottomContainerConstSet;

    private ConstraintSet parentConstSet;
    private ConstraintSet comingDriverInfoLayConstSet;
    private ConstraintSet comingDriverLayConstSet;

    private boolean isMainMenuOpen = false;
    private boolean isDriverInfoContainerExpanded = false;

    private boolean getLocationFromMapCameraTarget = true;

    private static final int PICKUP = 1;
    private static final int DESTINATION = 2;
    private static final int PAYMENT = 3;
    private static final int SEARCHING = 4;
    private int mapFunctionality;

    private static final int PICKUP_TEXT = 1;
    private static final int DESTINATION_TEXT = 2;
    private int textToUpdate;

    private LinearLayout verticalBlueLine;
    private AppCompatImageView destinationIcon;
    private LinearLayout horizontalGreyDivider;
    private TextView destinationAddressText;
    private AppCompatImageView clientMarkerFlagIcon;
    private ConstraintLayout searchTaxiAnimationLayout;
    private TextView searchTaxiLoadingMessage;

    private FragmentManager fragmentManager;

    private List<LatLng> airportPoints;

    private Handler driverLocationHandler;
    private Runnable driverLocationRunable;

    private int currentRideId;
    private String comingDriverId;

    private boolean clientIsOnBoard=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();

        setContentView(view);

        ((App) getApplication()).getComponent().inject(this);

        context = this;

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Places.initialize(getApplicationContext(), context.getResources().getString(R.string.google_maps_key));

        presenter.setView(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {

            mapFragment.getMapAsync(this);
        }

        pickUpLocation = new Location(LocationManager.GPS_PROVIDER);

        destinationLocation = new Location(LocationManager.GPS_PROVIDER);

        mapFunctionality = PICKUP;

        textToUpdate = PICKUP_TEXT;

        FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener(task1 -> {

                    String fbr_id = task1.getResult();

                    prefs.edit().putString("frb_installation_id", fbr_id).apply();

                    presenter.createUserDevice();

                });


        initializeConstrainSets();

        handleButtonsClick();

        fragmentManager = getSupportFragmentManager();

        airportPoints = new ArrayList<>();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.uber_style_json));

            if (!success) {
                Log.e("blepo", "Style parsing failed.");

            }
        } catch (Resources.NotFoundException e) {
            Log.e("blepo", "Can't find style. Error: ", e);
        }

        airportPoints.add(new LatLng(37.922292, 23.914974));
        airportPoints.add(new LatLng(37.959199, 23.949166));
        airportPoints.add(new LatLng(37.955812, 23.976215));
        airportPoints.add(new LatLng(37.915716, 23.936638));

        Polygon airportPolygon = googleMap.addPolygon(new PolygonOptions().addAll(airportPoints));
        airportPolygon.setStrokeWidth(0f);
        airportPolygon.setFillColor(Color.TRANSPARENT);

        mMap.setOnCameraIdleListener(() -> {

            if (getLocationFromMapCameraTarget) {

                Location locationForPresenter = new Location(LocationManager.GPS_PROVIDER);

                locationForPresenter.setLatitude(mMap.getCameraPosition().target.latitude);

                locationForPresenter.setLongitude(mMap.getCameraPosition().target.longitude);

                if (mapFunctionality == PICKUP) {

                    pickUpLocation.setLatitude(mMap.getCameraPosition().target.latitude);

                    pickUpLocation.setLongitude(mMap.getCameraPosition().target.longitude);

                    destinationLocation.setLatitude(mMap.getCameraPosition().target.latitude);

                    destinationLocation.setLongitude(mMap.getCameraPosition().target.longitude);

                }

                if (mapFunctionality == DESTINATION) {

                    destinationLocation.setLatitude(mMap.getCameraPosition().target.latitude);

                    destinationLocation.setLongitude(mMap.getCameraPosition().target.longitude);

                }

                presenter.getAddressFromLocation(locationForPresenter);
            }

        });

        checkPermissions();

        mMap.getUiSettings().setCompassEnabled(false);

        hideLoadingLayout();

    }

    @Override
    public void putLastKnownLocationOnMap(Location location) {
        LatLng passengerPosition = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(passengerPosition, 18), new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {

            }

            @Override
            public void onCancel() {

            }

        });

        hideLoadingLayout();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.getLastKnownLocation();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("blepo", "onResume is called!");

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void checkPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
                        , 123);

            } else {
                presenter.getLastKnownLocation();
            }
        } else {
            presenter.getLastKnownLocation();
        }
    }

    @Override
    public void onBackPressed() {

        if (mapFunctionality == DESTINATION && !isMainMenuOpen) {

            makeUiChangesForResetToPickUpChoice();

            presenter.getLastKnownLocation();

            mapFunctionality = PICKUP;

        }

        if (mapFunctionality == PAYMENT && !isMainMenuOpen) {

            makeUiChangesForResetToDestinationChoice();

            mapFunctionality = DESTINATION;

        }

        if(mapFunctionality == SEARCHING && !isMainMenuOpen){
            //TODO: Set back button functionality when we searching for taxi
        }

        if (isMainMenuOpen) {

            TransitionManager.beginDelayedTransition(binding.mainParent);

            parentConstSet.setVisibility(binding.whiteBlurBgLay.getId(), ConstraintSet.GONE);

            parentConstSet.clear(binding.mainMenuLay.getId(), ConstraintSet.END);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.hambMenuImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_icon, getApplicationContext().getTheme()));
            } else {
                binding.hambMenuImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_icon));
            }

            isMainMenuOpen = false;

            parentConstSet.applyTo(binding.mainParent);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        presenter.rxUnsubscribe();

        if (fcmBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(fcmBroadcastReceiver);
        }

        driverLocationHandler.removeCallbacks(driverLocationRunable);
    }

    @Override
    public void setAddressButtonText(String addressToShow, String fullAddress) {

        LatLng cameraLocation = new LatLng(mMap.getCameraPosition().target.latitude,
                mMap.getCameraPosition().target.longitude);

        if(mapFunctionality == PICKUP){
            presenter.getNearestDriverArrivalTime(pickUpLocation.getLatitude(), pickUpLocation.getLongitude());
        }

        if (PolyUtil.containsLocation(cameraLocation, airportPoints, true)) {

            if (mapFunctionality == PICKUP) {

                binding.mainPickupAddressTxt.setText(getResources().getString(R.string.main_airport_name));

                pickUpFullAddress = getResources().getString(R.string.main_airport_name);

            } else {

                if (destinationAddressText != null) {

                    destinationAddressText.setTextColor(Color.parseColor("#000000"));

                    destinationAddressText.setText(getResources().getString(R.string.main_airport_name));

                    destinationFullAddress = getResources().getString(R.string.main_airport_name);

                }

            }

        } else {

            if (mapFunctionality == PICKUP) {

                binding.mainPickupAddressTxt.setText(addressToShow);

                pickUpFullAddress = fullAddress;

            } else {

                if (destinationAddressText != null) {

                    destinationAddressText.setTextColor(Color.parseColor("#000000"));

                    destinationAddressText.setText(addressToShow);

                    destinationFullAddress = fullAddress;

                }

            }
        }



    }

    @Override
    public void showNoDriverInRangeMessage() {
        binding.mainClosestDriverTimeTxtMarker.setText("-");

        binding.mainClosestDriverTimeTxtBottom.setText(getResources().getString(R.string.main_no_driver_in_range));
    }



    @Override
    public void showDirectionsOnMap(List<String> polyLines) {

        for (int i = 0; i < polyLines.size(); i++) {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.parseColor("#205CBE"));
            options.width(5);
            options.zIndex(1);
            options.addAll(PolyUtil.decode(polyLines.get(i)));


            mMap.addPolyline(options);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(pickUpLocation.getLatitude(),pickUpLocation.getLongitude())).
                include(new LatLng(destinationLocation.getLatitude(),destinationLocation.getLongitude()));

        LatLngBounds rideBounds = builder.build();

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(rideBounds,200,200,5));
    }

    @Override
    public void showComingDriverDirectionsOnMap(List<String> points, int duration) {

        for (int i = 0; i < points.size(); i++) {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.parseColor("#205CBE"));
            options.width(5);
            options.zIndex(1);
            options.addAll(PolyUtil.decode(points.get(i)));


            mMap.addPolyline(options);
        }

        binding.mainComingDriverArrivalTimeTxtTwo.setText(String.valueOf(duration/60));
    }

    @Override
    public void showRideCost(String amount) {
        binding.mainBottomRideCost.setText(amount);
    }

    @Override
    public void showNearestDriverArrivalTime(String time) {

        binding.mainClosestDriverTimeTxtMarker.setText(time);

        binding.mainClosestDriverTimeTxtBottom.setText(getResources().getString(R.string.main_arriveat_message, time));

    }

    private void handleButtonsClick() {


        binding.humburgerMenuLay.setOnClickListener(v -> {

            TransitionManager.beginDelayedTransition(binding.mainParent);

            if (!isMainMenuOpen) {

                parentConstSet.setVisibility(binding.whiteBlurBgLay.getId(), ConstraintSet.VISIBLE);

                parentConstSet.connect(binding.mainMenuLay.getId(), ConstraintSet.END, binding.mainParent.getId(), ConstraintSet.END, Math.round(convertDpToPixel(80, context)));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.hambMenuImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_cross_icon, getApplicationContext().getTheme()));
                } else {
                    binding.hambMenuImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_cross_icon));
                }

                isMainMenuOpen = true;

            } else {

                parentConstSet.setVisibility(binding.whiteBlurBgLay.getId(), ConstraintSet.GONE);

                parentConstSet.clear(binding.mainMenuLay.getId(), ConstraintSet.END);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    binding.hambMenuImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_icon, getApplicationContext().getTheme()));
                } else {
                    binding.hambMenuImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_icon));
                }

                isMainMenuOpen = false;
            }

            parentConstSet.applyTo(binding.mainParent);


        });

        binding.whiteBlurBgLay.setOnClickListener(v -> {

            TransitionManager.beginDelayedTransition(binding.mainParent);

            parentConstSet.setVisibility(binding.whiteBlurBgLay.getId(), ConstraintSet.GONE);

            parentConstSet.clear(binding.mainMenuLay.getId(), ConstraintSet.END);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.hambMenuImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_icon, getApplicationContext().getTheme()));
            } else {
                binding.hambMenuImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_icon));
            }

            isMainMenuOpen = false;

            parentConstSet.applyTo(binding.mainParent);

        });

        binding.mainResetLocationButton.setOnClickListener(v -> presenter.getLastKnownLocation());

        binding.mainPickupAddressTxt.setOnClickListener(v -> {

            textToUpdate = PICKUP_TEXT;

            SearchLocationFragment searchFragment = new SearchLocationFragment();

            Bundle bundle = new Bundle();

            bundle.putString("title", getResources().getString(R.string.main_top_title_pickup));

            bundle.putDouble("lat", pickUpLocation.getLatitude());

            bundle.putDouble("lng", pickUpLocation.getLongitude());

            searchFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_from_top)
                    .replace(binding.mainFragmentContainer.getId(), searchFragment, "searchLocationFragment")
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();


        });


        binding.mainCTAButton.setOnClickListener(v -> {

            if (mapFunctionality == PICKUP) {

                makeUiChangesForDestinationChoice();

                mapFunctionality = DESTINATION;


            } else if (mapFunctionality == DESTINATION) {

                if(destinationAddressText.getText().toString().equalsIgnoreCase(getResources().getString(R.string.main_where_to_go))){

                    destinationAddressText.setTextColor(Color.RED);

                }else{
                    Log.d("blepo",destinationAddressText.getText().toString());
                    Log.d("blepo", "Going To Payment");
                    Log.d("blepo", "PickUp Full Address: " + pickUpFullAddress);
                    Log.d("blepo", "PickUp Coords-> Lat: " + pickUpLocation.getLatitude() + " Lng: " + pickUpLocation.getLongitude());
                    Log.d("blepo", "Destination Full Address: " + destinationFullAddress);
                    Log.d("blepo", "Destination Coords-> Lat: " + destinationLocation.getLatitude() + " Lng: " + destinationLocation.getLongitude());

                    makeUiChangesForPaymentChoice();

                    mapFunctionality = PAYMENT;
                }



            } else if (mapFunctionality == PAYMENT) {

                sendRideRequestToDrivers();

                makeUiChangesForSearchTaxiAnimation();

                Log.d("blepo", "Going Find Taxi");
                Log.d("blepo", "PickUp Full Address: " + pickUpFullAddress);
                Log.d("blepo", "PickUp Coords-> Lat: " + pickUpLocation.getLatitude() + " Lng: " + pickUpLocation.getLongitude());
                Log.d("blepo", "Destination Full Address: " + destinationFullAddress);
                Log.d("blepo", "Destination Coords-> Lat: " + destinationLocation.getLatitude() + " Lng: " + destinationLocation.getLongitude());
                Log.d("blepo", "payment_id-> " + currentRidePaymentId);

                mapFunctionality = SEARCHING;

            }else if(mapFunctionality == SEARCHING){

                //Todo: Cancel taxi searching to backend

                makeUiChangesForResetToPaymentChoice();

                mapFunctionality = PAYMENT;

            }

        });

        binding.mainComingDriverImg.setOnClickListener(v->{

            if(isDriverInfoContainerExpanded){

                makeUiChangesForShrinkDriverContainer();

                isDriverInfoContainerExpanded = false;

            }else{

                makeUiChangesForExpandDriverContainer();

                isDriverInfoContainerExpanded = true;

            }

        });

    }

    @Override
    public void onSearchFragmentBackButtonPressed() {
        hideFragmentContainer();
    }

    @Override
    public void onPaymentFragmentBackButtonClick() {
        hideFragmentContainer();
    }

    @Override
    public void onGoogleResultItemClick(Place place) {

        if (place.getLatLng() != null && place.getAddress() != null) {

            if (mapFunctionality == PICKUP) {

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude)));

            }

            if (mapFunctionality == DESTINATION && textToUpdate == PICKUP_TEXT) {

                mMap.clear();

                mMap.addMarker(
                        new MarkerOptions()
                                .position(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pick_up_marker))

                );


                binding.mainPickupAddressTxt.setText(place.getAddress().split(",")[0]);
                pickUpFullAddress = place.getAddress();
                pickUpLocation.setLatitude(place.getLatLng().latitude);
                pickUpLocation.setLongitude(place.getLatLng().longitude);
            }

            if (mapFunctionality == DESTINATION && textToUpdate == DESTINATION_TEXT) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude)));
            }


            hideFragmentContainer();
        }


    }

    @Override
    public void onFoursquareResultItemClick(FoursquarePlace place) {

        if (mapFunctionality == PICKUP) {

            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(place.getLatitude(), place.getLongtitude())));

        }

        if (mapFunctionality == DESTINATION && textToUpdate == PICKUP_TEXT) {

            mMap.clear();

            mMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(place.getLatitude(), place.getLongtitude()))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pick_up_marker))

            );


            binding.mainPickupAddressTxt.setText(place.getAddress().split(",")[0]);
            pickUpFullAddress = place.getAddress();
            pickUpLocation.setLatitude(place.getLatitude());
            pickUpLocation.setLongitude(place.getLongtitude());
        }

        if (mapFunctionality == DESTINATION && textToUpdate == DESTINATION_TEXT) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(place.getLatitude(), place.getLongtitude())));
        }

        hideFragmentContainer();
    }

    @Override
    public void onPaymentFragmentCashButtonClick() {

        currentRidePaymentId = "cash";

        binding.mainBottomPaymentImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_cash_white));

        binding.mainBottomSelectedCardNumber.setText(getResources().getString(R.string.main_cash));

        hideFragmentContainer();
    }

    @Override
    public void onPaymentFragmentCardItemClick(StripePaymentMethod paymentMethod) {

        currentRidePaymentId = paymentMethod.getId();

        if (paymentMethod.getBrand().equalsIgnoreCase("visa")) {
            binding.mainBottomPaymentImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));
        } else {
            binding.mainBottomPaymentImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_mastercard));
        }

        binding.mainBottomSelectedCardNumber.setText(
                getResources().getString(R.string.main_card_dots_plus_numbers, paymentMethod.getLast4())
        );

        hideFragmentContainer();
    }

    @Override
    public void showComingDriverInfo(String driverId, int rideId, AcceptedDriverInfoResponse driverInfo) {

        Picasso.get().load("http://pick-rides.eu/storage/dr_pr_img/dri_img_"+ driverId +".jpg")
                .into(binding.mainComingDriverImg);

        binding.mainComingDriverName.setText(driverInfo.getName());
        binding.mainComingDriverRating.setText("Βαθμολογία: 4.8");
        binding.mainComingDriverPlate.setText(driverInfo.getPlate());
        binding.mainComingDriverMake.setText(driverInfo.getMake() +" "+driverInfo.getModel());


        makeUiChangesToShowComingDriverLayout();

        presenter.fillRideInfo(driverId,rideId,pickUpFullAddress,pickUpLocation.getLatitude(),pickUpLocation.getLongitude(),
                destinationFullAddress,destinationLocation.getLatitude(),destinationLocation.getLongitude(),
                currentRidePaymentId);

    }

    @Override
    public void rideInfoUpdated(String driverId) {

        driverLocationHandler = new Handler();

        driverLocationRunable = new Runnable() {
            @Override
            public void run() {

                presenter.getComingDriverLocation(driverId);

                driverLocationHandler.postDelayed(this,5000);

            }
        };

        driverLocationHandler.postDelayed(driverLocationRunable,0);
    }

    @Override
    public void updateDriverLocationOnMap(LatLng driverLocation) {

        mMap.clear();

        if(!clientIsOnBoard){
            mMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(pickUpLocation.getLatitude(), pickUpLocation.getLongitude()))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pick_up_marker))

            );
        }

        if(clientIsOnBoard){
            mMap.addMarker(
                    new MarkerOptions()
                            .position(new LatLng(destinationLocation.getLatitude(), destinationLocation.getLongitude()))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker))

            );
        }


        mMap.addMarker(
                new MarkerOptions()
                        .position(driverLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi_top_marker))

        );

        if(!clientIsOnBoard){
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            builder.include(new LatLng(pickUpLocation.getLatitude(),pickUpLocation.getLongitude())).
                    include(driverLocation);

            LatLngBounds rideBounds = builder.build();

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(rideBounds,200,200,5));

            mMap.getUiSettings().setAllGesturesEnabled(true);

            presenter.getComingDriverRoute(pickUpLocation.getLatitude(), pickUpLocation.getLongitude(),
                    driverLocation.latitude, driverLocation.longitude);
        }

        if(clientIsOnBoard){

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            builder.include(new LatLng(destinationLocation.getLatitude(),destinationLocation.getLongitude())).
                    include(driverLocation);

            LatLngBounds rideBounds = builder.build();

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(rideBounds,200,200,5));

            mMap.getUiSettings().setAllGesturesEnabled(true);

            presenter.getComingDriverRoute(destinationLocation.getLatitude(), destinationLocation.getLongitude(),
                    driverLocation.latitude, driverLocation.longitude);
        }



    }

    private static float convertDpToPixel(float dp, @NonNull Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private void initializeConstrainSets() {

        parentConstSet = new ConstraintSet();

        parentConstSet.clone(binding.mainParent);

        comingDriverLayConstSet = new ConstraintSet();

        comingDriverLayConstSet.clone(binding.mainComingDriverLayout);

        comingDriverInfoLayConstSet = new ConstraintSet();

        comingDriverInfoLayConstSet.clone(binding.mainDriverComingInfoLay);

        addressBoxDefConstSet = new ConstraintSet();

        addressBoxDefConstSet.clone(binding.mainAddressesLayout);

        bottomCarTypeLayDefSet = new ConstraintSet();

        bottomCarTypeLayDefSet.clone(binding.mainCarTypeLay);

        bottomContainerConstSet = new ConstraintSet();

        bottomContainerConstSet.clone(binding.mainBottomContainer);

    }

    private void makeUiChangesForDestinationChoice() {
        final int sdk = android.os.Build.VERSION.SDK_INT;

        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            binding.mainAddressesLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_white_small_corners));
        } else {
            binding.mainAddressesLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_white_small_corners));
        }

        binding.mainTitleUpTxt.setText(getResources().getString(R.string.main_top_title_dropoff));

        binding.mainCTAButton.setText(getResources().getString(R.string.main_cta_button_dropoff));

        TransitionManager.beginDelayedTransition(binding.mainParent);

        parentConstSet.constrainHeight(binding.mainAddressesLayout.getId(), ConstraintSet.WRAP_CONTENT);

        parentConstSet.setVisibility(binding.mainResetLocationButton.getId(), ConstraintSet.GONE);

        parentConstSet.applyTo(binding.mainParent);

        ConstraintSet addressBoxExtConstSet = new ConstraintSet();

        verticalBlueLine = new LinearLayout(context);

        verticalBlueLine.setId(View.generateViewId());

        destinationIcon = new AppCompatImageView(context);

        destinationIcon.setId(View.generateViewId());

        horizontalGreyDivider = new LinearLayout(context);

        horizontalGreyDivider.setId(View.generateViewId());

        destinationAddressText = new TextView(context);

        destinationAddressText.setId(View.generateViewId());

        binding.mainAddressesLayout.addView(verticalBlueLine);

        binding.mainAddressesLayout.addView(destinationIcon);

        binding.mainAddressesLayout.addView(horizontalGreyDivider);

        binding.mainAddressesLayout.addView(destinationAddressText);

        addressBoxExtConstSet.clone(binding.mainAddressesLayout);

        //Set Constrains Here After adding new Views and Clone

        addressBoxExtConstSet.clear(binding.pickUpIcon.getId(), ConstraintSet.BOTTOM);

        addressBoxExtConstSet.setMargin(binding.pickUpIcon.getId(), ConstraintSet.TOP, Math.round(convertDpToPixel(16, context)));

        verticalBlueLine.setBackgroundColor(getResources().getColor(R.color.primary));

        addressBoxExtConstSet.constrainWidth(verticalBlueLine.getId(), Math.round(convertDpToPixel(2, context)));

        addressBoxExtConstSet.constrainHeight(verticalBlueLine.getId(), Math.round(convertDpToPixel(30, context)));

        addressBoxExtConstSet.connect(verticalBlueLine.getId(), ConstraintSet.TOP, binding.pickUpIcon.getId(), ConstraintSet.BOTTOM,
                Math.round(convertDpToPixel(10, context)));

        addressBoxExtConstSet.connect(verticalBlueLine.getId(), ConstraintSet.START, binding.pickUpIcon.getId(), ConstraintSet.START);

        addressBoxExtConstSet.connect(verticalBlueLine.getId(), ConstraintSet.END, binding.pickUpIcon.getId(), ConstraintSet.END);

        addressBoxExtConstSet.constrainWidth(destinationIcon.getId(), Math.round(convertDpToPixel(13, context)));

        addressBoxExtConstSet.constrainHeight(destinationIcon.getId(), Math.round(convertDpToPixel(15, context)));

        destinationIcon.setImageResource(R.drawable.ic_location_filled);

        addressBoxExtConstSet.connect(destinationIcon.getId(), ConstraintSet.TOP, verticalBlueLine.getId(), ConstraintSet.BOTTOM,
                Math.round(convertDpToPixel(10, context)));

        addressBoxExtConstSet.connect(destinationIcon.getId(), ConstraintSet.START, binding.mainAddressesLayout.getId(), ConstraintSet.START,
                Math.round(convertDpToPixel(16, context)));

        addressBoxExtConstSet.connect(destinationIcon.getId(), ConstraintSet.BOTTOM, binding.mainAddressesLayout.getId(), ConstraintSet.BOTTOM,
                Math.round(convertDpToPixel(16, context)));

        addressBoxExtConstSet.constrainHeight(horizontalGreyDivider.getId(), Math.round(convertDpToPixel(2, context)));

        addressBoxExtConstSet.constrainWidth(horizontalGreyDivider.getId(), ConstraintSet.MATCH_CONSTRAINT);

        horizontalGreyDivider.setBackgroundColor(Color.parseColor("#E0E0E0"));

        addressBoxExtConstSet.connect(horizontalGreyDivider.getId(), ConstraintSet.START, binding.mainPickupAddressTxt.getId(), ConstraintSet.START);

        addressBoxExtConstSet.connect(horizontalGreyDivider.getId(), ConstraintSet.END, binding.mainAddressesLayout.getId(), ConstraintSet.END);

        addressBoxExtConstSet.connect(horizontalGreyDivider.getId(), ConstraintSet.TOP, binding.pickUpIcon.getId(), ConstraintSet.BOTTOM);

        addressBoxExtConstSet.connect(horizontalGreyDivider.getId(), ConstraintSet.BOTTOM, destinationIcon.getId(), ConstraintSet.TOP);

        addressBoxExtConstSet.constrainHeight(destinationAddressText.getId(), ConstraintSet.WRAP_CONTENT);

        addressBoxExtConstSet.constrainWidth(destinationAddressText.getId(), ConstraintSet.MATCH_CONSTRAINT);

        addressBoxExtConstSet.connect(destinationAddressText.getId(), ConstraintSet.TOP, destinationIcon.getId(), ConstraintSet.TOP);

        addressBoxExtConstSet.connect(destinationAddressText.getId(), ConstraintSet.BOTTOM, destinationIcon.getId(), ConstraintSet.BOTTOM);

        addressBoxExtConstSet.connect(destinationAddressText.getId(), ConstraintSet.START, binding.mainPickupAddressTxt.getId(), ConstraintSet.START);

        addressBoxExtConstSet.connect(destinationAddressText.getId(), ConstraintSet.END, binding.mainPickupAddressTxt.getId(), ConstraintSet.END);

        destinationAddressText.setText(getResources().getString(R.string.main_where_to_go));

        destinationAddressText.setTextColor(Color.parseColor("#BDBDBD"));

        Typeface typeface = ResourcesCompat.getFont(context, R.font.inter_regular);

        destinationAddressText.setTypeface(typeface, Typeface.BOLD);

        destinationAddressText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        destinationAddressText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        addressBoxExtConstSet.applyTo(binding.mainAddressesLayout);

        mMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pick_up_marker))

        );

        binding.mainClientMarker.removeView(binding.mainMarkerTextContainer);

        clientMarkerFlagIcon = new AppCompatImageView(context);

        clientMarkerFlagIcon.setId(View.generateViewId());

        binding.mainClientMarker.addView(clientMarkerFlagIcon);

        ConstraintSet addFlagSet = new ConstraintSet();

        addFlagSet.clone(binding.mainClientMarker);

        addFlagSet.constrainWidth(clientMarkerFlagIcon.getId(), Math.round(convertDpToPixel(20, context)));

        addFlagSet.constrainHeight(clientMarkerFlagIcon.getId(), Math.round(convertDpToPixel(20, context)));

        addFlagSet.connect(clientMarkerFlagIcon.getId(), ConstraintSet.TOP, binding.mainClientMarker.getId(), ConstraintSet.TOP);
        addFlagSet.connect(clientMarkerFlagIcon.getId(), ConstraintSet.START, binding.mainClientMarker.getId(), ConstraintSet.START);
        addFlagSet.connect(clientMarkerFlagIcon.getId(), ConstraintSet.BOTTOM, binding.mainClientMarker.getId(), ConstraintSet.BOTTOM);
        addFlagSet.connect(clientMarkerFlagIcon.getId(), ConstraintSet.END, binding.mainClientMarker.getId(), ConstraintSet.END);

        clientMarkerFlagIcon.setImageResource(R.drawable.ic_destination_flag);

        addFlagSet.applyTo(binding.mainClientMarker);

        destinationAddressText.setOnClickListener(v -> {

            textToUpdate = DESTINATION_TEXT;

            SearchLocationFragment searchFragment = new SearchLocationFragment();

            Bundle bundle = new Bundle();

            bundle.putString("title", getResources().getString(R.string.main_top_title_dropoff));

            bundle.putDouble("lat", pickUpLocation.getLatitude());

            bundle.putDouble("lng", pickUpLocation.getLongitude());

            searchFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_from_top)
                    .replace(binding.mainFragmentContainer.getId(), searchFragment, "searchLocationFragment")
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void makeUiChangesForResetToPickUpChoice() {

        binding.mainTitleUpTxt.setText(getResources().getString(R.string.main_top_title_pickup));

        binding.mainCTAButton.setText(getResources().getString(R.string.main_cta_button_pickup));

        binding.mainAddressesLayout.removeView(verticalBlueLine);

        binding.mainAddressesLayout.removeView(destinationIcon);

        binding.mainAddressesLayout.removeView(horizontalGreyDivider);

        binding.mainAddressesLayout.removeView(destinationAddressText);

        binding.mainClientMarker.removeView(clientMarkerFlagIcon);

        binding.mainClientMarker.addView(binding.mainMarkerTextContainer);

        TransitionManager.beginDelayedTransition(binding.mainParent);

        addressBoxDefConstSet.applyTo(binding.mainAddressesLayout);

        parentConstSet.constrainHeight(binding.mainAddressesLayout.getId(), Math.round(convertDpToPixel(60, context)));

        parentConstSet.setVisibility(binding.mainResetLocationButton.getId(), ConstraintSet.VISIBLE);

        parentConstSet.applyTo(binding.mainParent);

        final int sdk = android.os.Build.VERSION.SDK_INT;

        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            binding.mainAddressesLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_white_cut_corners));
        } else {
            binding.mainAddressesLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_white_cut_corners));
        }

        mMap.clear();

    }

    private void makeUiChangesForPaymentChoice() {

        binding.mainTitleUpTxt.setText(getResources().getString(R.string.main_top_title_payment));

        binding.mainCTAButton.setText(getResources().getString(R.string.main_cta_button_callTaxi));

        getLocationFromMapCameraTarget = false;

        String lastPaymentId = prefs.getString("lastPaymentMethodSelected", null);

        if (lastPaymentId != null && !lastPaymentId.equalsIgnoreCase("cash")) {

            binding.mainBottomSelectedCardNumber.setText(
                    getResources().getString(R.string.main_card_dots_plus_numbers,
                            prefs.getString("card_last4", null))
            );

            if (prefs.getString("card_brand", null).equalsIgnoreCase("visa")) {

                binding.mainBottomPaymentImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_visa));

            } else {

                binding.mainBottomPaymentImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_mastercard));

            }


            currentRidePaymentId = lastPaymentId;
        }

        TransitionManager.beginDelayedTransition(binding.mainParent);

        parentConstSet.setVisibility(binding.mainClientMarker.getId(), ConstraintSet.GONE);

        parentConstSet.setVisibility(binding.mainSpotImage.getId(), ConstraintSet.GONE);

        parentConstSet.applyTo(binding.mainParent);

        bottomCarTypeLayDefSet.setVisibility(binding.bottomCarLay.getId(), ConstraintSet.GONE);

        bottomCarTypeLayDefSet.setVisibility(binding.bottomPaymentLay.getId(), ConstraintSet.VISIBLE);

        bottomCarTypeLayDefSet.applyTo(binding.mainCarTypeLay);

        mMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(destinationLocation.getLatitude(), destinationLocation.getLongitude()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination_marker))

        );

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(new LatLng(pickUpLocation.getLatitude(), pickUpLocation.getLongitude()));

        builder.include(new LatLng(destinationLocation.getLatitude(), destinationLocation.getLongitude()));

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));

        mMap.getUiSettings().setAllGesturesEnabled(false);

        binding.mainPickupAddressTxt.setOnClickListener(null);

        destinationAddressText.setOnClickListener(null);

        binding.mainCarTypeLay.setOnClickListener(v -> {
            PaymentMethodFragment paymentMethodFragment = new PaymentMethodFragment();

            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_from_top)
                    .replace(binding.mainFragmentContainer.getId(), paymentMethodFragment, "paymentMethodFragment")
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });

        presenter.getRouteDirections(pickUpLocation.getLatitude(), pickUpLocation.getLongitude(),
                destinationLocation.getLatitude(), destinationLocation.getLongitude());

    }

    private void makeUiChangesForResetToDestinationChoice() {

        binding.mainTitleUpTxt.setText(getResources().getString(R.string.main_top_title_dropoff));

        binding.mainCTAButton.setText(getResources().getString(R.string.main_cta_button_dropoff));

        getLocationFromMapCameraTarget = true;

        TransitionManager.beginDelayedTransition(binding.mainParent);

        parentConstSet.setVisibility(binding.mainClientMarker.getId(), ConstraintSet.VISIBLE);

        parentConstSet.setVisibility(binding.mainSpotImage.getId(), ConstraintSet.VISIBLE);

        parentConstSet.applyTo(binding.mainParent);

        bottomCarTypeLayDefSet.setVisibility(binding.bottomPaymentLay.getId(), ConstraintSet.GONE);

        bottomCarTypeLayDefSet.setVisibility(binding.bottomCarLay.getId(), ConstraintSet.VISIBLE);

        bottomCarTypeLayDefSet.applyTo(binding.mainCarTypeLay);

        mMap.clear();

        mMap.addMarker(
                new MarkerOptions()
                        .position(new LatLng(pickUpLocation.getLatitude(), pickUpLocation.getLongitude()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pick_up_marker))

        );

        mMap.getUiSettings().setAllGesturesEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(destinationLocation.getLatitude(), destinationLocation.getLongitude())));

        binding.mainPickupAddressTxt.setOnClickListener(v -> {

            textToUpdate = PICKUP_TEXT;

            SearchLocationFragment searchFragment = new SearchLocationFragment();

            Bundle bundle = new Bundle();

            bundle.putString("title", getResources().getString(R.string.main_top_title_pickup));

            bundle.putDouble("lat", pickUpLocation.getLatitude());

            bundle.putDouble("lng", pickUpLocation.getLongitude());

            searchFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_from_top)
                    .replace(binding.mainFragmentContainer.getId(), searchFragment, "searchLocationFragment")
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();

        });

        destinationAddressText.setOnClickListener(v -> {

            textToUpdate = DESTINATION_TEXT;

            SearchLocationFragment searchFragment = new SearchLocationFragment();

            Bundle bundle = new Bundle();

            bundle.putString("title", getResources().getString(R.string.main_top_title_dropoff));

            bundle.putDouble("lat", pickUpLocation.getLatitude());

            bundle.putDouble("lng", pickUpLocation.getLongitude());

            searchFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_from_top)
                    .replace(binding.mainFragmentContainer.getId(), searchFragment, "searchLocationFragment")
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();

        });

        binding.mainCarTypeLay.setOnClickListener(null);

    }

    private void makeUiChangesForSearchTaxiAnimation(){

        searchTaxiAnimationLayout = new ConstraintLayout(context);

        searchTaxiAnimationLayout.setId(View.generateViewId());

        binding.mainBottomContainer.removeView(binding.mainCarTypeLay);

        binding.mainBottomContainer.addView(searchTaxiAnimationLayout);

        bottomContainerConstSet.constrainHeight(searchTaxiAnimationLayout.getId(), Math.round(convertDpToPixel(60,context)));

        bottomContainerConstSet.connect(searchTaxiAnimationLayout.getId(),ConstraintSet.BOTTOM,
                binding.mainCTAButton.getId(),ConstraintSet.TOP, Math.round(convertDpToPixel(10,context)));

        bottomContainerConstSet.connect(searchTaxiAnimationLayout.getId(),ConstraintSet.START,
                ConstraintSet.PARENT_ID,ConstraintSet.START, Math.round(convertDpToPixel(16,context)));

        bottomContainerConstSet.connect(searchTaxiAnimationLayout.getId(),ConstraintSet.END,
                ConstraintSet.PARENT_ID,ConstraintSet.END, Math.round(convertDpToPixel(16,context)));

        searchTaxiLoadingMessage = new TextView(context);

        searchTaxiLoadingMessage.setId(View.generateViewId());

        ConstraintSet searchTaxiLoadingLayConstSet = new ConstraintSet();

        searchTaxiLoadingLayConstSet.clone(searchTaxiAnimationLayout);

        searchTaxiAnimationLayout.addView(searchTaxiLoadingMessage);

        searchTaxiLoadingMessage.setText(getResources().getString(R.string.main_searching_loading_message));
        searchTaxiLoadingMessage.setTextColor(Color.WHITE);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.inter_regular);
        searchTaxiLoadingMessage.setTypeface(typeface, Typeface.BOLD);
        searchTaxiLoadingMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        searchTaxiLoadingLayConstSet.constrainWidth(searchTaxiLoadingMessage.getId(),ConstraintSet.WRAP_CONTENT);
        searchTaxiLoadingLayConstSet.constrainHeight(searchTaxiLoadingMessage.getId(),ConstraintSet.WRAP_CONTENT);
        searchTaxiLoadingLayConstSet.connect(searchTaxiLoadingMessage.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP);
        searchTaxiLoadingLayConstSet.connect(searchTaxiLoadingMessage.getId(),ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START);
        searchTaxiLoadingLayConstSet.connect(searchTaxiLoadingMessage.getId(),ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END);
        searchTaxiLoadingLayConstSet.connect(searchTaxiLoadingMessage.getId(),ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM);

        searchTaxiLoadingLayConstSet.applyTo(searchTaxiAnimationLayout);

        bottomContainerConstSet.applyTo(binding.mainBottomContainer);

        TransitionManager.beginDelayedTransition(binding.mainParent);

        Animation fadeOut = new AlphaAnimation(1,0);
        fadeOut.setDuration(1000);
        fadeOut.setRepeatCount(Animation.INFINITE);
        fadeOut.setRepeatMode(Animation.REVERSE);

        searchTaxiLoadingMessage.startAnimation(fadeOut);

        binding.mainTitleUpTxt.setText(getResources().getString(R.string.main_top_title_searching));

        binding.mainCTAButton.setText(getResources().getString(R.string.main_cta_button_cancel));

    }

    private void makeUiChangesForResetToPaymentChoice(){

        searchTaxiLoadingMessage.clearAnimation();

        binding.mainBottomContainer.removeView(searchTaxiAnimationLayout);

        binding.mainBottomContainer.addView(binding.mainCarTypeLay);

        binding.mainTitleUpTxt.setText(getResources().getString(R.string.main_top_title_payment));

        binding.mainCTAButton.setText(getResources().getString(R.string.main_cta_button_callTaxi));

    }

    private void makeUiChangesToShowComingDriverLayout(){
        searchTaxiLoadingMessage.clearAnimation();

        binding.mainTitleUpTxt.setText(getResources().getString(R.string.main_top_title_driverComing));

        TransitionManager.beginDelayedTransition(binding.mainParent);

        parentConstSet.setVisibility(binding.mainBottomContainer.getId(),ConstraintSet.GONE);

        parentConstSet.connect(binding.mainComingDriverLayout.getId(),ConstraintSet.TOP,
                binding.mainAddressesLayout.getId(),ConstraintSet.BOTTOM,Math.round(convertDpToPixel(16,context)));

        parentConstSet.applyTo(binding.mainParent);

        isDriverInfoContainerExpanded = true;
    }

    private void makeUiChangesForExpandDriverContainer(){

        TransitionManager.beginDelayedTransition(binding.mainParent);

        parentConstSet.constrainHeight(binding.mainComingDriverLayout.getId(),0);

        parentConstSet.connect(binding.mainComingDriverLayout.getId(),ConstraintSet.TOP,
                binding.mainAddressesLayout.getId(),ConstraintSet.BOTTOM,Math.round(convertDpToPixel(16,context)));

        parentConstSet.applyTo(binding.mainParent);

        comingDriverLayConstSet.constrainHeight(binding.mainDriverComingInfoLay.getId(),0);
        comingDriverLayConstSet.applyTo(binding.mainComingDriverLayout);

        comingDriverInfoLayConstSet.clear(binding.mainDriverComingCarInfoLay.getId(),ConstraintSet.BOTTOM);
        comingDriverInfoLayConstSet.setVisibility(binding.mainComingDriverMessagerLay.getId(),ConstraintSet.VISIBLE);
        comingDriverInfoLayConstSet.setVisibility(binding.mainComingDriverButtonsLay.getId(),ConstraintSet.VISIBLE);
        comingDriverInfoLayConstSet.applyTo(binding.mainDriverComingInfoLay);

    }

    private void makeUiChangesForShrinkDriverContainer(){

        comingDriverInfoLayConstSet.setVisibility(binding.mainComingDriverMessagerLay.getId(),ConstraintSet.GONE);
        comingDriverInfoLayConstSet.setVisibility(binding.mainComingDriverButtonsLay.getId(),ConstraintSet.GONE);
        comingDriverInfoLayConstSet.connect(binding.mainDriverComingCarInfoLay.getId(),ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,
                Math.round(convertDpToPixel(8,context)));
        comingDriverInfoLayConstSet.applyTo(binding.mainDriverComingInfoLay);

        comingDriverLayConstSet.constrainHeight(binding.mainDriverComingInfoLay.getId(),ConstraintSet.WRAP_CONTENT);
        comingDriverLayConstSet.applyTo(binding.mainComingDriverLayout);

        parentConstSet.clear(binding.mainComingDriverLayout.getId(),ConstraintSet.TOP);

        parentConstSet.constrainHeight(binding.mainComingDriverLayout.getId(),ConstraintSet.WRAP_CONTENT);

        parentConstSet.applyTo(binding.mainParent);

        TransitionManager.beginDelayedTransition(binding.mainParent);

    }

    private void makeUiChangesForDriverArrival(){

        Toast.makeText(context,"Driver Arrived", Toast.LENGTH_LONG).show();

        binding.mainComingDriverArrivalTimeTxtOne.setText("Έφτασε");

        binding.mainComingDriverArrivalTimeTxtTwo.setVisibility(View.GONE);

        Animation fadeOutIn = new AlphaAnimation(1, 0);
        fadeOutIn.setInterpolator(new AccelerateInterpolator());
        fadeOutIn.setDuration(1000);
        fadeOutIn.setRepeatMode(Animation.REVERSE);
        fadeOutIn.setRepeatCount(Animation.INFINITE);

        binding.mainComingDriverArrivalTimeLayout.startAnimation(fadeOutIn);

    }

    private void  makeUiChangesForPassengerOnBoard(){
        Toast.makeText(context,"Passenger on Board",Toast.LENGTH_SHORT).show();

        binding.mainComingDriverArrivalTimeLayout.clearAnimation();

        makeUiChangesForShrinkDriverContainer();

        binding.mainTitleUpTxt.setText("Η διαδρομή σου ξεκίνησε");

        binding.mainComingDriverImg.setOnClickListener(null);
    }



    private void hideLoadingLayout() {

        TransitionManager.beginDelayedTransition(binding.mainParent);

        parentConstSet.setVisibility(binding.mainLoadingLayout.getId(), ConstraintSet.GONE);

        parentConstSet.applyTo(binding.mainParent);
    }

//    private void showLoadingLayout() {
//
//        TransitionManager.beginDelayedTransition(binding.mainParent);
//
//        defaultConstSet.setVisibility(binding.mainLoadingLayout.getId(), ConstraintSet.VISIBLE);
//
//        defaultConstSet.applyTo(binding.mainParent);
//
//    }

    private void hideFragmentContainer() {
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_from_top)
                .replace(binding.mainFragmentContainer.getId(), new Fragment())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();

    }

    private void sendRideRequestToDrivers() {

        fcmBroadcastReceiver = initializeBroadcastReceiver();

        LocalBroadcastManager.getInstance(context).registerReceiver(fcmBroadcastReceiver
                ,new IntentFilter("FCM MainActivity")
        );

        presenter.sendRideToDrivers();
    }

    private BroadcastReceiver initializeBroadcastReceiver(){

        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if(intent.getExtras() != null){

                    if(Objects.requireNonNull(intent.getExtras().getString("message_action"))
                            .equalsIgnoreCase("sft")){

                        currentRideId =intent.getExtras().getInt("ride_id");

                        comingDriverId = intent.getExtras().getString("driver_id");

                        Log.d("blepo" ,"To ride id sto main apo not einai: " + currentRideId);

                        Log.d("blepo" ,"To driver id sto main apo not einai: " + comingDriverId);

                        presenter.getComingDriverInfo(comingDriverId,currentRideId);

                    }


                    if(Objects.requireNonNull(intent.getExtras().getString("message_action"))
                            .equalsIgnoreCase("htr")){

                        if(intent.getStringExtra("action_type").equalsIgnoreCase("driver_arrived")){
                            makeUiChangesForDriverArrival();
                        }

                        if(intent.getStringExtra("action_type").equalsIgnoreCase("passenger_onboard")){

                            clientIsOnBoard = true;

                            makeUiChangesForPassengerOnBoard();

                            presenter.getComingDriverLocation(comingDriverId);

                        }

                        if(intent.getStringExtra("action_type").equalsIgnoreCase("ride_completed")){

                            Intent intent1 = new Intent(MainActivity.this, EndRideActivity.class);

                            intent1.putExtra("driverID", comingDriverId);

                            intent1.putExtra("rideID", currentRideId);

                            startActivity(intent1, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());

                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                            MainActivity.this.finish();
                        }

                    }

                }

            }
        };
    }
}

