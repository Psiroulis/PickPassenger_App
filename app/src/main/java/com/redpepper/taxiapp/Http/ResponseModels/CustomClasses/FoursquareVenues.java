package com.redpepper.taxiapp.Http.ResponseModels.CustomClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoursquareVenues {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("distance")
    @Expose
    private Integer distance;
    @SerializedName("address")
    @Expose
    private List<String> address = null;
    @SerializedName("lat")
    @Expose
    private Double latitude;
    @SerializedName("lng")
    @Expose
    private Double longitude;
    @SerializedName("image")
    @Expose
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
