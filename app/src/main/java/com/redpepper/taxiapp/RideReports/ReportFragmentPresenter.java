package com.redpepper.taxiapp.RideReports;

import android.util.Log;

import com.redpepper.taxiapp.Http.PostModels.Reports.ReportPost;
import com.redpepper.taxiapp.RideReports.Models.ReportType;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ReportFragmentPresenter implements ReportsFragmentMVP.Presenter {

    ReportsFragmentMVP.Model model;
    ReportsFragmentMVP.View view;

    private CompositeDisposable subscription;

    public ReportFragmentPresenter(ReportsFragmentMVP.Model model) {
        this.model = model;

        subscription = new CompositeDisposable();
    }

    @Override
    public void setView(ReportsFragmentMVP.View view) {
        this.view = view;
    }

    @Override
    public void getAllReportTypes() {
        subscription.add(
                model.getReportTypes()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<ReportType>>(){
                    @Override
                    public void onSuccess(@NonNull List<ReportType> reportTypes) {
                        view.populateReportTypesList(reportTypes);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                })
        );
    }

    @Override
    public void createNewReport(ReportPost post) {
        subscription.add(
                model.createNewReport(post)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>(){
                    @Override
                    public void onSuccess(@NonNull String s) {

                        Log.d("blepo","Success from presenter:" + s);
                        if(s.equalsIgnoreCase("true")){

                            view.showSuccessfulReportCreationMessage();
                        }else{

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
