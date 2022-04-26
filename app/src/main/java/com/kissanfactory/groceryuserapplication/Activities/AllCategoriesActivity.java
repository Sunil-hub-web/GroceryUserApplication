package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Adapters.AllCategoriesAdapter;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.Category;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCategoriesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView allCategories;
    private List<Category> categoriesList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);

        init();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("All Categories");
        }
        getAllCategories();
    }



    private void getAllCategories(){
        Call<ApiResponse> call = new UserApiToJsonHandler().getCategories();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    categoriesList = response.body().getData();
                    showAllCategories(categoriesList);
                    Log.d("Categories", response.body().getData().toString());
                }else{
                    Toast.makeText(AllCategoriesActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(AllCategoriesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAllCategories(List<Category> catList){
        AllCategoriesAdapter adapter = new AllCategoriesAdapter(this, catList);

        allCategories = findViewById(R.id.allCategories);

        allCategories.setAdapter(adapter);
        allCategories.setOnItemClickListener((adapterView, view, i, l) -> startActivity(new Intent(AllCategoriesActivity.this, CategoryDetailActivity.class)
        .putExtra("category", catList.get(i).getId())
        .putExtra("name", catList.get(i).getName())));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}