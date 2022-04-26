package com.kissanfactory.groceryuserapplication.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kissanfactory.groceryuserapplication.Activities.ProductDetailActivity;
import com.kissanfactory.groceryuserapplication.Helpers.SSLCertificateHandler;
import com.kissanfactory.groceryuserapplication.Models.Cart;
import com.kissanfactory.groceryuserapplication.Models.Favourite;
import com.kissanfactory.groceryuserapplication.Models.NoMsgResponse;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        public void onFavouriteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private Activity context;
    private List<Favourite> favouriteList;

    public FavouritesAdapter(Activity context) {
        this.context = context;
        SSLCertificateHandler.nuke();
    }

    public void setFavouriteList(List<Favourite> favouriteList) {
        this.favouriteList = favouriteList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavouritesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_detail_item, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
        try{
            holder.name.setText(StringUtils.capitalize(favouriteList.get(position).getProductId().getTitle()));
            holder.price.setText("₹ "+ favouriteList.get(position).getProductId().getPrice());
            holder.icon.setImageResource(R.drawable.liked);
         //   String url = "https://api.kisaan-factory.com/" + favouriteList.get(position).getProductId().getImages().get(0).replace("http://13.58.30.53:3000/", "");
            String url = "https://kisaanandfactory.com/static_file/" + favouriteList.get(position).getProductId().getImages();
            Glide.with(context).load(url).into(holder.prodImg);
//            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
//            ImageLoader imageLoader = ImageLoader.getInstance();
//            imageLoader.displayImage(url,holder.prodImg);
            holder.addToCart.setOnClickListener(view -> {
                ProgressDialog dialog = new ProgressDialog(context);
                dialog.setCancelable(false);
                dialog.setMessage("Please Wait");
                dialog.setTitle("Adding to cart");
                dialog.create();
                dialog.show();

                SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", MODE_PRIVATE);
                String token = sharedPreferences.getString("token", "");

                Cart cart = new Cart();
                cart.setImage("");
                cart.setName("cart item");
                cart.setQuantity(1);

                Call<NoMsgResponse> call = new UserApiToJsonHandler().addToCart(token, favouriteList.get(position).getProductId().getId(), cart);
                call.enqueue(new Callback<NoMsgResponse>() {
                    @Override
                    public void onResponse(Call<NoMsgResponse> call, Response<NoMsgResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()){
                            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<NoMsgResponse> call, Throwable t) {
                        dialog.dismiss();
                    }
                });
            });
            holder.rootView.setOnClickListener(view -> {
                context.startActivity(new Intent(context, ProductDetailActivity.class)
                        .putExtra("id", favouriteList.get(position).getProductId().getId()));
            });
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public static class FavouritesViewHolder extends RecyclerView.ViewHolder{
        private TextView name, price;
        private Button addToCart;
        private ImageView icon, prodImg;
        private CardView rootView;
        public FavouritesViewHolder(@NonNull View itemView, OnItemClickListener clickListener) {
            super(itemView);


            rootView = itemView.findViewById(R.id.rootView);
            icon = itemView.findViewById(R.id.icon);
            prodImg = itemView.findViewById(R.id.prodImg);
            addToCart = itemView.findViewById(R.id.addToCart);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            icon.setOnClickListener(view -> {
                if (clickListener!=null){
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION){
                        clickListener.onFavouriteClick(position);
                    }
                }
            });
        }
    }
}
