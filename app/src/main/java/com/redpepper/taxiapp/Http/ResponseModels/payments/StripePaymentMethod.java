package com.redpepper.taxiapp.Http.ResponseModels.payments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StripePaymentMethod {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("brand")
    @Expose
    private String brand;

    @SerializedName("last4")
    @Expose
    private String last4;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }
}
