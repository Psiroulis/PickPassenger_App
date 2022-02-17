
package com.redpepper.taxiapp.RideReports;

import android.content.SharedPreferences;

import com.redpepper.taxiapp.Http.PostModels.Reports.ReportPost;
import com.redpepper.taxiapp.Http.ResponseModels.Reports.ReportResponse;
import com.redpepper.taxiapp.Http.ResponseModels.Reports.ReportTypesResponse;
import com.redpepper.taxiapp.Http.apiservices.PassengerApiService;

import io.reactivex.Single;
import retrofit2.Response;

public class NetworkRepository implements Repository {

    SharedPreferences prefs;
    PassengerApiService apiService;
    String token;


    public NetworkRepository(SharedPreferences prefs, PassengerApiService service) {
        this.prefs = prefs;
        this.apiService = service;
        this.token = "Bearer " + prefs.getString("access_Token",null);
    }

    @Override
    public Single<Response<ReportTypesResponse>> getAllReportTypes() {
        return apiService.getAllReportTypes();
    }

    @Override
    public Single<Response<ReportResponse>> createNewReport(ReportPost post) {
        return apiService.createNewReport(token,post);
    }
}
