package com.redpepper.taxiapp.End_ride;

import com.redpepper.taxiapp.Http.ResponseModels.AcceptedDriverInfoResponse;
import com.redpepper.taxiapp.Http.ResponseModels.RideResponse;

import io.reactivex.Single;
import retrofit2.Response;

public class EndRideActivityModel implements EndRideActivityMVP.Model {

    Repository repository;

    public EndRideActivityModel(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Single<Response<RideResponse>> getRideInfo(int rideId) {
        return repository.getRideInfo(rideId);
    }

    @Override
    public Single<Response<AcceptedDriverInfoResponse>> getDriverInfo(String driverId) {
        return repository.getDriverInfo(driverId);
    }
}
