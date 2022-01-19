package com.redpepper.taxiapp.Main;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.patloew.rxlocation.RxLocation;
import com.redpepper.taxiapp.Http.PostModels.DirectionPathPost;
import com.redpepper.taxiapp.Http.PostModels.Fcm.PostFcmToken;
import com.redpepper.taxiapp.Http.PostModels.Fcm.UserDevicePost;
import com.redpepper.taxiapp.Http.PostModels.NearestDriverTimePost;
import com.redpepper.taxiapp.Http.PostModels.RidePost;
import com.redpepper.taxiapp.Http.ResponseModels.AcceptedDriverInfoResponse;
import com.redpepper.taxiapp.Http.ResponseModels.ComingDriverLocationResponse;
import com.redpepper.taxiapp.Http.ResponseModels.DirectionPathResponse;
import com.redpepper.taxiapp.Http.ResponseModels.NearestDriverTimeResponse;
import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;
import com.redpepper.taxiapp.Services.FetchAddressService;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.Response;

public class MainActivityRepository implements Repository {

    PassengerApiService apiService;
    SharedPreferences prefs;
    Context context;
    RxLocation rxLocation;
    String token;

    public MainActivityRepository(SharedPreferences prefs, PassengerApiService service, Context context, RxLocation rxLocation) {

        this.prefs = prefs;
        this.apiService = service;

        this.context = context;
        this.rxLocation = rxLocation;
        this.token = "Bearer " + prefs.getString("access_Token",null);
        Log.d("blepo", "token:-> " + token);
    }

    @Override
    public Single<Location> getLastKnownLocation() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        return rxLocation.location().lastLocation().toSingle();

    }

    @Override
    public Single<ArrayList<String>> getAddress(Location location) {

        FetchAddressService service = new FetchAddressService(context,location);

        return service.getAddress();
    }

    @Override
    public Single<Response<DirectionPathResponse>> getDirections(String originCoords, String destinationCoords) {
        return apiService.getDirectionsPath(token,new DirectionPathPost(originCoords,destinationCoords));
    }

    @Override
    public Single<Response<NearestDriverTimeResponse>> getNearestDriverArrivalTime(String destinationCoords) {
        return apiService.getNearestDriverArrivalTime(token,new NearestDriverTimePost(destinationCoords));
    }


    @Override
    public Single<Response<SimpleResponse>> storeUserFCMToken(String fcmToken) {
        return apiService.storeUserFcmToken(token,new PostFcmToken(fcmToken));
    }

    @Override
    public Single<Response<SimpleResponse>> createUserDevice() {
        Log.d("blepo", "main repository fbr_in:"+ prefs.getString("frb_installation_id","") );
        Log.d("blepo", "main repository fcm_tok:"+  prefs.getString("fcm_token","") );
        return apiService.createDevice(token,new UserDevicePost(
               prefs.getString("frb_installation_id",""),
               prefs.getString("fcm_token","")));
    }

    @Override
    public Single<Response<AcceptedDriverInfoResponse>> getComingDriverInfo(String driverId) {
        return apiService.getAcceptedDriverInfos(token,driverId);
    }

    @Override
    public Single<Response<SimpleResponse>> fillRideInfo(int rideId, String pickupAddress,
                                                         double pickupLatitude, double pickupLongitude,
                                                         String destinationAddress, double destinationLatitude,
                                                         double destinationLongitude, String paymentMethodId) {

        return apiService.addInfoToRide(token, rideId, new RidePost(pickupAddress, pickupLatitude,
                pickupLongitude, destinationAddress, destinationLatitude, destinationLongitude, paymentMethodId));
    }

    @Override
    public Single<Response<ComingDriverLocationResponse>> getComingDriverLocation(String driverId) {
        return apiService.getComingDriverLocation(token,driverId);
    }
}


