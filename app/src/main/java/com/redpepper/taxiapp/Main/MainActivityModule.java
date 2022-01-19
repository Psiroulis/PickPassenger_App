package com.redpepper.taxiapp.Main;

import android.content.Context;
import android.content.SharedPreferences;

import com.patloew.rxlocation.RxLocation;
import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {

    @Provides
    public MainActivityMVP.Presenter providePresenter(MainActivityMVP.Model model, Context context){
        return new MainActivityPresenter(model,context);
    }

    @Provides
    public MainActivityMVP.Model provideModel(MainActivityRepository repository){
        return new MainActivityModel(repository);
    }

    @Provides
    public MainActivityRepository provideRepository(SharedPreferences prefs,
                                                    PassengerApiService service,
                                                    Context context,
                                                    RxLocation rxLocation){
        return new MainActivityRepository(prefs,service,context,rxLocation);
    }
}
