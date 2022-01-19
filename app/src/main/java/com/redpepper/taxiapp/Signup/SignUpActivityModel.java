package com.redpepper.taxiapp.Signup;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;

import io.reactivex.Single;

public class SignUpActivityModel implements SignUpActivityMVP.Model {

    SignUpActivityRepository repository;

    public SignUpActivityModel(SignUpActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<SimpleResponse> getSignUpResponse(String name, String surname, String phone) {

        return repository.getRegitrationInfoFromServer(name, surname,phone);
    }
}
