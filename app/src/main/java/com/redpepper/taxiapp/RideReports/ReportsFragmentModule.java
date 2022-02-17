package com.redpepper.taxiapp.RideReports;

import android.content.SharedPreferences;

import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import dagger.Module;
import dagger.Provides;

@Module
public class ReportsFragmentModule {

    @Provides
    public ReportsFragmentMVP.Presenter providePresenter(ReportsFragmentMVP.Model model){
        return new ReportFragmentPresenter(model);
    }

    @Provides
    public ReportsFragmentMVP.Model provideModel(Repository repository){
        return new ReportsFragmentModel(repository);
    }

    @Provides
    public Repository provideRepository(SharedPreferences prefs, PassengerApiService service){
        return new NetworkRepository(prefs,service);
    }

}
