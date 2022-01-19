package com.redpepper.taxiapp.End_ride;


import com.redpepper.taxiapp.Http.ResponseModels.RideResponse;

import io.reactivex.Single;
import retrofit2.Response;

public interface EndRideActivityMVP {

    interface Model{
        Single<Response<RideResponse>> getRideInfo(int rideId);
    }

    interface View{
        void setRideInfo(RideResponse rideInfo);
    }

    interface Presenter{
        void setView(EndRideActivityMVP.View view);

        void getRideInfo(int rideId);

        void rxUnsubscribe();
    }

}
