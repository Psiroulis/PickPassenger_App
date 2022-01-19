package com.redpepper.taxiapp.Search_locations;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.redpepper.taxiapp.Http.ResponseModels.FoursquarePlacesResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;

public interface Repository {

    Observable<AutocompletePrediction> getGooglePlacePredictions(String query, LatLng passengerLocation);

    Observable<Place> getGooglePlaceById(String placeId);

    Single<Response<FoursquarePlacesResponse>> getFoursquareResponse(LatLng pickupLocation);
}
