package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kissanfactory.groceryuserapplication.Helpers.Validator;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.Customer;
import com.kissanfactory.groceryuserapplication.Models.NoMsgResponse;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText mobile;
    private TextView err;
    private CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        init();
    }

    // initializing the toolbar
    private void init(){
        toolbar = findViewById(R.id.toolbar);

        if(getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // setting up the toolbar title
            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Forget Password");
        }

        err = findViewById(R.id.errorMsg);
        mobile = findViewById(R.id.userPhone);
        countryCodePicker = findViewById(R.id.countryCodePicker);

    }

    // verify otp
    public void requestOtp(View view){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setTitle("Verifying");
        dialog.setCancelable(false);

        err.setVisibility(View.GONE);
        if(new Validator().validInput(mobile)){
            dialog.create();
            dialog.show();
            int cc = Integer.parseInt(countryCodePicker.getSelectedCountryCode());
            Customer customer = new Customer();
            customer.setMobile(Long.parseLong(mobile.getText().toString().trim()));
            customer.setCountryCode(cc);

            Call<NoMsgResponse> call = new UserApiToJsonHandler().forgotPasswordOtp(customer);
            call.enqueue(new Callback<NoMsgResponse>() {
                @Override
                public void onResponse(Call<NoMsgResponse> call, Response<NoMsgResponse> response) {
                    if (response.isSuccessful()){
                        dialog.dismiss();
                        startActivity(new Intent(ForgotPasswordActivity.this, ForgotPasswordOTPActivity.class)
                                .putExtra("countryCode", cc)
                                .putExtra("mobile", Long.parseLong(mobile.getText().toString().trim())));
                    }else{
                        dialog.dismiss();
                        Gson gson = new Gson();
                        ApiResponse message = gson.fromJson(response.errorBody().charStream(),ApiResponse.class);
                        showError(message.getMsg());
                    }
                }

                @Override
                public void onFailure(Call<NoMsgResponse> call, Throwable t) {
                    dialog.dismiss();
                    showError(t.getMessage());
                }
            });

        }else{
            showError("Enter valid number");
        }

    }

    private void showError(String error){
        err.setText(error);
        err.setVisibility(View.VISIBLE);
    }


    public void signUp(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    // configuring the back arrow functionality
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}