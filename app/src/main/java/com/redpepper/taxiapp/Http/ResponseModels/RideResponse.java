package com.redpepper.taxiapp.Http.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RideResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("pickup_address")
    @Expose
    private String pickupAddress;
    @SerializedName("pickup_latitude")
    @Expose
    private Double pickupLatitude;
    @SerializedName("pickup_longitude")
    @Expose
    private Double pickupLongitude;
    @SerializedName("destination_address")
    @Expose
    private String destinationAddress;
    @SerializedName("destination_latitude")
    @Expose
    private Double destinationLatitude;
    @SerializedName("destination_longitude")
    @Expose
    private Double destinationLongitude;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("payment_amount")
    @Expose
    private Double paymentAmount;
    @SerializedName("assignment")
    @Expose
    private Object assignment;
    @SerializedName("notice")
    @Expose
    private Object notice;
    @SerializedName("boarding")
    @Expose
    private Object boarding;
    @SerializedName("debarkation")
    @Expose
    private Object debarkation;
    @SerializedName("user_cancel")
    @Expose
    private Object userCancel;
    @SerializedName("user_cancelation")
    @Expose
    private Object userCancelation;
    @SerializedName("driver_cancel")
    @Expose
    private Object driverCancel;
    @SerializedName("driver_cancelation")
    @Expose
    private Object driverCancelation;
    @SerializedName("driver_rating")
    @Expose
    private Object driverRating;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public Double getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(Double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public Double getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(Double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public Double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(Double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public Double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(Double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Object getAssignment() {
        return assignment;
    }

    public void setAssignment(Object assignment) {
        this.assignment = assignment;
    }

    public Object getNotice() {
        return notice;
    }

    public void setNotice(Object notice) {
        this.notice = notice;
    }

    public Object getBoarding() {
        return boarding;
    }

    public void setBoarding(Object boarding) {
        this.boarding = boarding;
    }

    public Object getDebarkation() {
        return debarkation;
    }

    public void setDebarkation(Object debarkation) {
        this.debarkation = debarkation;
    }

    public Object getUserCancel() {
        return userCancel;
    }

    public void setUserCancel(Object userCancel) {
        this.userCancel = userCancel;
    }

    public Object getUserCancelation() {
        return userCancelation;
    }

    public void setUserCancelation(Object userCancelation) {
        this.userCancelation = userCancelation;
    }

    public Object getDriverCancel() {
        return driverCancel;
    }

    public void setDriverCancel(Object driverCancel) {
        this.driverCancel = driverCancel;
    }

    public Object getDriverCancelation() {
        return driverCancelation;
    }

    public void setDriverCancelation(Object driverCancelation) {
        this.driverCancelation = driverCancelation;
    }

    public Object getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(Object driverRating) {
        this.driverRating = driverRating;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
