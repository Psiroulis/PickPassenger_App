package com.redpepper.taxiapp.End_ride;


import com.redpepper.taxiapp.Http.ResponseModels.AcceptedDriverInfoResponse;
import com.redpepper.taxiapp.Http.ResponseModels.RideResponse;

import io.reactivex.Single;
import retrofit2.Response;

public interface EndRideActivityMVP {

    interface Model{
        Single<Response<RideResponse>> getRideInfo(int rideId);
        Single<Response<AcceptedDriverInfoResponse>> getDriverInfo(String driverId);
    }

    interface View{
        void setRideInfo(RideResponse rideInfo);

        void setDriverInfo(AcceptedDriverInfoResponse driverInfo);

        void moveToMainScreen();

    }

    interface Presenter{
        void setView(EndRideActivityMVP.View view);

        void getRideInfo(int rideId);

        void getDriverInfo(String driverId);

        void sendDriverRating(float rating);

        void rxUnsubscribe();
    }

}
