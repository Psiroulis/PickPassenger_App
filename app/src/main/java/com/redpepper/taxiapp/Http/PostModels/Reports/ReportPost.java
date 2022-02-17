package com.redpepper.taxiapp.Http.PostModels.Reports;

public class ReportPost {

    private int ride_id;
    private int report_type;
    private String description;

    public ReportPost(int ride_Id, int report_Type, String description) {
        this.ride_id = ride_Id;
        this.report_type = report_Type;
        this.description = description;
    }
}
