package com.redpepper.taxiapp.End_ride;


import com.redpepper.taxiapp.Http.ResponseModels.AcceptedDriverInfoResponse;
import com.redpepper.taxiapp.Http.ResponseModels.RideResponse;
import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;

import io.reactivex.Single;
import retrofit2.Response;

public interface Repository {

    Single<Response<RideResponse>> getRideInfo(int rideId);
    Single<Response<AcceptedDriverInfoResponse>> getDriverInfo(String driverId);
    Single<Response<SimpleResponse>> storeRideRating(int rideId, float rating);
}
