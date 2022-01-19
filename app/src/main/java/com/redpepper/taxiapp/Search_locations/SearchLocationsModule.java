package com.redpepper.taxiapp.Search_locations;

import android.content.Context;
import android.content.SharedPreferences;

import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchLocationsModule {

    @Provides
    public SearchLocationsMVP.Presenter providePresenter(SearchLocationsMVP.Model model){
        return new SearchLocationsPresenter(model);
    }

    @Provides
    public SearchLocationsMVP.Model provideModel(SearchLocationsRepository repository){
        return new SearchLocationsModel(repository);
    }

    @Provides
    public SearchLocationsRepository provideRepository(Context context,
                                                       SharedPreferences prefs,
                                                       PassengerApiService apiService){
        return new SearchLocationsRepository(context,prefs,apiService);
    }
}
