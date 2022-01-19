package com.redpepper.taxiapp.Http.PostModels;

public class SignUpPost {
    private String name;
    private String surname;
    private String phone;


    public SignUpPost(String name, String surname, String phone) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;

    }
}
