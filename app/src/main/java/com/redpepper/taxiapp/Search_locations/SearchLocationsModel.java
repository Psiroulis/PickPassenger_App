package com.redpepper.taxiapp.Search_locations;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.redpepper.taxiapp.Http.ResponseModels.FoursquarePlacesResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;

public class SearchLocationsModel implements SearchLocationsMVP.Model {

    SearchLocationsRepository repository;

    public SearchLocationsModel(SearchLocationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<AutocompletePrediction> getGooglePlacesPredictions(String query, LatLng passengerLocation) {
        return this.repository.getGooglePlacePredictions(query,passengerLocation);
    }

    @Override
    public Observable<Place> getGooglePlace(String placeId) {
        return repository.getGooglePlaceById(placeId);
    }

    @Override
    public Single<Response<FoursquarePlacesResponse>> getFoursquarePlaces(LatLng pickupLocation) {
        return repository.getFoursquareResponse(pickupLocation);
    }
}
