package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.UserSingleton;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView name, email, phone, err;
    Button bt_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }

    private void init() {
        name = findViewById(R.id.userName);
        email = findViewById(R.id.userEmail);
        phone = findViewById(R.id.userPhone);
        err = findViewById(R.id.errorMsg);
        toolbar = findViewById(R.id.toolbar);

        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("My Profile");
        }

        getUserProfile();
    }

    // getting the user profile from the server
    private void getUserProfile() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Fetching data");
        dialog.setMessage("Please wait...");
        dialog.create();
        dialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

//if(token.isEmpty())
//{
//    token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MTAwNDE4NTk4NmI4MTkwYjk1ZmY4MGYiLCJleHAiOjE2MzI1OTA3NjUsImlhdCI6MTYyNzQwNjc2NX0.JYIjbvpMmlWvKpYrpRK9obhe3GXk2smZ2OiyFlbaqDo";
//}

        // check the singleton


        UserSingleton user = UserSingleton.getInstance();
        if (user.getName() == null) {
            // get from API
            Call<ApiResponse> call = new UserApiToJsonHandler().customerProfile(token);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        name.setText(response.body().getUserData().getName());
                        email.setText(response.body().getUserData().getEmailID());
                        phone.setText(String.valueOf(response.body().getUserData().getMobile()));
                        // fill singleton
                        user.setEmail(response.body().getUserData().getEmailID());
                        user.setMobile(response.body().getUserData().getMobile());
                        user.setName(response.body().getUserData().getName());
                    } else {
                        showError("Something went wrong, try again later");
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    dialog.dismiss();
                    showError(t.getMessage());
                }
            });
        } else {
            // fill from singleton
            name.setText(user.getName());
            email.setText(user.getEmail());
            phone.setText(String.valueOf(user.getMobile()));
            dialog.dismiss();
        }
    }

    private void getUpdateUserProfile() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Fetching data");
        dialog.setMessage("Please wait...");
        dialog.create();
        dialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

//if(token.isEmpty())
//{
//    token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2MTAwNDE4NTk4NmI4MTkwYjk1ZmY4MGYiLCJleHAiOjE2MzI1OTA3NjUsImlhdCI6MTYyNzQwNjc2NX0.JYIjbvpMmlWvKpYrpRK9obhe3GXk2smZ2OiyFlbaqDo";
//}
        // check the singleton


        UserSingleton user = UserSingleton.getInstance();
        if (user.getName() == null) {
            // get from API
            Call<ApiResponse> call = new UserApiToJsonHandler().customerProfile(token);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        name.setText(response.body().getUserData().getName());
                        email.setText(response.body().getUserData().getEmailID());
                        phone.setText(String.valueOf(response.body().getUserData().getMobile()));
                        // fill singleton
                        user.setEmail(response.body().getUserData().getEmailID());
                        user.setMobile(response.body().getUserData().getMobile());
                        user.setName(response.body().getUserData().getName());
                    } else {
                        showError("Something went wrong, try again later");
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    dialog.dismiss();
                    showError(t.getMessage());
                }
            });
        } else {
            // fill from singleton
            name.setText(user.getName());
            email.setText(user.getEmail());
            phone.setText(String.valueOf(user.getMobile()));
            dialog.dismiss();
        }
    }

    private void showError(String message) {
        err.setText(message);
        err.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            startActivity(new Intent(this, MainActivity.class));
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}