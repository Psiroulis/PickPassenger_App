package com.redpepper.taxiapp.Http.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllPlacesResponse {

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("places")
    @Expose
    private List<Place> places;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
