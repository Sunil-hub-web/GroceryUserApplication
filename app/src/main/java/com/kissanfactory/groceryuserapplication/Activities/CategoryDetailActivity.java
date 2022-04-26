package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Adapters.CategoryDetailsAdapter;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.viewallTopProducts.TopProductViewAlltDTO;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView productsList;
    private ProgressBar loading;
    private String vUrl = "", vCategory = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        productsList = findViewById(R.id.categoryItemList);
        loading = findViewById(R.id.loading);

        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TextView title = toolbar.findViewById(R.id.tb_title);
            if (getIntent() != null) {
                try {
                    vCategory = getIntent().getStringExtra("category");
                    vUrl = getIntent().getStringExtra("Url");
                    String titleTxt = getIntent().getStringExtra("name");

                    titleTxt = titleTxt.replace("_", " ");
                    String[] txts = titleTxt.split(" ");
                    titleTxt = "";
                    for (String string : txts) {
                        string = string.substring(0, 1).toUpperCase() + string.substring(1);
                        titleTxt = titleTxt + string + " ";
                    }
                    title.setText(titleTxt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        getProducts();
    }

    // getting the products of each category
    private void getProducts() {
        loading.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if (vCategory.equals("1")) {
            Call<TopProductViewAlltDTO> fruitsCall;
            fruitsCall = new UserApiToJsonHandler().getalltop_category(vUrl);
            fruitsCall.enqueue(new Callback<TopProductViewAlltDTO>() {
                @Override
                public void onResponse(Call<TopProductViewAlltDTO> call, Response<TopProductViewAlltDTO> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getData() != null) {
                            loading.setVisibility(View.GONE);
                            CategoryDetailsAdapter adapter = new CategoryDetailsAdapter(CategoryDetailActivity.this);

                            adapter.setProductList(response.body().getData());
                            productsList.setAdapter(adapter);

                            LinearLayoutManager layoutManager = new LinearLayoutManager(CategoryDetailActivity.this);
                            productsList.addItemDecoration(new RecyclerView.ItemDecoration() {
                                @Override
                                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                                    super.getItemOffsets(outRect, view, parent, state);
                                    outRect.bottom = 10;
                                }
                            });
                            productsList.setLayoutManager(layoutManager);
                        } else {
                            // hide that category
                            loading.setVisibility(View.GONE);
                            Toast.makeText(CategoryDetailActivity.this, "No Items in this category", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        loading.setVisibility(View.GONE);
                        Log.e("Error: ", "Something went wrong!");
                    }
                }

                @Override
                public void onFailure(Call<TopProductViewAlltDTO> call, Throwable t) {
                    loading.setVisibility(View.GONE);
                    Log.e("Error: ", t.getMessage());
                }
            });


        } else {
            Call<ApiResponse> fruitsCall;
            fruitsCall = new UserApiToJsonHandler().categoryProducts(getIntent().getStringExtra("category"));


            fruitsCall.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getProductDetails() != null) {
                            loading.setVisibility(View.GONE);
                            CategoryDetailsAdapter adapter = new CategoryDetailsAdapter(CategoryDetailActivity.this);

                            adapter.setProductList(response.body().getProductDetails());
                            productsList.setAdapter(adapter);

                            LinearLayoutManager layoutManager = new LinearLayoutManager(CategoryDetailActivity.this);
                            productsList.addItemDecoration(new RecyclerView.ItemDecoration() {
                                @Override
                                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                                    super.getItemOffsets(outRect, view, parent, state);
                                    outRect.bottom = 10;
                                }
                            });
                            productsList.setLayoutManager(layoutManager);
                        } else {
                            // hide that category
                            loading.setVisibility(View.GONE);
                            Toast.makeText(CategoryDetailActivity.this, "No Items in this category", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        loading.setVisibility(View.GONE);
                        Log.e("Error: ", "Something went wrong!");
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    loading.setVisibility(View.GONE);
                    Log.e("Error: ", t.getMessage());
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        } else if (item.getItemId() == R.id.db_cart) {
            if (this.getSharedPreferences("UserData", MODE_PRIVATE).getBoolean("exist", false)) {
                startActivity(new Intent(this, CartActivity.class));
            } else {
                Toast.makeText(this, "Login to go to cart", Toast.LENGTH_SHORT).show();
            }

        } else if (item.getItemId() == R.id.db_notification) {
            startActivity(new Intent(this, NotificationActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}