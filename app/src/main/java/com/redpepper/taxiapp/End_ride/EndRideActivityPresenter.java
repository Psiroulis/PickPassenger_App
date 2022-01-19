package com.redpepper.taxiapp.End_ride;

import com.redpepper.taxiapp.Http.ResponseModels.RideResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class EndRideActivityPresenter implements EndRideActivityMVP.Presenter {

    EndRideActivityMVP.Model model;
    EndRideActivityMVP.View view;

    private CompositeDisposable subscription;

    public EndRideActivityPresenter(EndRideActivityMVP.Model model) {

        this.model = model;

        this.subscription = new CompositeDisposable();

    }

    @Override
    public void setView(EndRideActivityMVP.View view) {
        this.view = view;
    }

    @Override
    public void getRideInfo(int rideId) {

        subscription.add(
                model.getRideInfo(rideId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(new DisposableSingleObserver<Response<RideResponse>>(){
            @Override
            public void onSuccess(@NonNull Response<RideResponse> rideResponse) {

                RideResponse rideInfo= rideResponse.body();

                view.setRideInfo(rideInfo);

            }

            @Override
            public void onError(@NonNull Throwable e) {

                e.printStackTrace();
            }
        }));
    }

    @Override
    public void rxUnsubscribe() {
        if(subscription!=null){
            if(!subscription.isDisposed()){
                subscription.dispose();
            }
        }
    }
}
