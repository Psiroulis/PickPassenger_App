package com.redpepper.taxiapp.Http.PostModels;

public class TokenPost {

    private String grant_type;
    private String client_id;
    private String client_secret;
    private String username;
    private String password;
    private String scope;

    //Digital Ocean

    public TokenPost(String phone, String password){
        this.grant_type = "password";
        this.client_id = "2";
        this.client_secret = "7NzcTwfDHVvrReWOAJPLCNNaYYuzyXkguSAAmvTQ";
        this.username = phone;
        this.password = password;
        this.scope = "";
    }

    //mac
//    public TokenPost(String phone, String password) {
//        this.grant_type = "password";
//        this.client_id = "2";
//        this.client_secret = "Vxrqip5RI9W8UrVOyp5oee9iPrkfzL361Hysiv8w";
//        this.username = phone;
//        this.password = password;
//        this.scope = "";
//    }

//    //laptop
//        public TokenPost(String phone, String password) {
//        this.grant_type = "password";
//        this.client_id = "2";
//        this.client_secret = "Z8xK7HVSveOkWGnfnVcYvCUEgCluJBSKMmF8ljcL";
//        this.username = phone;
//        this.password = password;
//        this.scope = "";
//    }


}
