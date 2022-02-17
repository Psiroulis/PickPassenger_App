package com.redpepper.taxiapp.RideReports;

import com.redpepper.taxiapp.Http.PostModels.Reports.ReportPost;
import com.redpepper.taxiapp.RideReports.Models.ReportType;

import java.util.List;

import io.reactivex.Single;

public interface ReportsFragmentMVP {
    interface Model{
        Single<List<ReportType>> getReportTypes();
        Single<String> createNewReport(ReportPost post);
    }

    interface View{
        void populateReportTypesList(List<ReportType> list);
        void showSuccessfulReportCreationMessage();
    }

    interface Presenter{
        void setView(ReportsFragmentMVP.View view);
        void getAllReportTypes();
        void createNewReport(ReportPost post);
        void rxUnsubscribe();
    }
}
