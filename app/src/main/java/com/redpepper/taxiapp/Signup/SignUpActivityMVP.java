package com.redpepper.taxiapp.Signup;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;

import io.reactivex.Single;

public interface SignUpActivityMVP {
    interface Model{

        Single<SimpleResponse> getSignUpResponse(String name, String Surname, String phone);

    }


    interface View{

        void proceedToValidatePasswordActivity();

        void showCreateUserErrorMessage(String errorMessage);

    }

    interface Presenter{

        void setView(SignUpActivityMVP.View view);

        void registerUser(String name, String surname, String phoneNumber);

        void rxUnsubscribe();

    }
}
