package com.kissanfactory.groceryuserapplication.Adapters;

import static android.content.Context.MODE_PRIVATE;

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
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.Cart;
import com.kissanfactory.groceryuserapplication.Models.NoMsgResponse;
import com.kissanfactory.groceryuserapplication.Models.Product;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDetailsSearchAdapter extends RecyclerView.Adapter<CategoryDetailsSearchAdapter.CategoryDetailsViewHolder> {

    private List<Product> productList;
    private Activity context;

    public CategoryDetailsSearchAdapter(Activity context) {
        this.context = context;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryDetailsViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.category_detail_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryDetailsViewHolder holder, int position) {
        holder.name.setText(StringUtils.capitalize(productList.get(position).getTitle()));
        holder.price.setText("₹ "+ productList.get(position).getPrice());
        Glide.with(context).load("https://kisaanandfactory.com/static_file/" + productList.get(position).getImages().get(0)).into(holder.prodImg);
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

            Call<NoMsgResponse> call = new UserApiToJsonHandler().addToCart(token, productList.get(position).getId(), cart);
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
            .putExtra("id", productList.get(position).getId()));
        });

        holder.like.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", MODE_PRIVATE);

            if (sharedPreferences.getBoolean("exist", false)){
                ProgressDialog dialog = new ProgressDialog(context);
                dialog.setCancelable(false);
                dialog.setTitle("Adding to Favourites");
                dialog.setMessage("Please wait...");
                dialog.create();

                String token = sharedPreferences.getString("token", "");
                if (!productList.get(position).isLiked()){
                    dialog.show();

                    productList.get(position).setProductId();
                    Call<NoMsgResponse> call1 = new UserApiToJsonHandler().addToFavourites(token, productList.get(position));
                    call1.enqueue(new Callback<NoMsgResponse>() {
                        @Override
                        public void onResponse(Call<NoMsgResponse> call, Response<NoMsgResponse> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()){
                                Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
                                holder.like.setImageResource(R.drawable.liked);
                            }else{
                                holder.like.setImageResource(R.drawable.liked);
                                Gson gson = new Gson();
                                ApiResponse message = gson.fromJson(response.errorBody().charStream(),ApiResponse.class);
                                Toast.makeText(context, message.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<NoMsgResponse> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    // dislike it here
                    dialog.setTitle("Removing From Favourites");
                    dialog.show();

                    Call<ApiResponse> removeFav = new UserApiToJsonHandler().deleteFavourite(token, productList.get(position).getId());
                    removeFav.enqueue(new Callback<ApiResponse>() {
                        @Override
                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()){
                                Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                            }else{
                                Gson gson = new Gson();
                                ApiResponse message = gson.fromJson(response.errorBody().charStream(),ApiResponse.class);
                                Toast.makeText(context, message.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }else{
                Toast.makeText(context, "Login to add favourites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class CategoryDetailsViewHolder extends RecyclerView.ViewHolder{
        private TextView name, price;
        private Button addToCart;
        private ImageView like, prodImg;
        private CardView rootView;
        public CategoryDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.rootView);
            addToCart = itemView.findViewById(R.id.addToCart);
            name = itemView.findViewById(R.id.productName);
            prodImg = itemView.findViewById(R.id.prodImg);
            price = itemView.findViewById(R.id.productPrice);
            like = itemView.findViewById(R.id.icon);
        }
    }
}
