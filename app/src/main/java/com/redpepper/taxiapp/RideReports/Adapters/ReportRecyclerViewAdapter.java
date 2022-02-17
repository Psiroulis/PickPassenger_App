package com.redpepper.taxiapp.RideReports.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.redpepper.taxiapp.RideReports.Models.ReportType;
import com.redpepper.taxiapp.databinding.FragmentReportsBinding;

import java.util.List;


public class ReportRecyclerViewAdapter extends RecyclerView.Adapter<ReportRecyclerViewAdapter.ViewHolder> {

   private final List<ReportType> data;
   private ReportItemClickListener listener;

    public ReportRecyclerViewAdapter(List<ReportType> data, ReportItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FragmentReportsBinding binding = FragmentReportsBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent,false);

        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        ReportType reportType = data.get(position);

        holder.reportId.setText(String.valueOf(reportType.getId()));
        holder.reportTitle.setText(reportType.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView reportTitle;
        public TextView reportId;


        public ViewHolder(FragmentReportsBinding binding) {
            super(binding.getRoot());

            reportTitle = binding.reportItemTitle;
            reportId = binding.reportItemId;

            binding.getRoot().setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listener.onReportItemClick(v,this.getLayoutPosition());
        }
    }

    public interface ReportItemClickListener{
        void onReportItemClick(View view, int position);
    }
}