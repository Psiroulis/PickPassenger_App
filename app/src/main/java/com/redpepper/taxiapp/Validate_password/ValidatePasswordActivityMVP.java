package com.redpepper.taxiapp.Validate_password;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
import com.redpepper.taxiapp.Http.ResponseModels.TokenResponse;

import io.reactivex.Single;

public interface ValidatePasswordActivityMVP {

    interface Model{
        Single<SimpleResponse> getLoginResponse(String phone, String password);

        Single<SimpleResponse> getResentPasswordResponse(String phone);

        Single<TokenResponse> getTokenResponse(String phone, String password);

        void storeLoginState();

        void storeUserToken(String token);

        void storeRefreshToken(String refreshToken);

        void storeExrirationTime(int expirationTime);


    }

    interface View{

        void showWrongCredentialsMessage();

        void proceedToMainActivityWithLoggedInUser();

        void showResentSmsSuccessMessage();

        void showServerErrorMessage();
    }

    interface Presenter{
        void setView(ValidatePasswordActivityMVP.View view);

        void loginUser(String phone, String password);

        void getUserToken(String phone, String password);

        void resentSms(String phone);

        void setLoginState();

        void rxUnsubscribe();
    }
}
