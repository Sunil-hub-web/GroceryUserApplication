package com.kissanfactory.groceryuserapplication.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kissanfactory.groceryuserapplication.Activities.ProductDetailActivity;
import com.kissanfactory.groceryuserapplication.Helpers.SSLCertificateHandler;
import com.kissanfactory.groceryuserapplication.Models.Cart;
import com.kissanfactory.groceryuserapplication.Models.NoMsgResponse;
import com.kissanfactory.groceryuserapplication.Models.Product;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.CategoryViewHolder> {

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onFavouriteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private List<Product> productList;
    private Activity context;

    public CategoryProductAdapter(Activity context) {
        this.context = context;
        SSLCertificateHandler.nuke();
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_grid_item, parent, false), clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.rootView.setOnClickListener(view -> {
            context.startActivity(new Intent(context, ProductDetailActivity.class)
                    .putExtra("id", productList.get(position).getId()));
        });
        //  String url = "https://api.kisaan-factory.com/" + productList.get(position).getImages().get(0).replace("http://13.58.30.53:3000/", "");
        String url = null;
        try {
            url = "https://kisaanandfactory.com/static_file/" + productList.get(position).getImages().get(0);
            System.out.println("image.........."+productList.get(position).getImages().get(0).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.displayImage(url,holder.prodImg);
        Glide.with(context).load(url).into(holder.prodImg);
        holder.name.setText(StringUtils.capitalize(productList.get(position).getTitle()));
        holder.price.setText("₹ " + productList.get(position).getPrice());

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.getSharedPreferences("UserData", MODE_PRIVATE).getBoolean("exist", false)) {
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
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Already in cart", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<NoMsgResponse> call, Throwable t) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(context, "Login to Add to cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (productList.get(position).isLiked()) {
            holder.icon.setImageResource(R.drawable.liked);
        }

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private CardView rootView;
        private TextView name, price;
        private LinearLayout addToCart;
        private ImageView icon, prodImg;

        public CategoryViewHolder(@NonNull View itemView, OnItemClickListener clickListener) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            addToCart = itemView.findViewById(R.id.addToCart);
            rootView = itemView.findViewById(R.id.rootView);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            prodImg = itemView.findViewById(R.id.productPic);
            icon.setOnClickListener(view -> {
                if (clickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onFavouriteClick(position);
                    }
                }
            });
        }
    }
}
