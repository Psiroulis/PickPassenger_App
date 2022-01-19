package com.redpepper.taxiapp.Search_locations;

import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.redpepper.taxiapp.Http.ResponseModels.CustomClasses.FoursquareVenues;
import com.redpepper.taxiapp.Http.ResponseModels.FoursquarePlacesResponse;
import com.redpepper.taxiapp.Search_locations.Models.FoursquarePlace;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SearchLocationsPresenter implements SearchLocationsMVP.Presenter {

    SearchLocationsMVP.Model model;
    SearchLocationsMVP.View view;
    CompositeDisposable subscription;
    private Place selectedPlace = null;

    private ArrayList<AutocompletePrediction> predictions;

    private ArrayList<FoursquarePlace> foursquarePlaces;

    public SearchLocationsPresenter(SearchLocationsMVP.Model model) {
        this.model = model;
        this.subscription = new CompositeDisposable();
        this.predictions = new ArrayList<>();
        this.foursquarePlaces = new ArrayList<>();
    }

    @Override
    public void setView(SearchLocationsMVP.View view) {
        this.view = view;
    }

    @Override
    public void getFavoriteLocations() {

    }

    @Override
    public void getHomeLocations() {

    }

    @Override
    public void getJobLocations() {

    }

    @Override
    public void performSearch(String query, LatLng passengerLocation) {

        predictions.clear();

        subscription.add(
                model.getGooglePlacesPredictions(query,passengerLocation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<AutocompletePrediction>(){
                    @Override
                    public void onNext(@NonNull AutocompletePrediction autocompletePrediction) {
                        predictions.add(autocompletePrediction);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(e instanceof ApiException){
                            ApiException apiException = (ApiException) e;
                            Log.e("blepo","Place not found:" + apiException.getStatusCode());
                        }
                    }

                    @Override
                    public void onComplete() {
                        view.populateSearchListWithGoogleResults(predictions);
                    }
                })
        );


    }

    @Override
    public void getGooglePlace(String placeId) {

        subscription.add(
                model.getGooglePlace(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                 .subscribeWith(new DisposableObserver<Place>(){
                     @Override
                     public void onNext(@NonNull Place place) {
                         selectedPlace = place;
                     }

                     @Override
                     public void onError(@NonNull Throwable e) {

                     }

                     @Override
                     public void onComplete() {
                         view.googlePlaceWasRetrieved(selectedPlace);
                     }
                 })
        );
    }

    @Override
    public void performFoursquareSearch(LatLng pickupLocation) {

        foursquarePlaces.clear();

        subscription.add(
                model.getFoursquarePlaces(pickupLocation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Response<FoursquarePlacesResponse>>(){
                    @Override
                    public void onSuccess(@NonNull Response<FoursquarePlacesResponse> response) {
                        if(response.body().getSuccess().equalsIgnoreCase("true")){

                            List<FoursquareVenues> venues = response.body().getVenues();

                            for(FoursquareVenues venue : venues){

                                Integer distance = venue.getDistance();
                                String distanceToShow = "";
                                String metrics = "";

                                if ((Double.valueOf(distance) / 1000) > 1) {

                                    distanceToShow = String.format("%.2f", Double.valueOf(distance) / 1000);

                                    metrics = "km";


                                } else {

                                    distanceToShow = String.valueOf(distance);

                                    metrics = "m";
                                }

                                List pp = venue.getAddress();

                                StringBuilder builder = new StringBuilder();

                                String seperator = "";
                                for (int g = 0; g < pp.size(); g++) {
                                    builder.append(seperator);
                                    builder.append(pp.get(g));
                                    seperator = ", ";
                                }

                                FoursquarePlace place = new FoursquarePlace(

                                        venue.getLatitude(),
                                        venue.getLongitude(),
                                        venue.getName(),
                                        builder.toString(),
                                        distanceToShow,
                                        metrics

                                );

                                foursquarePlaces.add(place);
                            }

                            view.populateSearchListWithFoursquareResults(foursquarePlaces);


                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                })
        );
    }

    @Override
    public void performSearchInFavoriteLocations() {

    }

    @Override
    public void performSearchInHomeLocations() {

    }

    @Override
    public void performSearchInJobLocations() {

    }

    @Override
    public void addLocationToFavorites() {

    }

    @Override
    public void removeLocationFromFavorites() {

    }

    @Override
    public void addLocationToHome() {

    }

    @Override
    public void removeLocationFromHome() {

    }

    @Override
    public void addLocationToJob() {

    }

    @Override
    public void removeLocationFromJob() {

    }

    @Override
    public void rxUnsubscribe() {
        if (subscription != null) {
            if (!subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }
}
