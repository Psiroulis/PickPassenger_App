package com.redpepper.taxiapp.Root;

import android.app.Application;

import com.redpepper.taxiapp.End_ride.EndRideActivity;
import com.redpepper.taxiapp.End_ride.EndRideActivityModule;
import com.redpepper.taxiapp.Http.ApiModule;
import com.redpepper.taxiapp.Login.LoginActivity;
import com.redpepper.taxiapp.Login.LoginActivityModule;
import com.redpepper.taxiapp.Main.MainActivity;
import com.redpepper.taxiapp.Main.MainActivityModule;
import com.redpepper.taxiapp.Payments.PaymentMethodFragment;
import com.redpepper.taxiapp.Payments.PaymentMethodFragmentModule;
import com.redpepper.taxiapp.Search_locations.SearchLocationFragment;
import com.redpepper.taxiapp.Search_locations.SearchLocationsModule;
import com.redpepper.taxiapp.Signup.SignUpActivity;
import com.redpepper.taxiapp.Signup.SignUpActivityModule;
import com.redpepper.taxiapp.Splash.SplashActivity;
import com.redpepper.taxiapp.Splash.SplashActivityModule;
import com.redpepper.taxiapp.Validate_password.ValidatePasswordActivity;
import com.redpepper.taxiapp.Validate_password.ValidatePasswordActivityModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class,
        SplashActivityModule.class,
        LoginActivityModule.class,
        ApiModule.class,
        MainActivityModule.class,
        SignUpActivityModule.class,
        ValidatePasswordActivityModule.class,
        SearchLocationsModule.class,
        PaymentMethodFragmentModule.class,
        EndRideActivityModule.class
})

public interface ApplicationComponent {

    void inject (Application application);

    void inject (SplashActivity target);

    void inject (LoginActivity target);

    void inject (MainActivity target);

    void inject (SignUpActivity target);

    void inject (ValidatePasswordActivity target);

    void inject (SearchLocationFragment target);

    void inject (PaymentMethodFragment target);

    void inject (EndRideActivity target);
}
