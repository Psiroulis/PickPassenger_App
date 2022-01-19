package com.redpepper.taxiapp.Root;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.patloew.rxlocation.RxLocation;
import com.redpepper.taxiapp.Utils.NetworkUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(){
        return application.getSharedPreferences("app",Context.MODE_PRIVATE);
    }

    @Provides
    RxLocation provideRxLocation(Context context){
        return new RxLocation(context);
    }

    @Provides
    NetworkUtil provideNetworkUtil (Context context){
        return new NetworkUtil(context);
    }

}
