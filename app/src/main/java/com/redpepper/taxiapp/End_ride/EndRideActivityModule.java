package com.redpepper.taxiapp.End_ride;

import android.content.SharedPreferences;

import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import dagger.Module;
import dagger.Provides;

@Module
public class EndRideActivityModule {

    @Provides
    public EndRideActivityMVP.Presenter providePresenter(EndRideActivityMVP.Model model){
        return new EndRideActivityPresenter(model);
    }

    @Provides
    public EndRideActivityMVP.Model provideModel(Repository repository){
        return new EndRideActivityModel(repository);
    }

    @Provides
    public Repository provideRepository(PassengerApiService apiService, SharedPreferences prefs){
        return new EndRideActivityRepository(apiService,prefs);
    }

}
