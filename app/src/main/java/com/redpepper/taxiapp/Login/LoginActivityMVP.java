package com.redpepper.taxiapp.Login;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;

import io.reactivex.Single;

public interface LoginActivityMVP {

    interface Model{
        Single<SimpleResponse> getUserExistResponse(String phone);
    }

    interface View{
        String getPhone();

        void showUserNotExistsMessage();

        void showWrongPhoneMessage();

        void moveToPasswordValidationScreen();
    }

    interface Presenter{

        void setView(LoginActivityMVP.View view);

        void checkIfUserExists(String phone);


        void rxUnsubscribe();

    }

}
