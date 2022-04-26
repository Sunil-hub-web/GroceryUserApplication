package com.kissanfactory.groceryuserapplication.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kissanfactory.groceryuserapplication.Models.paymenthistory.Datum;
import com.kissanfactory.groceryuserapplication.R;

import java.util.List;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.PaymentViewHolder> {

    private Activity context;
    private List<Datum> paymentHistoryList;


    public PaymentHistoryAdapter(Activity context) {
        this.context = context;
    }

    public void setPaymentHistoryList(List<Datum> paymentHistoryList) {
        this.paymentHistoryList = paymentHistoryList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentViewHolder holder, int position) {

        holder.orderId.setText(paymentHistoryList.get(position).getPaymentStatus().getStatus());
        holder.orderDate.setText(paymentHistoryList.get(position).getCreatedAt().split("T")[0]);

        String[] prices = String.valueOf(paymentHistoryList.get(position).getAmount()).split("[.]");
        String price = "₹" + prices[0] + "." + prices[1].substring(0, 1);
        holder.price.setText(price);
    }

    @Override
    public int getItemCount() {
        return paymentHistoryList.size();
    }

    public static class PaymentViewHolder extends RecyclerView.ViewHolder {
        private TextView price, orderId, orderDate;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.payAmount);
            orderDate = itemView.findViewById(R.id.payDate);
            orderId = itemView.findViewById(R.id.payId);
        }
    }
}
