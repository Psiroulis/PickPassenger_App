package com.redpepper.taxiapp.Search_locations.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.redpepper.taxiapp.R;

import java.util.ArrayList;

public class GoogleSearchAdapter extends RecyclerView.Adapter<GoogleSearchAdapter.ViewHolder> {

    private ArrayList<AutocompletePrediction> data;

    private ItemClickListener listener;

    public GoogleSearchAdapter(ArrayList<AutocompletePrediction> data, ItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GoogleSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.serfrag_google_result_list_item,
                parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoogleSearchAdapter.ViewHolder holder, int position) {

        AutocompletePrediction prediction = data.get(position);

        holder.primaryText.setText(prediction.getPrimaryText(null));
        holder.secondaryText.setText(prediction.getSecondaryText(null));

    }

    @Override
    public int getItemCount() { return data.size(); }


    public class  ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView primaryText;
        private final TextView secondaryText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            primaryText = itemView.findViewById(R.id.serFragGoogleResult_primary_txt);
            secondaryText = itemView.findViewById(R.id.serFragGoogleResult_secondary_txt);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onGoogleItemClick(v,this.getLayoutPosition());
        }
    }

    public interface ItemClickListener{
        void onGoogleItemClick(View v, int position);

    }
}
