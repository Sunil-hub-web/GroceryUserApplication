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
import com.kissanfactory.groceryuserapplication.Models.Password;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText password1, password2;
    private TextView err;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
    }

    // initializing the view
    private void init(){
        // initializing the custom toolbar
        toolbar = findViewById(R.id.toolbar);

        if(getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // setting up the toolbar title
            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Change Password");
        }

        err = findViewById(R.id.errorMsg);
        password1 = findViewById(R.id.userPassword);
        password2 = findViewById(R.id.userConfPassword);
    }

    // change the password using the route
    public void changePassword(View view){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait..");
        dialog.setTitle("Updating Password");
        dialog.setCancelable(false);

        err.setVisibility(View.GONE);
        if (new Validator().validInputs(password1, password2)){
            if (new Validator().passwordConfirmation(password1, password2)){
                dialog.create();
                dialog.show();

                String pass1 = password1.getText().toString().trim();
                String pass2 = password2.getText().toString().trim();
                String token = getIntent().getStringExtra("token");

                Password newPassword = new Password();
                newPassword.setPassword1(pass1);
                newPassword.setPassword2(pass2);
                newPassword.setMobile(getIntent().getLongExtra("phone",1));

                Call<ApiResponse> call = new UserApiToJsonHandler().changePassword(newPassword, token);
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()){
                            dialog.dismiss();
                            // show some confirmation here
                            startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
                            ChangePasswordActivity.this.finish();
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
                showError("Passwords do not match");
            }
        }else{
            showError("Fill all fields");
        }

    }

    private void showError(String error){
        err.setText(error);
        err.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}