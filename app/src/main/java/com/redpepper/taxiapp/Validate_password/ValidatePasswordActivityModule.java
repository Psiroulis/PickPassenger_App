package com.redpepper.taxiapp.Validate_password;

import android.content.SharedPreferences;

import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import dagger.Module;
import dagger.Provides;

@Module
public class ValidatePasswordActivityModule {
    @Provides
    public ValidatePasswordActivityMVP.Presenter providePresenter(ValidatePasswordActivityMVP.Model model){
        return new ValidatePasswordActivityPresenter(model);
    }

    @Provides
    public ValidatePasswordActivityMVP.Model provideModel(ValidatePasswordActivityRepository repository){
        return new ValidatePasswordActivityModel(repository);
    }

    @Provides
    public ValidatePasswordActivityRepository provideRepository(SharedPreferences prefs, PassengerApiService service){
        return new ValidatePasswordActivityRepositoryImpl(prefs, service);
    }
}
