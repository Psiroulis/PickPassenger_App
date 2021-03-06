package com.redpepper.taxiapp.End_ride;

import android.content.SharedPreferences;

import com.redpepper.taxiapp.Http.ResponseModels.AcceptedDriverInfoResponse;
import com.redpepper.taxiapp.Http.ResponseModels.RideResponse;
import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import io.reactivex.Single;
import retrofit2.Response;

public class EndRideActivityRepository implements Repository {

    PassengerApiService apiService;
    String token;

    public EndRideActivityRepository(PassengerApiService apiService, SharedPreferences prefs) {

        this.apiService =apiService;
        this.token = "Bearer " + prefs.getString("access_Token",null);
    }

    @Override
    public Single<Response<RideResponse>> getRideInfo(int rideId) {
        return apiService.getRideInfo(token,rideId);
    }

    @Override
    public Single<Response<AcceptedDriverInfoResponse>> getDriverInfo(String driverId) {
        return apiService.getAcceptedDriverInfos(token,driverId);
    }

    @Override
    public Single<Response<SimpleResponse>> storeRideRating(int rideId, float rating) {
        return apiService.addRideRating(token, rideId, (int)rating);
    }
}
