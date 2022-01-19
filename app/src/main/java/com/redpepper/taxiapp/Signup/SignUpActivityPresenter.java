package com.redpepper.taxiapp.Signup;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SignUpActivityPresenter implements SignUpActivityMVP.Presenter {

    private SignUpActivityMVP.Model model;
    private SignUpActivityMVP.View view;
    private CompositeDisposable subscription;

    public SignUpActivityPresenter(SignUpActivityMVP.Model model) {

        this.model = model;

        this.subscription = new CompositeDisposable();
    }

    @Override
    public void setView(SignUpActivityMVP.View view) {

        this.view = view;

    }

    @Override
    public void registerUser(String name, String surname, String phoneNumber) {

        subscription.add(
                model.getSignUpResponse(name,surname,phoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SimpleResponse>(){
                    @Override
                    public void onSuccess(@NonNull SimpleResponse response) {

                        if(response.getSuccess().equalsIgnoreCase("true")){

                            view.proceedToValidatePasswordActivity();

                        }else{

                            view.showCreateUserErrorMessage(response.getMessage());

                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
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

