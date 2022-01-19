package com.redpepper.taxiapp.Main;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.redpepper.taxiapp.Http.ResponseModels.AcceptedDriverInfoResponse;
import com.redpepper.taxiapp.Http.ResponseModels.ComingDriverLocationResponse;
import com.redpepper.taxiapp.Http.ResponseModels.DirectionPathResponse;
import com.redpepper.taxiapp.Http.ResponseModels.NearestDriverTimeResponse;
import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;

public interface MainActivityMVP {
    interface Model{

        Single<Location> getLastKnownLocation();

        Single<ArrayList<String>> getAddressFromLocation(Location location);

        Single<Response<DirectionPathResponse>> getDirections(String originCoords, String destinationCoords);

        Single<Response<NearestDriverTimeResponse>> getNearestDriverArrivalTime(String destinationCoords);

        Single<Response<SimpleResponse>> storeUserFCMToken(String fcmToken);

        Single<Response<SimpleResponse>> createUserDevice();

        Single<Response<AcceptedDriverInfoResponse>> getComingDriverInfo(String driverId);

        Single<Response<SimpleResponse>> fillRideInfo(int rideId, String pickupAddress,
                                                      double pickupLatitude, double pickupLongitude,
                                                      String destinationAddress, double destinationLatitude,
                                                      double destinationLongitude, String paymentMethodId);

        Single<Response<ComingDriverLocationResponse>> getComingDriverLocation(String driverId);




    }

    interface View{

        void putLastKnownLocationOnMap(Location Location);

        void setAddressButtonText(String adresssToshow, String fulladdress);

        void showDirectionsOnMap(List<String> points);

        void showComingDriverDirectionsOnMap(List<String> points,int duration);

        void showRideCost(String amount);

        void showNearestDriverArrivalTime(String time);

        void showComingDriverInfo(String driverId, int rideId, AcceptedDriverInfoResponse driverInfo);

        void updateDriverLocationOnMap(LatLng driverLocation);

        void rideInfoUpdated(String driverId);
    }

    interface Presenter{

        void setView(MainActivityMVP.View view);

        void getLastKnownLocation();

        void getAddressFromLocation(Location location);

        void getRouteDirections(double originLat, double originLng, double destinationLat, double destinationLng);

        void getNearestDriverArrivalTime(double destinationLat, double destinationLng);

        void storeUserFcmToken(String fcmToken);

        void createUserDevice();

        void sendRideToDrivers();

        void getComingDriverInfo(String driverId, int rideId);

        void fillRideInfo(String driverId, int rideId, String pickupAddress,
                          double pickupLatitude, double pickupLongitude,
                          String destinationAddress, double destinationLatitude,
                          double destinationLongitude, String paymentMethodId);

        void getComingDriverLocation(String driverId);

        void getComingDriverRoute(double originLat, double originLng, double destinationLat, double destinationLng);




        void rxUnsubscribe();
    }
}
