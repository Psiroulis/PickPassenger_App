package com.redpepper.taxiapp.Login;


import com.redpepper.taxiapp.Http.PostModels.UserExistsPost;
import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import io.reactivex.Single;

public class LoginActivityRepositoryImpl implements LoginActivityRepository {

    PassengerApiService apiService;

    public LoginActivityRepositoryImpl(PassengerApiService service) {

        this.apiService = service;
    }

    @Override
    public Single<SimpleResponse> getIfUserExists(String phone) {
        return apiService.checkUserExists(new UserExistsPost(phone));
    }

    //seconds in month = 2592000
}
