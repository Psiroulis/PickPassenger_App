package com.redpepper.taxiapp.Validate_password;

import android.util.Log;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
import com.redpepper.taxiapp.Http.ResponseModels.TokenResponse;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ValidatePasswordActivityPresenter implements ValidatePasswordActivityMVP.Presenter {

    private ValidatePasswordActivityMVP.View view;
    private ValidatePasswordActivityMVP.Model model;
    private CompositeDisposable subscription;

    public ValidatePasswordActivityPresenter(ValidatePasswordActivityMVP.Model model) {
        this.model = model;
        subscription = new CompositeDisposable();
    }

    @Override
    public void setView(ValidatePasswordActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void loginUser(String phone, String password) {

        subscription.add(
                    model.getLoginResponse(phone, password)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSingleObserver<SimpleResponse>(){
                                @Override
                                public void onSuccess(SimpleResponse validatePasswordResponse) {

                                    if(validatePasswordResponse.getSuccess().equalsIgnoreCase("true")){
                                        setLoginState();
                                        getUserToken(phone,password);
                                    }else{
                                        view.showWrongCredentialsMessage();
                                    }

                                }

                                @Override
                                public void onError(Throwable e) {
                                    view.showWrongCredentialsMessage();

                                }
                            })
            );



    }

    @Override
    public void resentSms(String phone) {

        subscription.add(
                model.getResentPasswordResponse(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SimpleResponse>(){
                    @Override
                    public void onSuccess(SimpleResponse resentPasswordResponse) {

                        if(resentPasswordResponse.getSuccess().equalsIgnoreCase("true")){
                            view.showResentSmsSuccessMessage();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                })
        );

    }

    @Override
    public void getUserToken(String phone, String password) {
        subscription.add(
          model.getTokenResponse(phone,password)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeWith(new DisposableSingleObserver<TokenResponse>(){
              @Override
              public void onSuccess(TokenResponse tokenResponse) {
                  model.storeUserToken(tokenResponse.getAccessToken());
                  model.storeRefreshToken(tokenResponse.getRefreshToken());
                  model.storeExrirationTime(tokenResponse.getExpiresIn());
                  view.proceedToMainActivityWithLoggedInUser();

                  Log.d("blepo", "user_token: "+tokenResponse.getAccessToken()+"refersh_token: "+tokenResponse.getRefreshToken()
                          +"expire in: " + tokenResponse.getExpiresIn());
              }

              @Override
              public void onError(Throwable e) {

              }
          })
        );
    }

    @Override
    public void setLoginState() {
        model.storeLoginState();
    }

    @Override
    public void rxUnsubscribe() {
        if(subscription != null){
            if(!subscription.isDisposed()){
                subscription.dispose();
            }
        }
    }
}
