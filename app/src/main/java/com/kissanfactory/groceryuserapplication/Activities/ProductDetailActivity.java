package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Adapters.CategoryProductAdapter;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.Cart;
import com.kissanfactory.groceryuserapplication.Models.NoMsgResponse;
import com.kissanfactory.groceryuserapplication.Models.Product;
import com.kissanfactory.groceryuserapplication.Models.ProductResponse;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;

import org.apache.commons.lang3.StringUtils;
import org.imaginativeworld.whynotimagecarousel.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView title, discount, totalRating, type, price, description, minus, add;
    private RecyclerView similarProducts;
    private ProgressBar loading;
    private NestedScrollView container;
    private Button addToCartBtn, buyNowBtn;

    private EditText quantity;
    int quantityNum = 1;
    int stock = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        init();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityNum < stock) {
                    quantityNum += 1;
                    quantity.setText(String.valueOf(quantityNum));
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (quantityNum > 1) {
                    quantityNum -= 1;
                    quantity.setText(String.valueOf(quantityNum));
                }
            }
        });
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.productTitle);
        type = findViewById(R.id.type);
        price = findViewById(R.id.productPrice);
        totalRating = findViewById(R.id.totalRating);
        similarProducts = findViewById(R.id.similarProducts);
        loading = findViewById(R.id.loading);
        container = findViewById(R.id.container);
        minus = findViewById(R.id.minus);
        add = findViewById(R.id.add);
        quantity = findViewById(R.id.quantity);
        discount = findViewById(R.id.discount);
        description = findViewById(R.id.description);


        addToCartBtn = findViewById(R.id.addToCart);
        addToCartBtn.setOnClickListener(view -> addToCart(false));
        //  buyNowBtn = findViewById(R.id.buyNow);
        //  buyNowBtn.setOnClickListener(view -> addToCart(true));

        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);

            TextView title = toolbar.findViewById(R.id.tb_title);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            title.setText("Product Description");
        }
        getDetails();
    }


    private void addToCart(boolean goToCart) {
        if (getSharedPreferences("UserData", MODE_PRIVATE).getBoolean("exist", false)) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setMessage("Please Wait");
            dialog.setTitle("Adding to cart");
            dialog.create();
            dialog.show();

            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
            String token = sharedPreferences.getString("token", "");

            Cart cart = new Cart();
            cart.setQuantity(quantityNum);
            cart.setName("Cart Item");
            cart.setImage("");

            Call<NoMsgResponse> call = new UserApiToJsonHandler().addToCart(token, getIntent().getStringExtra("id"), cart);
            call.enqueue(new Callback<NoMsgResponse>() {
                @Override
                public void onResponse(Call<NoMsgResponse> call, Response<NoMsgResponse> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        Toast.makeText(ProductDetailActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Item Already in Cart", Toast.LENGTH_SHORT).show();
                    }

                    if (goToCart) {
                        startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<NoMsgResponse> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "Login to Add to cart", Toast.LENGTH_SHORT).show();
        }
    }

    // get details
    private void getDetails() {
        loading.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Call<ProductResponse> call = new UserApiToJsonHandler().getProductDetails(getIntent().getStringExtra("id"));
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    loading.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);
                    Product product = response.body().getProductDetails();
                    fillFields(product);
                    fillSimilarProducts(product.getCategoryId());
                } else {
                    // do something here
                    loading.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                // do something here
                loading.setVisibility(View.GONE);
                container.setVisibility(View.VISIBLE);
            }
        });
    }

    // similar products
    private void fillSimilarProducts(String category) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Call<ApiResponse> fruitsCall = new UserApiToJsonHandler().categoryProducts(category);
        fruitsCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getProductDetails() != null) {
                        CategoryProductAdapter adapter = new CategoryProductAdapter(ProductDetailActivity.this);

                        adapter.setProductList(response.body().getProductDetails());
                        similarProducts.setAdapter(adapter);

                        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailActivity.this, RecyclerView.HORIZONTAL, false);
                        similarProducts.addItemDecoration(new RecyclerView.ItemDecoration() {
                            @Override
                            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                outRect.left = 20;
                            }
                        });
                        similarProducts.setLayoutManager(layoutManager);
                    } else {
                        // hide that category
                    }
                } else {
                    Log.e("Error: ", "Something went bad!");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("Error: ", t.getMessage());
            }
        });
    }

    // fill the fields of the product
//    @RequiresApi(api = Build.VERSION_CODES.N)
    private void fillFields(Product product) {
        ImageCarousel carousel = findViewById(R.id.carousel);
        totalRating.setText("Stock: " + String.valueOf(product.getInStock()));
        stock = product.getInStock();
        title.setText(StringUtils.capitalize(product.getTitle()));
        price.setText("₹ " + product.getPrice());
        discount.setText("Discount: " + product.getDiscount() + "%");
        type.setText(product.getType());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            description.setText(Html.fromHtml(product.getDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
        } else {
            description.setText(Jsoup.parse(product.getDescription()).text());
        }

        // fill up the images
        List<CarouselItem> imageUrls = new ArrayList<>();
        for (String url : product.getImages()) {
            imageUrls.add(new CarouselItem("https://kisaanandfactory.com/static_file/" + url));
        }
        carousel.addData(imageUrls);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}