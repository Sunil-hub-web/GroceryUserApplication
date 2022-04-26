package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kissanfactory.groceryuserapplication.Adapters.CategoryDetailsSearchAdapter;
import com.kissanfactory.groceryuserapplication.AppHelper;
import com.kissanfactory.groceryuserapplication.Models.Product;
import com.kissanfactory.groceryuserapplication.Models.Product_Details.ProductList;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView searchTitle;
    private ProgressBar loading;
    private RecyclerView searchedProducts;
    //  SwipeRefreshLayout swipeRefreshLayout;
    int count = 0;
    CategoryDetailsSearchAdapter adapter;
    String vSearch_text = "";
    List<Product> productDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        try {
            vSearch_text = getIntent().getStringExtra("term").toString().trim();


        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {
        searchedProducts = findViewById(R.id.searchProductsView);
        searchTitle = findViewById(R.id.searchTitle);


        searchTitle.setText("Results for: " + vSearch_text);
        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(" ");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Search: ");
        }
        productDetails = new ArrayList<>();
        searchedProducts.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        search(vSearch_text, count);

        AppHelper.setupLoadMore(searchedProducts, new AppHelper.OnScrollToEnd() {
            @Override
            public void scrolledToEnd(int lastVisibleItem) {

                if (count != lastVisibleItem) {
                    count = lastVisibleItem;
                    search(vSearch_text, count);
                }
            }
        });

    }

    // getting the searched products
    private void search(String item, int count1) {

        Call<ProductList> call = new UserApiToJsonHandler().searchProducts(item, count1);
        call.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                // swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    if (response.body().getProductDetails() != null) {
                        // productDetails = response.body().getProductDetails();
                        productDetails.addAll(response.body().getProductDetails());
                        adapter = new CategoryDetailsSearchAdapter(SearchActivity.this);
                        adapter.setProductList(productDetails);

                        count = count + productDetails.size();
                    } else {
                        Log.d("Response", response.body().toString());
                    }
                    searchedProducts.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}