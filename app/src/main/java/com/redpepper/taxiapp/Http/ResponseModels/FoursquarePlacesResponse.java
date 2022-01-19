package com.redpepper.taxiapp.Http.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.redpepper.taxiapp.Http.ResponseModels.CustomClasses.FoursquareVenues;

import java.util.List;

public class FoursquarePlacesResponse {
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("foursquareVenues")
    @Expose
    private List<FoursquareVenues> venues = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<FoursquareVenues> getVenues() {
        return venues;
    }

    public void setVenues(List<FoursquareVenues> venues) {
        this.venues = venues;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
