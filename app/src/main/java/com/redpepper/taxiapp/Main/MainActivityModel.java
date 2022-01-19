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

public class MainActivityModel implements MainActivityMVP.Model {

    Repository repository;

    public MainActivityModel(Repository repository) {

        this.repository = repository;
    }

    @Override
    public Single<Location> getLastKnownLocation() {
        return repository.getLastKnownLocation();
    }

    @Override
    public Single<ArrayList<String>> getAddressFromLocation(Location location) {
        return repository.getAddress(location);
    }

    @Override
    public Single<Response<DirectionPathResponse>> getDirections(String originCoords, String destinationCoords) {
        return repository.getDirections(originCoords, destinationCoords);
    }

    @Override
    public Single<Response<NearestDriverTimeResponse>> getNearestDriverArrivalTime(String destinationCoords) {
        return repository.getNearestDriverArrivalTime(destinationCoords);
    }

    @Override
    public Single<Response<SimpleResponse>> storeUserFCMToken(String fcmToken) {
        return repository.storeUserFCMToken(fcmToken);
    }

    @Override
    public Single<Response<SimpleResponse>> createUserDevice() {
        return repository.createUserDevice();
    }

    @Override
    public Single<Response<AcceptedDriverInfoResponse>> getComingDriverInfo(String driverId) {
        return repository.getComingDriverInfo(driverId);
    }

    @Override
    public Single<Response<SimpleResponse>> fillRideInfo(int rideId, String pickupAddress,
                                                         double pickupLatitude, double pickupLongitude,
                                                         String destinationAddress, double destinationLatitude,
                                                         double destinationLongitude, String paymentMethodId) {

        return repository.fillRideInfo(rideId, pickupAddress, pickupLatitude, pickupLongitude, destinationAddress,
                destinationLatitude, destinationLongitude, paymentMethodId);
    }

    @Override
    public Single<Response<ComingDriverLocationResponse>> getComingDriverLocation(String driverId) {
        return repository.getComingDriverLocation(driverId);
    }
}
