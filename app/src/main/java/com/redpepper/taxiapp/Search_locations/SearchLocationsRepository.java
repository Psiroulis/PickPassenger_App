package com.redpepper.taxiapp.Search_locations;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.redpepper.taxiapp.R;
import com.redpepper.taxiapp.Http.PostModels.FoursquareApiPost;
import com.redpepper.taxiapp.Http.ResponseModels.FoursquarePlacesResponse;
import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;

public class SearchLocationsRepository implements Repository{

    PassengerApiService apiService;
    SharedPreferences prefs;
    Context context;
    PlacesClient placesClient;
    String token;
    AutocompleteSessionToken sessionToken;

    public SearchLocationsRepository(Context context, SharedPreferences prefs, PassengerApiService apiService) {
        this.apiService = apiService;
        this.prefs = prefs;
        this.context = context;
        this.placesClient= Places.createClient(context);
        this.token = "Bearer " + prefs.getString("access_Token",null);
        this.sessionToken = null;
    }

    @Override
    public Observable<AutocompletePrediction> getGooglePlacePredictions(String query, LatLng passengerLocation) {

        return Observable.create(emitter -> {

            Places.initialize(context, context.getString(R.string.google_maps_key));

            if(sessionToken == null){
                sessionToken = AutocompleteSessionToken.newInstance();
                Log.d("blepo", "New Autocomplete Session Token is created");
            }

            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                    .setSessionToken(sessionToken)
                    .setOrigin(passengerLocation)
                    .setLocationRestriction(RectangularBounds.newInstance(
                            new LatLng(37.628304, 23.491091),
                            new LatLng(38.304958, 24.102206)
                    ))
                    .setCountries("GR")
                    .setQuery(query)
                    .build();

            placesClient.findAutocompletePredictions(request)
                    .addOnSuccessListener(response->{
                       for(AutocompletePrediction prediction : response.getAutocompletePredictions()){
                           emitter.onNext(prediction);
                       }
                    })
                    .addOnFailureListener(exception->{
                        emitter.onError(exception);
                    })
                    .addOnCompleteListener(command -> {
                        emitter.onComplete();
                    });

        });

    }

    @Override
    public Observable<Place> getGooglePlaceById(String placeId) {

        return Observable.create(emitter -> {

            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.TYPES, Place.Field.ID,
                    Place.Field.ADDRESS, Place.Field.LAT_LNG);

            FetchPlaceRequest request = FetchPlaceRequest.builder(placeId,placeFields).setSessionToken(sessionToken).build();

            placesClient.fetchPlace(request)
                    .addOnSuccessListener(response->{
                         emitter.onNext(response.getPlace());
                    })
                    .addOnFailureListener(exception->{
                        emitter.onError(exception);
                    })
                    .addOnCompleteListener(command -> {
                        sessionToken = null;
                        Log.d("blepo","New Autocomplete Session Token is completed here");
                        emitter.onComplete();
                    });
        });

    }

    @Override
    public Single<Response<FoursquarePlacesResponse>> getFoursquareResponse(LatLng pickupLocation) {
        return apiService.getFoursquareNearPlaces(token,new FoursquareApiPost(pickupLocation.latitude + "," + pickupLocation.longitude));
    }
}
