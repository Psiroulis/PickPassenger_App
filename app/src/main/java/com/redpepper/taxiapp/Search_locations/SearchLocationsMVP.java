package com.redpepper.taxiapp.Search_locations;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.redpepper.taxiapp.Http.ResponseModels.FoursquarePlacesResponse;
import com.redpepper.taxiapp.Search_locations.Models.FoursquarePlace;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;

public interface SearchLocationsMVP {

    interface Model{

        Observable<AutocompletePrediction> getGooglePlacesPredictions(String query, LatLng passengerLocation);
        Observable<Place> getGooglePlace(String placeId);
        Single<Response<FoursquarePlacesResponse>> getFoursquarePlaces(LatLng pickupLocation);

    }

    interface View{

       void populateSearchListWithGoogleResults(ArrayList<AutocompletePrediction> predictionsList);
       void populateSearchListWithFoursquareResults(ArrayList<FoursquarePlace> foursquarePlacesList);
       void googlePlaceWasRetrieved(Place place);

    }

    interface Presenter{
        void setView(SearchLocationsMVP.View view);

        void getFavoriteLocations();

        void getHomeLocations();

        void getJobLocations();

        void performSearch(String query, LatLng passengerLocation);

        void getGooglePlace(String placeId);

        void performFoursquareSearch(LatLng pickupLocation);

        void performSearchInFavoriteLocations();

        void performSearchInHomeLocations();

        void performSearchInJobLocations();

        void addLocationToFavorites();

        void removeLocationFromFavorites();

        void addLocationToHome();

        void removeLocationFromHome();

        void addLocationToJob();

        void removeLocationFromJob();

        void rxUnsubscribe();
    }
}
