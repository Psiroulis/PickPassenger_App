package com.redpepper.taxiapp.Http.PostModels;

public class AcceptedDriverArrivalTimePost {

    private String origins;
    private String destinations;

    public AcceptedDriverArrivalTimePost(String origins, String destinations) {
        this.origins = origins;
        this.destinations = destinations;
    }
}
