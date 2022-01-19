package com.redpepper.taxiapp.Http.PostModels;

public class ValidatePasswordPost {

    private String phone;
    private String password;

    public ValidatePasswordPost(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }
}
