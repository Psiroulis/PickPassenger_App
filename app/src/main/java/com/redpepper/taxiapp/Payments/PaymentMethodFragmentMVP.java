package com.redpepper.taxiapp.Payments;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeAddPaymentMethodResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeClientSecretResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeGetAllPaymentMethodsResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripePaymentMethod;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;

public interface PaymentMethodFragmentMVP {

    interface Model{
        Single<Response<StripeClientSecretResponse>> getSecret();
        Single<Response<StripeAddPaymentMethodResponse>> addNewPaymentMethod(String methodId);
        Single<Response<StripeGetAllPaymentMethodsResponse>> getPaymentMethods();
        Single<Response<SimpleResponse>> deletePaymentMethod(String methodId);
        Single<Response<SimpleResponse>> createStripeCustomer();
    }
    interface View{
        void fillSavedPaymentMethodsList(List<StripePaymentMethod> paymentMethodsList);
        void setStripeClientSecretString(String stripeClientSecretString);
        void addNewCardToStripeCustomer();
        void completeAddNewCardProcedure();
    }
    interface Presenter{

        void setView(View view);
        void rxUnsubscribe();

        void createStripeCustomer();
        void getClientSecret();
        void getUsersPaymentMethods();
        void saveUserNewPaymentMethod(String paymentMethodId);
        void deleteUserPaymentMethod(String paymentMethodId);
    }
}
