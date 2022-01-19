package com.redpepper.taxiapp.Splash;

import android.os.Handler;

import androidx.annotation.Nullable;

import io.reactivex.disposables.CompositeDisposable;

public class SplashActivityPresenter implements SplashActivityMVP.Presenter {

    @Nullable
    private SplashActivityMVP.View view;
    private SplashActivityMVP.Model model;

    private CompositeDisposable subscription;

    public SplashActivityPresenter(SplashActivityMVP.Model model) {
        this.model = model;

        subscription = new CompositeDisposable();
    }

    @Override
    public void setView(SplashActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void getLogedInUser() {

        Handler handler = new Handler();

        handler.postDelayed(() -> {

            if(model.isUserLoggedIn()){

                view.moveToMainScreen();

            }else{

                view.moveToLoginScreen();

            }

        },4 * 1000);

    }

    @Override
    public void rxUnsubscribe() {
        if (subscription != null) {
            if (!subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }
}