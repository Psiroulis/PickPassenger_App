package com.redpepper.taxiapp.Payments;

import android.content.SharedPreferences;

import com.redpepper.taxiapp.Http.PostModels.payments.PostPaymentMethodId;
import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeAddPaymentMethodResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeClientSecretResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeGetAllPaymentMethodsResponse;
import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import io.reactivex.Single;
import retrofit2.Response;

public class PaymentMethodFragmentRepository implements Repository {

    SharedPreferences prefs;
    PassengerApiService service;
    String token;

    public PaymentMethodFragmentRepository(SharedPreferences prefs, PassengerApiService service) {
        this.prefs = prefs;
        this.service = service;
        this.token = "Bearer " + prefs.getString("access_Token",null);
    }

    @Override
    public Single<Response<StripeClientSecretResponse>> getClientSecret() {
        return service.getStripeClientSecret(token);
    }

    @Override
    public Single<Response<StripeAddPaymentMethodResponse>> addPaymentMethod(String methodId) {
        return service.addNewPaymentMethod(token,new PostPaymentMethodId(methodId));
    }

    @Override
    public Single<Response<StripeGetAllPaymentMethodsResponse>> getPaymentMethods() {
        return service.getStripeClientAllPaymentMethods(token);
    }

    @Override
    public Single<Response<SimpleResponse>> deletePaymentMethod(String methodId) {
        return service.deleteStripePaymentMethod(token, new PostPaymentMethodId(methodId));
    }

    @Override
    public Single<Response<SimpleResponse>> createStripeCustomer() {
        return service.createStripeCustomer(token);
    }
}
