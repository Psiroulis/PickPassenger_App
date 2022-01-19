package com.redpepper.taxiapp.Http.ResponseModels.payments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StripeClientSecretResponse {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("client_secret")
    @Expose
    private String client_secret;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
}
