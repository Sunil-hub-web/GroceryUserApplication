package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Helpers.Validator;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.Customer;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationOTPActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText otp;
    private TextView err;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_o_t_p);

        init();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar);
        if(getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Verify Phone");
        }

        err = findViewById(R.id.errorMsg);
        otp = findViewById(R.id.otp);
    }

    // verifying the OTP
    public void verifyOtp(View view){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setTitle("Verifying");
        dialog.setCancelable(false);

        err.setVisibility(View.GONE);
        if(new Validator().validInput(otp)){
            dialog.create();
            dialog.show();

            Customer customer = new Customer();
            customer.setCountryCode(getIntent().getIntExtra("countryCode",1));
            customer.setMobile(getIntent().getLongExtra("mobile",1));
            customer.setOtp(Integer.parseInt(otp.getText().toString().trim()));

            Call<ApiResponse> call = new UserApiToJsonHandler().verifyPhoneOtp(customer);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if(response.isSuccessful()){
                        // saving shared prefs
                        dialog.dismiss();
                        saveSharedPrefs(response.body().getToken());
                        startActivity(new Intent(RegistrationOTPActivity.this, MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }else{
                        dialog.dismiss();
                        Gson gson = new Gson();
                        ApiResponse message = gson.fromJson(response.errorBody().charStream(),ApiResponse.class);
                        showError(message.getMsg());
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    dialog.dismiss();
                    showError(t.getMessage());
                }
            });
        }else{
            Toast.makeText(this, "Enter OTP in the field", Toast.LENGTH_SHORT).show();
        }
    }

    private void showError(String message){
        err.setText(message);
        err.setVisibility(View.VISIBLE);
    }

    // save data to shared prefs
    private void saveSharedPrefs(String token){
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", getIntent().getStringExtra("name"));
        editor.putString("token", token);
        editor.putBoolean("exist", true);

        editor.apply();
    }

    public void signIn(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}