package com.redpepper.taxiapp.Splash;

public class SplashActivityModel implements SplashActivityMVP.Model {

    private SplashActivityRepository repository;

    public SplashActivityModel(SplashActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isUserLoggedIn() {
        return repository.checkUserLogin();
    }


}
