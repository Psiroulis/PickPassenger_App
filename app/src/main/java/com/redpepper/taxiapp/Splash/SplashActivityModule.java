package com.redpepper.taxiapp.Splash;

import android.content.SharedPreferences;

import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import dagger.Module;
import dagger.Provides;

@Module
public class SplashActivityModule {

    @Provides
    SplashActivityMVP.Presenter providePresenter(SplashActivityMVP.Model model){
        return new SplashActivityPresenter(model);
    }

    @Provides
    SplashActivityMVP.Model provideModel(SplashActivityRepository repository){
        return new SplashActivityModel(repository);
    }

    @Provides
    SplashActivityRepository provideRepository(SharedPreferences prefs,
                                               PassengerApiService service){

        return new SplashActivityRepositoryImpl(prefs, service);
    }

}
