package com.redpepper.taxiapp.Signup;

import com.redpepper.taxiapp.Http.PostModels.SignUpPost;
import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import io.reactivex.Single;

public class SignUpActivityRepositoryImpl implements SignUpActivityRepository {

    PassengerApiService apiService;

    public SignUpActivityRepositoryImpl(PassengerApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Single<SimpleResponse> getRegitrationInfoFromServer(String name, String surname, String phone) {
        return apiService.register(new SignUpPost(name, surname, phone));
    }
}
