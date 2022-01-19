package com.redpepper.taxiapp.Splash;

import android.content.SharedPreferences;

import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;


public class SplashActivityRepositoryImpl implements SplashActivityRepository {

    SharedPreferences prefs;
    PassengerApiService service;
    String token;


    public SplashActivityRepositoryImpl(SharedPreferences prefs, PassengerApiService service) {

        this.prefs = prefs;
        this.service = service;
        this.token = "Bearer " + prefs.getString("access_Token",null);
    }

    @Override
    public boolean checkUserLogin() {

        return prefs.getBoolean("User_is_logged",false);

    }

}

//    Gson gson = new Gson();
//
//    String json = prefs.getString("SignInUser", "");
//
//        if(!json.trim().equals("")){
//
//                //convert json from prefs to Passenger Object
//                return gson.fromJson(json,Passenger.class);
//        }
//
//    Editor prefsEditor = mPrefs.edit();
//    Gson gson = new Gson();
//    String json = gson.toJson(MyObject);
//prefsEditor.putString("MyObject", json);
//        prefsEditor.commit();