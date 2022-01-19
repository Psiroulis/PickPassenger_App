package com.redpepper.taxiapp.Search_locations.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.redpepper.taxiapp.R;
import com.redpepper.taxiapp.databinding.SerfragFoursquareResultListItemBinding;
import com.redpepper.taxiapp.Search_locations.Models.FoursquarePlace;

import java.util.ArrayList;

public class FoursquareSearchAdapter extends RecyclerView.Adapter<FoursquareSearchAdapter.ViewHolder> {

    private ArrayList<FoursquarePlace> data;
    private ItemClickListener listener;

    public FoursquareSearchAdapter(ArrayList<FoursquarePlace> data, ItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoursquareSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.serfrag_foursquare_result_list_item,
                parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoursquareSearchAdapter.ViewHolder holder, int position) {

        FoursquarePlace foursquarePlace = data.get(position);

        holder.binding.serfragFoursquareitemPlaceName.setText(foursquarePlace.getName());

        holder.binding.serfragFoursquareitemPlaceAddress.setText(foursquarePlace.getAddress().split(",")[0]);

        holder.binding.serfragFoursquareitemDistance.setText(foursquarePlace.getDistance());

        holder.binding.serfragFoursquareitemMetrics.setText(foursquarePlace.getDistanceMetrics());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        SerfragFoursquareResultListItemBinding binding;
       
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = SerfragFoursquareResultListItemBinding.bind(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onFoursquareItemClick(v,this.getLayoutPosition());
        }
    }

    public interface ItemClickListener{
        void onFoursquareItemClick(View v, int position);
    }
}
