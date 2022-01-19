package com.redpepper.taxiapp.Http.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllRecentPlacesResponse {

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("places")
    @Expose
    private List<RecentPlace> places;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<RecentPlace> getPlaces() {
        return places;
    }

    public void setPlaces(List<RecentPlace> places) {
        this.places = places;
    }
}
