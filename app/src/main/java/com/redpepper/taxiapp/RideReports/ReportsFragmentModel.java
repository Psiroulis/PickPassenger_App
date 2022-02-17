package com.redpepper.taxiapp.RideReports;

import com.redpepper.taxiapp.Http.PostModels.Reports.ReportPost;
import com.redpepper.taxiapp.Http.ResponseModels.CustomClasses.ImportReportType;
import com.redpepper.taxiapp.RideReports.Models.ReportType;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public class ReportsFragmentModel implements ReportsFragmentMVP.Model {
    Repository repository;

    public ReportsFragmentModel(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Single<List<ReportType>> getReportTypes() {
        return repository.getAllReportTypes().map(reportTypesResponseResponse -> {
            List<ReportType> list = new ArrayList<>();
            for (ImportReportType report:reportTypesResponseResponse.body().getReportTypes()) {

                ReportType rep = new ReportType(report.getId(),report.getTitle());
                list.add(rep);

            }
            return list;
        });
    }

    @Override
    public Single<String> createNewReport(ReportPost post) {
        return repository.createNewReport(post).map(reportResponse ->{
            if(reportResponse.body().getSuccess().equalsIgnoreCase("true"))
                return "true";

            return "false";

        });
    }
}
