package com.redpepper.taxiapp.RideReports;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.redpepper.taxiapp.Http.PostModels.Reports.ReportPost;
import com.redpepper.taxiapp.RideReports.Adapters.ReportRecyclerViewAdapter;
import com.redpepper.taxiapp.RideReports.Models.ReportType;
import com.redpepper.taxiapp.Root.App;
import com.redpepper.taxiapp.databinding.FragmentReportsListBinding;

import java.util.List;

import javax.inject.Inject;

public class ReportFragment extends Fragment  implements ReportsFragmentMVP.View,ReportRecyclerViewAdapter.ReportItemClickListener{

    private FragmentReportsListBinding binding;

    private List<ReportType> reportTypes;

    private boolean showSubmitForm = false;

    private int rideId,reportTypeForPost;




    @Inject
    ReportsFragmentMVP.Presenter presenter;


    public ReportFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        ((App) context.getApplicationContext()).getComponent().inject(this);

        presenter.getAllReportTypes();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter.setView(this);

        if (getArguments() != null) {

            rideId = getArguments().getInt("rideId");

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentReportsListBinding.inflate(inflater,container,false);

        binding.reportFragBackButton.setOnClickListener(v->{

            if(!showSubmitForm){
                //Todo: Close fragment
            }else{
                binding.reportFragFormLayout.setVisibility(View.GONE);
                binding.reportfragDivider.setVisibility(View.VISIBLE);
                binding.reportFragList.setVisibility(View.VISIBLE);

                showSubmitForm =false;
            }

        });

        binding.reportFragSubBtn.setOnClickListener(v->{
            ReportPost post = new ReportPost(rideId,reportTypeForPost,binding.reportFragSubDescription.getText().toString());
            presenter.createNewReport(post);
        });

        return binding.getRoot();
    }

    @Override
    public void populateReportTypesList(List<ReportType> list) {

        reportTypes = list;

        ReportRecyclerViewAdapter adapter = new ReportRecyclerViewAdapter(list,this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        binding.reportFragList.setLayoutManager(layoutManager);
        binding.reportFragList.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        binding.reportFragList.addItemDecoration(itemDecor);
        binding.reportFragList.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onReportItemClick(View view, int position) {

        ReportType reportType =  reportTypes.get(position);

        binding.reportfragDivider.setVisibility(View.GONE);
        binding.reportFragList.setVisibility(View.GONE);
        binding.reportFragSubTitle.setText(reportType.getTitle());
        binding.reportFragSubDescription.setText("");
        binding.reportFragFormLayout.setVisibility(View.VISIBLE);

        reportTypeForPost = reportType.getId();

        showSubmitForm = true;

    }

    @Override
    public void showSuccessfulReportCreationMessage() {
        Log.d("blepo","To report created");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.rxUnsubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.rxUnsubscribe();
    }
}