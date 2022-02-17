package com.redpepper.taxiapp.RideReports;


import com.redpepper.taxiapp.Http.PostModels.Reports.ReportPost;
import com.redpepper.taxiapp.Http.ResponseModels.Reports.ReportResponse;
import com.redpepper.taxiapp.Http.ResponseModels.Reports.ReportTypesResponse;

import io.reactivex.Single;
import retrofit2.Response;

public interface Repository {
    Single<Response<ReportTypesResponse>> getAllReportTypes();
    Single<Response<ReportResponse>> createNewReport(ReportPost post);
}
