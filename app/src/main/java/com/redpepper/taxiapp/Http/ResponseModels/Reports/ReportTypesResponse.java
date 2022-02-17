package com.redpepper.taxiapp.Http.ResponseModels.Reports;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.redpepper.taxiapp.Http.ResponseModels.CustomClasses.ImportReportType;

import java.util.List;

public class ReportTypesResponse {
    @SerializedName("success ")
    @Expose
    private String success;
    @SerializedName("report_types")
    @Expose
    private List<ImportReportType> reportTypes = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ImportReportType> getReportTypes() {
        return reportTypes;
    }

    public void setReportTypes(List<ImportReportType> reportTypes) {
        this.reportTypes = reportTypes;
    }
}
