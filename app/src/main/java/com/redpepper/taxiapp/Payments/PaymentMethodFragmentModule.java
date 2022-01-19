package com.redpepper.taxiapp.Payments;

import android.content.SharedPreferences;

import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import dagger.Module;
import dagger.Provides;

@Module
public class PaymentMethodFragmentModule {

    @Provides
    public PaymentMethodFragmentMVP.Presenter providePresenter(PaymentMethodFragmentMVP.Model model,SharedPreferences prefs){
        return new PaymentMethodFragmentPresenter(model,prefs);
    }

    @Provides
    public PaymentMethodFragmentMVP.Model provideModel(PaymentMethodFragmentRepository repository){
        return new PaymentMethodFragmentModel(repository);
    }

    @Provides
    public PaymentMethodFragmentRepository providesRepository(SharedPreferences prefs, PassengerApiService service){
        return new PaymentMethodFragmentRepository(prefs, service);
    }
}
