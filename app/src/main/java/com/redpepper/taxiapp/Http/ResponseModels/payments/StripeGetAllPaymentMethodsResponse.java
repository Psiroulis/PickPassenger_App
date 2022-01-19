package com.redpepper.taxiapp.Http.ResponseModels.payments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StripeGetAllPaymentMethodsResponse {

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("paymentMethods")
    @Expose
    private List<StripePaymentMethod> paymentsMethods;

    @SerializedName("message")
    @Expose
    private String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<StripePaymentMethod> getPaymentsMethods() {
        return paymentsMethods;
    }

    public void setPaymentsMethods(List<StripePaymentMethod> paymentsMethods) {
        this.paymentsMethods = paymentsMethods;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
