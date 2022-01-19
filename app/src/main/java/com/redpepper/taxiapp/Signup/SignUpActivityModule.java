package com.redpepper.taxiapp.Signup;

import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import dagger.Module;
import dagger.Provides;

@Module
public class SignUpActivityModule {

    @Provides
    SignUpActivityMVP.Presenter providePresenter(SignUpActivityMVP.Model model){
        return new SignUpActivityPresenter(model);
    }

    @Provides
    SignUpActivityMVP.Model provideModel(SignUpActivityRepository repository){
        return new SignUpActivityModel(repository);
    }

    @Provides
    SignUpActivityRepository providesRepository(PassengerApiService service){
        return new SignUpActivityRepositoryImpl(service);
    }
}
