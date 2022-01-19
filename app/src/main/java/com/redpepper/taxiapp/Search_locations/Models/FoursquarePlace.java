package com.redpepper.taxiapp.Search_locations.Models;

public class FoursquarePlace {

    private double latitude;
    private double longitude;
    private String name;
    private String address;
    private String distance;
    private String distanceMetrics;

    public FoursquarePlace(double latitude, double longitude, String name, String address, String distance, String distanceMetrics) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;

        this.address = address;
        this.distance = distance;
        this.distanceMetrics = distanceMetrics;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longitude;
    }

    public void setLongtitude(double longtitude) {
        this.longitude = longtitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistanceMetrics() {
        return distanceMetrics;
    }

    public void setDistanceMetrics(String distanceMetrics) {
        this.distanceMetrics = distanceMetrics;
    }
}
