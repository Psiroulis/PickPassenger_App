package com.redpepper.taxiapp.Http.PostModels;

public class DirectionPathPost {

    private String origin;
    private String destination;

    public DirectionPathPost(String origin, String destination) {
        this.origin = origin;
        this.destination = destination;
    }
}
