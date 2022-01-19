package com.redpepper.taxiapp.Payments;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeAddPaymentMethodResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeClientSecretResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeGetAllPaymentMethodsResponse;

import io.reactivex.Single;
import retrofit2.Response;

public class PaymentMethodFragmentModel implements PaymentMethodFragmentMVP.Model {

    PaymentMethodFragmentRepository repository;

    public PaymentMethodFragmentModel(PaymentMethodFragmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<Response<StripeClientSecretResponse>> getSecret() {
        return repository.getClientSecret();
    }

    @Override
    public Single<Response<StripeAddPaymentMethodResponse>> addNewPaymentMethod(String methodId) {
        return repository.addPaymentMethod(methodId);
    }

    @Override
    public Single<Response<StripeGetAllPaymentMethodsResponse>> getPaymentMethods() {
        return repository.getPaymentMethods();
    }

    @Override
    public Single<Response<SimpleResponse>> deletePaymentMethod(String methodId) {
        return repository.deletePaymentMethod(methodId);
    }

    @Override
    public Single<Response<SimpleResponse>> createStripeCustomer() {
        return repository.createStripeCustomer();
    }
}
