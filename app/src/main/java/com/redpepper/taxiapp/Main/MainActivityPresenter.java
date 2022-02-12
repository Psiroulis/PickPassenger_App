package com.redpepper.taxiapp.Main;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.redpepper.taxiapp.Utils.PriceCalculatorUtil;
import com.redpepper.taxiapp.Http.ResponseModels.AcceptedDriverInfoResponse;
import com.redpepper.taxiapp.Http.ResponseModels.ComingDriverLocationResponse;
import com.redpepper.taxiapp.Http.ResponseModels.DirectionPathResponse;
import com.redpepper.taxiapp.Http.ResponseModels.NearestDriverTimeResponse;
import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MainActivityPresenter implements MainActivityMVP.Presenter {

    MainActivityMVP.Model model;
    MainActivityMVP.View view;

    private CompositeDisposable subscription;

    public MainActivityPresenter(MainActivityMVP.Model model, Context context) {

        this.model = model;

        subscription = new CompositeDisposable();

    }

    @Override
    public void setView(MainActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void getLastKnownLocation() {
        if(model.getLastKnownLocation() != null){
            subscription.add(
                    model.getLastKnownLocation()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    view.putLastKnownLocationOnMap(location);

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Location emptyLoc = new Location(LocationManager.GPS_PROVIDER);
                                    emptyLoc.setLatitude(37.984153);
                                    emptyLoc.setLongitude(23.728065);
                                    view.putLastKnownLocationOnMap(emptyLoc);

                                }
                            })
            );

        }

    }

    @Override
    public void getAddressFromLocation(Location location) {
        subscription.add(
        model.getAddressFromLocation(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ArrayList<String>>() {
                    @Override
                    public void onSuccess(ArrayList<String> strings) {

                        if (view != null){

                            if(strings.get(0).equalsIgnoreCase("null")
                                    ||strings.get(0).equalsIgnoreCase("Service Is Not Availiable")
                                    ||strings.get(0).equalsIgnoreCase("Invalid Lat Lng Used")
                                    ||strings.get(0).equalsIgnoreCase("No Address found")){


                                //Todo:show error message

                            }else{

                                String[] addArray= strings.get(0).split(",");

                                view.setAddressButtonText(addArray[0],strings.get(0));

                            }

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("blepo","Address Error"+ e.getMessage());
                    }
                })
        );
    }

    @Override
    public void getRouteDirections(double originLat, double originLng, double destinationLat, double destinationLng) {
        subscription.add(model.getDirections(originLat + "," + originLng, destinationLat + "," + destinationLng).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<DirectionPathResponse>>(){
                    @Override
                    public void onSuccess(Response<DirectionPathResponse> directionPathResponseResponse) {
                        if(directionPathResponseResponse.body().getSuccess().equalsIgnoreCase("true")){

                            view.showDirectionsOnMap(directionPathResponseResponse.body().getPoints());

                            PriceCalculatorUtil priceCalculatorUtil = new PriceCalculatorUtil();

                            view.showRideCost(priceCalculatorUtil.calculatePrice(directionPathResponseResponse.body().getDistance()));

                        }else{

                            Log.e("blepo", "Error message:" + directionPathResponseResponse.body().getMessage());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })

        );
    }

    @Override
    public void getComingDriverRoute(double originLat, double originLng, double destinationLat, double destinationLng) {
        subscription.add(model.getDirections(originLat + "," + originLng, destinationLat + "," + destinationLng).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<DirectionPathResponse>>(){
                    @Override
                    public void onSuccess(Response<DirectionPathResponse> response) {
                        if(response.body().getSuccess().equalsIgnoreCase("true")){
                            view.showComingDriverDirectionsOnMap(response.body().getPoints(),
                                    response.body().getDuration());
                        }else{

                            Log.e("blepo", "Error message:" + response.body().getMessage());


                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                })

        );
    }

    @Override
    public void getNearestDriverArrivalTime(double destinationLat, double destinationLng) {
        subscription.add(
                model.getNearestDriverArrivalTime(destinationLat + "," + destinationLng)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Response<NearestDriverTimeResponse>>(){
                            @Override
                            public void onSuccess(Response<NearestDriverTimeResponse> nearestDriverTimeResponseResponse) {

                                if(nearestDriverTimeResponseResponse.body().getSuccess().equalsIgnoreCase("true")){

                                    view.showNearestDriverArrivalTime(String.valueOf(nearestDriverTimeResponseResponse.body().getTime() / 60));

                                }else{

                                    Log.e("blepo", "Error message:" + nearestDriverTimeResponseResponse.body().getMessage());

                                    view.showNoDriverInRangeMessage();

                                }

                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }));
    }

    @Override
    public void storeUserFcmToken(String fcmToken) {

        subscription.add(
                model.storeUserFCMToken(fcmToken)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Response<SimpleResponse>>(){
                            @Override
                            public void onSuccess(@NonNull Response<SimpleResponse> response) {

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        })
        );
    }

    @Override
    public void createUserDevice() {
        subscription.add(
          model.createUserDevice()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeWith(new DisposableSingleObserver<Response<SimpleResponse>>(){
              @Override
              public void onSuccess(@NonNull Response<SimpleResponse> response) {
                  if(response.body().getSuccess().equalsIgnoreCase("true")){

                  }else{
                      Log.e("blepo","Error at creating user device: " + response.body().getMessage());
                  }
              }

              @Override
              public void onError(@NonNull Throwable e) {

                  Log.e("blepo", "MainPresenter:creteUserDevice Error: " + e.getMessage());
              }
          })
        );
    }

    @Override
    public void sendRideToDrivers() {

    }

    @Override
    public void getComingDriverInfo(String driverId, int rideId) {
        subscription.add(
                model.getComingDriverInfo(driverId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Response<AcceptedDriverInfoResponse>>(){
                            @Override
                            public void onSuccess(@NonNull Response<AcceptedDriverInfoResponse> response) {
                                view.showComingDriverInfo(driverId, rideId, response.body());
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        })
        );
    }

    @Override
    public void fillRideInfo(String driverId, int rideId, String pickupAddress, double pickupLatitude,
                             double pickupLongitude, String destinationAddress,
                             double destinationLatitude, double destinationLongitude,
                             String paymentMethodId) {

        subscription.add(
                model.fillRideInfo(rideId,pickupAddress, pickupLatitude, pickupLongitude, destinationAddress,
                        destinationLatitude, destinationLongitude, paymentMethodId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Response<SimpleResponse>>(){
                            @Override
                            public void onSuccess(@NonNull Response<SimpleResponse> response) {
                                Log.d("blepo","MainActivityPresenter: Mpike to info sto ride, Response Code:"+ response.code());
                                view.rideInfoUpdated(driverId);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e("blepo","Not success to add ride info in mainActivity presenter. Retrying" + e.getMessage());
                                fillRideInfo(driverId,rideId,pickupAddress, pickupLatitude, pickupLongitude, destinationAddress,
                                        destinationLatitude, destinationLongitude, paymentMethodId);
                            }
                        })

        );

    }

    @Override
    public void getComingDriverLocation(String driverId) {
        subscription.add(
                model.getComingDriverLocation(driverId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<ComingDriverLocationResponse>>(){
                    @Override
                    public void onSuccess(@NonNull Response<ComingDriverLocationResponse> response) {
                        LatLng driverLocation = new LatLng(response.body().getLatitude(),
                                response.body().getLongitude());

                        view.updateDriverLocationOnMap(driverLocation);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                })
        );
    }

    @Override
    public void rxUnsubscribe() {
        if(subscription != null){
            if(!subscription.isDisposed()){
                subscription.dispose();
            }
        }
    }
}
