package com.redpepper.taxiapp.Payments;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeAddPaymentMethodResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeClientSecretResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeGetAllPaymentMethodsResponse;

import io.reactivex.Single;
import retrofit2.Response;

public interface Repository {
    Single<Response<StripeClientSecretResponse>> getClientSecret();
    Single<Response<StripeAddPaymentMethodResponse>> addPaymentMethod(String methodId);
    Single<Response<StripeGetAllPaymentMethodsResponse>> getPaymentMethods();
    Single<Response<SimpleResponse>> deletePaymentMethod(String methodId);
    Single<Response<SimpleResponse>> createStripeCustomer();
}
