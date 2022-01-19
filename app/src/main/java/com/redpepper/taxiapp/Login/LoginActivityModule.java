package com.redpepper.taxiapp.Login;

import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginActivityModule {

    @Provides
    LoginActivityMVP.Presenter providePresenter(LoginActivityMVP.Model model){
        return new LoginActivityPresenter(model);
    }

    @Provides
    LoginActivityMVP.Model provideModel(LoginActivityRepository repository){
        return new LoginActivityModel(repository);
    }

    @Provides
    LoginActivityRepository provideRepository(PassengerApiService service){
        return new LoginActivityRepositoryImpl(service);
    }
}
