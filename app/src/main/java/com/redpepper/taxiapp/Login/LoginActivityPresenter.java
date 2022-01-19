package com.redpepper.taxiapp.Login;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

class LoginActivityPresenter implements LoginActivityMVP.Presenter {

    private LoginActivityMVP.Model model;
    private CompositeDisposable subscription = null;
    private LoginActivityMVP.View view;

    public LoginActivityPresenter(LoginActivityMVP.Model model) {

        this.model = model;

        subscription = new CompositeDisposable();
    }

    @Override
    public void setView(LoginActivityMVP.View view) { this.view = view; }

    @Override
    public void checkIfUserExists(String phone) {

        subscription.add(
          model.getUserExistResponse(phone)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribeWith(new DisposableSingleObserver<SimpleResponse>(){
                      @Override
                      public void onSuccess(SimpleResponse response) {

                          if(response.getSuccess().equalsIgnoreCase("true")){

                              view.moveToPasswordValidationScreen();

                          }

                          if(response.getSuccess().equalsIgnoreCase("false")){

                              view.showUserNotExistsMessage();

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
    public void rxUnsubscribe() {
        if(subscription != null){
            if(!subscription.isDisposed()){
                subscription.dispose();
            }
        }
    }
}
