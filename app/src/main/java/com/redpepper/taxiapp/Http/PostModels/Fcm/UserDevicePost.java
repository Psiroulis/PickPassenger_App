package com.redpepper.taxiapp.Http.PostModels.Fcm;

public class UserDevicePost {

    private String frb_id;
    private String software;
    private String fcm_token;

    public UserDevicePost(String frb_id, String fcm_token) {
        this.frb_id = frb_id;
        this.fcm_token = fcm_token;
        this.software = "android";
    }
}
