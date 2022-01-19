package com.redpepper.taxiapp.Payments;

import android.content.SharedPreferences;
import android.util.Log;

import com.redpepper.taxiapp.Http.ResponseModels.SimpleResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeAddPaymentMethodResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeClientSecretResponse;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripeGetAllPaymentMethodsResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class PaymentMethodFragmentPresenter implements PaymentMethodFragmentMVP.Presenter {

    PaymentMethodFragmentMVP.Model model;
    SharedPreferences prefs;
    PaymentMethodFragmentMVP.View view;

    private CompositeDisposable subscription;

    public PaymentMethodFragmentPresenter(PaymentMethodFragmentMVP.Model model,SharedPreferences prefs) {
        this.model = model;
        this.prefs = prefs;
        this.subscription = new CompositeDisposable();
    }

    @Override
    public void setView(PaymentMethodFragmentMVP.View view) {
        this.view = view;
    }

    @Override
    public void createStripeCustomer() {
        subscription.add(
                model.createStripeCustomer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<SimpleResponse>>(){
                    @Override
                    public void onSuccess(@NonNull Response<SimpleResponse> response) {
                        if(response.body().getSuccess().equalsIgnoreCase("true")){

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("isStripeCustomerCreated",true);
                            editor.apply();

                            view.addNewCardToStripeCustomer();

                        }else{
                            //Todo: handle stripe customer creation error
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                })
        );
    }

    @Override
    public void getClientSecret() {
        subscription.add(
                model.getSecret()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Response<StripeClientSecretResponse>>(){
                            @Override
                            public void onSuccess(Response<StripeClientSecretResponse> response) {
                                if(response.body().getSuccess().equalsIgnoreCase("true")){

                                    view.setStripeClientSecretString(response.body().getClient_secret());
                                    Log.d("blepo", "Stripe Secret:" + response.body().getClient_secret());

                                }else{

                                    Log.e("blepo", "Error message:" + response.body());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        })
        );
    }

    @Override
    public void getUsersPaymentMethods() {
        subscription.add(
                model.getPaymentMethods()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Response<StripeGetAllPaymentMethodsResponse>>(){
                            @Override
                            public void onSuccess(@NonNull Response<StripeGetAllPaymentMethodsResponse> response) {
                                if(response.body().getSuccess().equalsIgnoreCase("true")){

                                    view.fillSavedPaymentMethodsList(response.body().getPaymentsMethods());

                                }else{

                                    //view.showEmptyPaymentMethodsListMessage();
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        })
        );
    }

    @Override
    public void saveUserNewPaymentMethod(String paymentMethodId) {
        subscription.add(
                model.addNewPaymentMethod(paymentMethodId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Response<StripeAddPaymentMethodResponse>>(){
                            @Override
                            public void onSuccess(Response<StripeAddPaymentMethodResponse> response) {
                                if(response.body().getSuccess().equalsIgnoreCase("true")){

                                    view.completeAddNewCardProcedure();

                                }else{

                                    Log.e("blepo", "Error message:" + response.body());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        })
        );
    }

    @Override
    public void deleteUserPaymentMethod(String paymentMethodId) {
        subscription.add(
                model.deletePaymentMethod(paymentMethodId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Response<SimpleResponse>>(){
                            @Override
                            public void onSuccess(@NonNull Response<SimpleResponse> response) {
                                if(response.body().getSuccess().equalsIgnoreCase("true")){

                                    getUsersPaymentMethods();

                                    Log.d("blepo", "Delete Completed");

                                }else{

                                    Log.e("blepo", "Error message:" + response.body());
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {

                            }
                        })
        );
    }

    @Override
    public void rxUnsubscribe() {
        if(subscription != null){
            if(!subscription.isDisposed()){
                subscription.dispose();
            }
        }
    }
}
