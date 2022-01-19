package com.redpepper.taxiapp.Signup;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;

import io.reactivex.Single;

public interface SignUpActivityRepository {
    Single<SimpleResponse> getRegitrationInfoFromServer(String name, String surname, String phone);
}
