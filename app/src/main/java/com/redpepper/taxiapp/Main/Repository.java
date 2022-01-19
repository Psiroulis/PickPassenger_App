package com.redpepper.taxiapp.Main;

import android.location.Location;

import com.redpepper.taxiapp.Http.ResponseModels.AcceptedDriverInfoResponse;
import com.redpepper.taxiapp.Http.ResponseModels.ComingDriverLocationResponse;
import com.redpepper.taxiapp.Http.ResponseModels.DirectionPathResponse;
import com.redpepper.taxiapp.Http.ResponseModels.NearestDriverTimeResponse;
import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.Response;

public interface Repository {
    Single<Location> getLastKnownLocation();
    Single<ArrayList<String>> getAddress(Location location);
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
