package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.kissanfactory.groceryuserapplication.Adapters.FavouritesAdapter;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView favouritesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        init();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar);
        favouritesView = findViewById(R.id.favouriteItems);

        if (getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("My Favourites");
        }

        fillFavourites();
    }

    private void fillFavourites(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Call<ApiResponse> call = new UserApiToJsonHandler().getFavourites(token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    FavouritesAdapter adapter = new FavouritesAdapter(FavoritesActivity.this);

                    if(response.body().getFavourite().size()>=1 && response.body().getFavourite().get(0).getProductId()!=null){
                        adapter.setFavouriteList(response.body().getFavourite());
                    }else{
                        adapter.setFavouriteList(new ArrayList<>());
                    }

                    adapter.setOnItemClickListener(position -> {
                        ProgressDialog dialog = new ProgressDialog(FavoritesActivity.this);
                        dialog.setCancelable(false);
                        dialog.setMessage("Please Wait");
                        dialog.setTitle("Deleting from favourites");
                        dialog.create();
                        dialog.show();

                        SharedPreferences sharedPreferences = FavoritesActivity.this.getSharedPreferences("UserData", MODE_PRIVATE);
                        String token = sharedPreferences.getString("token", "");
                        Call<ApiResponse> call1 = new UserApiToJsonHandler().deleteFavourite(token, response.body().getFavourite().get(position).getId());
                        call1.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                dialog.dismiss();
                                if (response.isSuccessful()){
                                    fillFavourites();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                dialog.dismiss();
                            }
                        });
                    });
                    favouritesView.setAdapter(adapter);
                    favouritesView.setLayoutManager(new LinearLayoutManager(FavoritesActivity.this));
                }else{

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_action_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }else if (item.getItemId()==R.id.db_cart){
            startActivity(new Intent(this, CartActivity.class));
        }else if (item.getItemId()==R.id.db_notification){
            startActivity(new Intent(this, NotificationActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}