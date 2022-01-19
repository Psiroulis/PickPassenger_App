package com.redpepper.taxiapp.Http.PostModels;

public class RecentPlacePost {
    private String address;
    private double latitude;
    private double longitude;

    public RecentPlacePost( String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
