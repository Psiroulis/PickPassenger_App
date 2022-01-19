package com.redpepper.taxiapp.Http.PostModels;

public class FavoritePlacePost {
    private String name;
    private String address;
    private double latitude;
    private double longitude;

    public FavoritePlacePost(String name, String address, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
