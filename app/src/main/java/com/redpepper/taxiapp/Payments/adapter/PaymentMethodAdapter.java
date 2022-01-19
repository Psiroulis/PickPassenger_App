package com.redpepper.taxiapp.Payments.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.redpepper.taxiapp.R;
import com.redpepper.taxiapp.databinding.PaymentFragmentListItemBinding;
import com.redpepper.taxiapp.Http.ResponseModels.payments.StripePaymentMethod;

import java.util.List;
import java.util.Objects;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {

    private final List<StripePaymentMethod> data;
    private final PaymentsListItemClickListener listener;
    private final Context context;

    public PaymentMethodAdapter(Context context, List<StripePaymentMethod> data, PaymentsListItemClickListener listener) {
        this.data = data;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        PaymentFragmentListItemBinding binding = PaymentFragmentListItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        StripePaymentMethod paymentMethod = data.get(position);

        SharedPreferences preferences = context.getSharedPreferences("app",Context.MODE_PRIVATE);


        if(Objects.requireNonNull(preferences.getString("lastPaymentMethodSelected", ""))
                .equalsIgnoreCase(paymentMethod.getId())){

            holder.parent.setBackgroundResource(R.drawable.bg_yellow_selected_payment_type);

        }

        if(paymentMethod.getBrand().equalsIgnoreCase("visa")){

            holder.cardBrandImage.setImageResource(R.drawable.ic_visa);

        }else if(paymentMethod.getBrand().equalsIgnoreCase("mastercard")){

            holder.cardBrandImage.setImageResource(R.drawable.ic_mastercard);
        }

        holder.cardLastFour.setText(paymentMethod.getLast4());

        holder.cardMethodId.setText(paymentMethod.getId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface PaymentsListItemClickListener{
        void onPaymentItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ConstraintLayout parent;
        public ImageView cardBrandImage;
        public TextView cardMethodId;
        public TextView cardLastFour;


        public ViewHolder(PaymentFragmentListItemBinding binding) {
            super(binding.getRoot());

            parent = binding.payfragItemParent;
            cardBrandImage = binding.payfragItemCardImg;
            cardMethodId = binding.payfragItemMethodId;
            cardLastFour = binding.payfragItemCardLastFour;

            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onPaymentItemClick(v,this.getLayoutPosition());
        }
    }


}


//