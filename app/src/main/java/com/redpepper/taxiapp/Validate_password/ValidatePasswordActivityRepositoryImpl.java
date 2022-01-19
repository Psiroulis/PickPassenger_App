package com.redpepper.taxiapp.Validate_password;

import android.content.SharedPreferences;
import android.util.Log;

import com.redpepper.taxiapp.Http.PostModels.ResentPasswordSmsPost;
import com.redpepper.taxiapp.Http.PostModels.TokenPost;
import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
import com.redpepper.taxiapp.Http.PostModels.ValidatePasswordPost;
import com.redpepper.taxiapp.Http.ResponseModels.TokenResponse;
import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import io.reactivex.Single;

public class ValidatePasswordActivityRepositoryImpl implements ValidatePasswordActivityRepository {

    private SharedPreferences prefs;
    private PassengerApiService service;

    public ValidatePasswordActivityRepositoryImpl(SharedPreferences prefs, PassengerApiService service) {
        this.prefs = prefs;
        this.service = service;
    }

    @Override
    public Single<SimpleResponse> getValidateResponse(String phone, String password) {
        return service.login(new ValidatePasswordPost(phone,password));
    }

    @Override
    public Single<SimpleResponse> getResentPasswordResponse(String phone) {
        return service.resetPassword(new ResentPasswordSmsPost(phone));
    }

    @Override
    public Single<TokenResponse> getUserToken(String phone, String password) {
        return service.getToken(new TokenPost(phone,password));
    }

    @Override
    public void storeLoginState() {
        SharedPreferences.Editor  editor = prefs.edit();
        editor.putBoolean("User_is_logged", true);
        editor.commit();
    }

    @Override
    public void storeUserToken(String token) {
        SharedPreferences.Editor  editor = prefs.edit();
        editor.putString("access_Token", token);
        editor.commit();
        Log.d("blepo", "egine save");
    }

    @Override
    public void storeRefreshToken(String refreshToken) {
        SharedPreferences.Editor  editor = prefs.edit();
        editor.putString("refresh_token", refreshToken);
        editor.commit();
    }

    @Override
    public void storeExrirationTime(int expireTime) {
        SharedPreferences.Editor  editor = prefs.edit();
        editor.putInt("expire_time", expireTime);
        editor.commit();
    }
}
