package com.redpepper.taxiapp.Validate_password;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
import com.redpepper.taxiapp.Http.ResponseModels.TokenResponse;

import io.reactivex.Single;

public class ValidatePasswordActivityModel implements ValidatePasswordActivityMVP.Model {

    private ValidatePasswordActivityRepository repository;

    public ValidatePasswordActivityModel(ValidatePasswordActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<SimpleResponse> getLoginResponse(String phone, String password) {
        return repository.getValidateResponse(phone,password);
    }

    @Override
    public Single<SimpleResponse> getResentPasswordResponse(String phone) {
        return repository.getResentPasswordResponse(phone);
    }

    @Override
    public Single<TokenResponse> getTokenResponse(String phone, String password) {
        return repository.getUserToken(phone,password);
    }

    @Override
    public void storeLoginState() {
        repository.storeLoginState();
    }

    @Override
    public void storeUserToken(String token) {
        repository.storeUserToken(token);
    }

    @Override
    public void storeRefreshToken(String refreshToken) {
        repository.storeRefreshToken(refreshToken);
    }

    @Override
    public void storeExrirationTime(int expirationTime) {
        repository.storeExrirationTime(expirationTime);
    }
}
