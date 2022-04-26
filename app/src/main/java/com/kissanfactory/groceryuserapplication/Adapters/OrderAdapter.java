package com.kissanfactory.groceryuserapplication.Adapters;

import android.annotation.SuppressLint;
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
import com.kissanfactory.groceryuserapplication.Activities.OrderDetailsActivity;
import com.kissanfactory.groceryuserapplication.Helpers.SSLCertificateHandler;

import com.kissanfactory.groceryuserapplication.Models.Order;
import com.kissanfactory.groceryuserapplication.Models.ProductResponse;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Activity context;
    private List<Order> orders;

    public OrderAdapter(Activity context) {
        SSLCertificateHandler.nuke();
        this.context = context;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_item, parent, false));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {


        holder.tv_price.setText("₹ " +  orders.get(position).getTotalAmount());
        if (orders.get(position).getProducts()!=null){
            for (int i = 0; i < orders.get(position).getProducts().size(); i++) {
                holder.cartId.setText(orders.get(position).getProducts().get(i).getProductName());
                holder.tv_item_count.setText(orders.get(position).getProducts().get(i).getProductQuantity()+"x");
            }
        }


//       holder.status.setText(orders.get(position).getOrderStatus());
//       holder.date.setText(orders.get(position).getCreatedAt().split("T")[0]+
//                " - " +orders.get(position).getCreatedAt().split("T")[1].split("[.]")[0]);

        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, OrderDetailsActivity.class).putExtra("cartId", orders.get(position).getCart_id()));
        });

        // load order img
        if (orders.get(position).getOrderImg() != null) {
           // String url = "https://api.kisaan-factory.com/" + orders.get(position).getOrderImg().replace("http://13.58.30.53:3000/", "");
            String url = "https://kisaanandfactory.com/static_file/" + orders.get(position).getOrderImg();
           // Glide.with(context).load(url).into(holder.orderImage).onLoadFailed(context.getDrawable(R.drawable.order_image));
            Glide.with(context).load(url).into(holder.orderImage);
        } else {
            Glide.with(context).clear(holder.orderImage);
        }
//        Glide.with(context).load(orders.get(position).getImage()).into(holder.orderImage).onLoadFailed(context.getDrawable(R.drawable.order_image));
//        getImage(orders.get(position).getProducts().get(0).getProductId(), holder.orderImage);
    }


    // get order image
    private void getImage(String id, ImageView imageView) {
        Call<ProductResponse> call = new UserApiToJsonHandler().getProductDetails(id);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body().getProductDetails() != null) {
                    Glide.with(context).load(response.body().getProductDetails().getImages().get(0)).into(imageView);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
//        return 5;
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView cartId, status, tv_item_count, tv_price, payStatus;
        ImageView orderImage;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderImage = itemView.findViewById(R.id.orderPic);
            cartId = itemView.findViewById(R.id.orderCartId);
            status = itemView.findViewById(R.id.orderStatus);
            tv_item_count = itemView.findViewById(R.id.tv_item_count);
            payStatus = itemView.findViewById(R.id.payStatus);
            tv_price = itemView.findViewById(R.id.tv_price);
        }
    }
}
