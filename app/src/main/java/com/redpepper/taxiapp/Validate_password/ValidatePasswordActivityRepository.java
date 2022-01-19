package com.redpepper.taxiapp.Validate_password;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
import com.redpepper.taxiapp.Http.ResponseModels.TokenResponse;

import io.reactivex.Single;

public interface ValidatePasswordActivityRepository {

    Single<SimpleResponse> getValidateResponse(String phone, String password);

    Single<SimpleResponse> getResentPasswordResponse(String phone);

    Single<TokenResponse> getUserToken(String phone, String password);

    void storeLoginState();

    void storeUserToken(String token);

    void storeRefreshToken(String refreshToken);

    void storeExrirationTime(int expireTime);
}
