package com.redpepper.taxiapp.Http.PostModels;

public class RidePost {

    private String pickup_address;
    private double pickup_latitude;
    private double pickup_longitude;
    private String destination_address;
    private double destination_latitude;
    private double destination_longitude;
    private String payment_type;

    public RidePost(String pickup_address, double pickup_latitude, double pickup_longitude,
                    String destination_address, double destination_latitude,
                    double destination_longitude, String payment_type) {
        this.pickup_address = pickup_address;
        this.pickup_latitude = pickup_latitude;
        this.pickup_longitude = pickup_longitude;
        this.destination_address = destination_address;
        this.destination_latitude = destination_latitude;
        this.destination_longitude = destination_longitude;
        this.payment_type = payment_type;
    }
}
