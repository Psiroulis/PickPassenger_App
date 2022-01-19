package com.redpepper.taxiapp.Services;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;

public class FetchAddressService {

    private Location location;

    private final static String TAG = "FETCH LOCATION SERVICE";

    Context context;

    public FetchAddressService(Context context,Location location) {

        this.location = location;

        this.context = context;

    }

    public Single<ArrayList<String>> getAddress(){

        ArrayList<String> response = new ArrayList<>();

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addresses = null;

        try{

            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

        }catch (IOException ioexeption){

            Log.e(TAG, "Service Is Not Availiable",ioexeption);

            response.add("Service Is Not Availiable");

            return Single.just(response);

        }catch (IllegalArgumentException illArgException){

            Log.e(TAG, "Invalid Lat Lng Used",illArgException);

            response.add("Invalid Lat Lng Used");

            return Single.just(response);

        }

        if(addresses == null || addresses.size() == 0){

            Log.e(TAG,"No Address found");

            response.add("No Address found");

            return Single.just(response);

        }else{

            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();

            for( int i = 0; i <= address.getMaxAddressLineIndex(); i++ ){
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG,"Adress Found");

            return Single.just(addressFragments);

        }

    }
}
