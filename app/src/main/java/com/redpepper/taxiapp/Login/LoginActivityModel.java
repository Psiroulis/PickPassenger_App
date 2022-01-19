package com.redpepper.taxiapp.Login;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;

import io.reactivex.Single;

class LoginActivityModel implements LoginActivityMVP.Model {

    private LoginActivityRepository repository;

    public LoginActivityModel(LoginActivityRepository repository) {

        this.repository = repository;
    }

    @Override
    public Single<SimpleResponse> getUserExistResponse(String phone) {
        return repository.getIfUserExists(phone);
    }
}
