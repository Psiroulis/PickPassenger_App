package com.redpepper.taxiapp.Root;

import android.app.Application;

import com.redpepper.taxiapp.End_ride.EndRideActivityModule;
import com.redpepper.taxiapp.Http.ApiModule;
import com.redpepper.taxiapp.Login.LoginActivityModule;
import com.redpepper.taxiapp.Main.MainActivityModule;
import com.redpepper.taxiapp.Payments.PaymentMethodFragmentModule;
import com.redpepper.taxiapp.Search_locations.SearchLocationsModule;
import com.redpepper.taxiapp.Signup.SignUpActivityModule;
import com.redpepper.taxiapp.Splash.SplashActivityModule;
import com.redpepper.taxiapp.Validate_password.ValidatePasswordActivityModule;

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .splashActivityModule(new SplashActivityModule())
                .loginActivityModule(new LoginActivityModule())
                .apiModule(new ApiModule())
                .mainActivityModule(new MainActivityModule())
                .signUpActivityModule(new SignUpActivityModule())
                .validatePasswordActivityModule(new ValidatePasswordActivityModule())
                .searchLocationsModule(new SearchLocationsModule())
                .paymentMethodFragmentModule(new PaymentMethodFragmentModule())
                .endRideActivityModule(new EndRideActivityModule())
                .build();

        component.inject(this);

    }

    public ApplicationComponent getComponent(){
        return component;
    }

}
