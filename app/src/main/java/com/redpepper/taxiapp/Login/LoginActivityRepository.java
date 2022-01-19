package com.redpepper.taxiapp.Login;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;

import io.reactivex.Single;

public interface LoginActivityRepository {

    Single<SimpleResponse> getIfUserExists(String phone);

}
