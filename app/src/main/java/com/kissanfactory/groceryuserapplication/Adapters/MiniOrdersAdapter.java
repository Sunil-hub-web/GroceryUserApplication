package com.kissanfactory.groceryuserapplication.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kissanfactory.groceryuserapplication.Activities.MiniOrderDetailActivity;
import com.kissanfactory.groceryuserapplication.Helpers.SSLCertificateHandler;
import com.kissanfactory.groceryuserapplication.Models.Order;
import com.kissanfactory.groceryuserapplication.R;

import java.util.List;

public class MiniOrdersAdapter extends RecyclerView.Adapter<MiniOrdersAdapter.MiniOrdersViewHolder> {

    List<Order> orders;
    Activity context;

    public MiniOrdersAdapter(Activity context) {
        this.context = context;
        SSLCertificateHandler.nuke();
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MiniOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MiniOrdersViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.mini_order_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MiniOrdersViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.id.setText(order.get_id());
        holder.status.setText(order.getOrderStatus());
        String date = order.getCreatedAt().split("T")[0];
        holder.date.setText(date);

        holder.itemView.setOnClickListener(v -> {
            context.startActivityForResult(new Intent(context, MiniOrderDetailActivity.class).putExtra("id", order.get_id()), 100);
        });

        // load order img
        if (order.getOrderImg()!=null){
       //     String url = "https://api.kisaan-factory.com/" + orders.get(position).getOrderImg().replace("http://13.58.30.53:3000/", "");
            String url = "https://kisaanandfactory.com/static_file/" + orders.get(position).getOrderImg();
      //      Glide.with(context).load(url).into(holder.orderImg).onLoadFailed(context.getDrawable(R.drawable.order_image));
            Glide.with(context).load(url).into(holder.orderImg);
        }else{
            Glide.with(context).clear(holder.orderImg);
        }

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class MiniOrdersViewHolder extends RecyclerView.ViewHolder{
        TextView id, date, status;
        ImageView orderImg;
        public MiniOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImg = itemView.findViewById(R.id.orderImg);
            id = itemView.findViewById(R.id.orderId);
            date = itemView.findViewById(R.id.orderDate);
            status = itemView.findViewById(R.id.orderStatus);
        }
    }
}
