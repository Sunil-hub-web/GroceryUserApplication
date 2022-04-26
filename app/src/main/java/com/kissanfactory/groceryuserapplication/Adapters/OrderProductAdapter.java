package com.kissanfactory.groceryuserapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kissanfactory.groceryuserapplication.Activities.ProductDetailActivity;
import com.kissanfactory.groceryuserapplication.Helpers.SSLCertificateHandler;
import com.kissanfactory.groceryuserapplication.Models.Product;
import com.kissanfactory.groceryuserapplication.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderListingViewholder> {

    public OrderProductAdapter(Context context) {
        this.context = context;
        SSLCertificateHandler.nuke();
    }

    private List<Product> listings;
    private Context context;
    public void setListings(List<Product> listings) {
        this.listings = listings;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderListingViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderListingViewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_listing_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListingViewholder holder, int position) {
        holder.name.setText(StringUtils.capitalize(listings.get(position).getTitle()));
        holder.price.setText("Unit Price: \t₹"+ listings.get(position).getPrice()+" (-"+listings.get(position).getDiscount()+"%)");
        holder.qty.setText("QTY: \t"+ listings.get(position).getProductQuantity());
        if(listings.get(position).getType().equals("product")){
            // gst from experience
            holder.gst.setText("GST: \t"+ listings.get(position).getExperience() + "%");
        }else{
            // gst from weight if it exists
            if(listings.get(position).getWeight().size()>0){
                holder.gst.setText("GST: \t"+ listings.get(position).getWeight().get(0) + "%");
            }
        }
        if(listings.get(position).getImages().size()>0){
           // String url = "https://api.kisaan-factory.com/" + listings.get(position).getImages().get(0).replace("http://13.58.30.53:3000/", "");
            String url = "https://kisaanandfactory.com/static_file/" + listings.get(position).getImages().get(0);
            Log.e("the url", url);
            Glide.with(context).load(url).into(holder.prodImg);
//            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
//            ImageLoader imageLoader = ImageLoader.getInstance();
//            imageLoader.displayImage(url,holder.prodImg);
        }

        holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context, ProductDetailActivity.class)
                .putExtra("id", listings.get(position).getId())));
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public static class OrderListingViewholder extends RecyclerView.ViewHolder{
        TextView name, gst, price, qty;
        ImageView prodImg;

        public OrderListingViewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            prodImg = itemView.findViewById(R.id.productImage);
            price = itemView.findViewById(R.id.unitPrice);
            gst = itemView.findViewById(R.id.gst);
            qty = itemView.findViewById(R.id.qty);
        }
    }
}
