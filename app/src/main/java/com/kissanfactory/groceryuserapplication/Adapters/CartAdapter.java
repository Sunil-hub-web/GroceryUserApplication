package com.kissanfactory.groceryuserapplication.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kissanfactory.groceryuserapplication.Activities.CartActivity;
import com.kissanfactory.groceryuserapplication.Helpers.SSLCertificateHandler;
import com.kissanfactory.groceryuserapplication.Models.Cart;
import com.kissanfactory.groceryuserapplication.R;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        public void delete(int position);
        public void changeQTy(int position, boolean increase);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private Activity context;
    private List<Cart> cartList;

    public CartAdapter(Activity context) {
        this.context = context;
        SSLCertificateHandler.nuke();
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.cart_item, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (cartList.get(position).getItem()!=null){

            float price = cartList.get(position).getItem().getPrice();
            float disc_price = cartList.get(position).getItem().getDiscount();
            float total_price = price * disc_price / 100;
            float total_Amount = price - total_price;
            DecimalFormat df = new DecimalFormat("#.##");
            float total_Amount1 = Float.valueOf(df.format(total_Amount));

            String totalprice = String.valueOf(total_price);

            holder.discountPrice.setText("₹ "+ cartList.get(position).getItem().getPrice());
            holder.productPrice.setText("₹ "+ total_Amount1);
            holder.name.setText(StringUtils.capitalize(cartList.get(position).getItem().getTitle()));
            holder.quantity.setText(String.valueOf(cartList.get(position).getQuantity()));
           // String urlS = "https://api.kisaan-factory.com/" + cartList.get(position).getItem().getImages().get(0).replace("http://13.58.30.53:3000/", "");
            String urlS = "https://kisaanandfactory.com/static_file/" + cartList.get(position).getItem().getImages().get(0);
//            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
//            ImageLoader imageLoader = ImageLoader.getInstance();
//            imageLoader.displayImage(urlS,holder.prodImg);
//            Glide.with(context).load(urlS).into(holder.prodImg);
            Picasso.get().load(urlS).into(holder.prodImg);
            //(CartActivity.cartActivity).cart_Total(cartList.get(position).getItem().getPrice(),cartList.get(position).getItem().getTitle().toString());

            holder.discountPrice.setPaintFlags(holder.discountPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder{
        private TextView name, price, quantity,productPrice,discountPrice;
        private ImageView delete, increase, decrease, prodImg;
        public CartViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            prodImg = itemView.findViewById(R.id.prodImg);
            productPrice = itemView.findViewById(R.id.productPrice);
            discountPrice = itemView.findViewById(R.id.discountPrice);
            delete = itemView.findViewById(R.id.deleteBtn);
            delete.setOnClickListener(view -> {
                if (onItemClickListener!=null){
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION){
                        onItemClickListener.delete(position);
                    }
                }
            });

            increase = itemView.findViewById(R.id.increaseQty);
            increase.setOnClickListener(view -> {
                if (onItemClickListener!=null){
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION){
                        onItemClickListener.changeQTy(position, true);
                    }
                }
            });
            decrease = itemView.findViewById(R.id.decreaseQty);
            decrease.setOnClickListener(view -> {
                if (onItemClickListener!=null){
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION){
                        onItemClickListener.changeQTy(position, false);
                    }
                }
            });
            quantity = itemView.findViewById(R.id.quantity);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
        }
    }
}
