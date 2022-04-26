package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Helpers.EditTextToClassConverter;
import com.kissanfactory.groceryuserapplication.Helpers.Validator;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.Customer;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CountryCodePicker countryCodePicker;
    private EditText name, password, email, confPassword, phone;
    private CheckBox terms;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
    }

    // initializing the views
    private void init(){
        // initializing the toolbar
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.tb_title);
        title.setText("Create New Account");

        if(getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        name = findViewById(R.id.userName);
        password = findViewById(R.id.userPassword);
        confPassword = findViewById(R.id.userConfPassword);
        email = findViewById(R.id.userEmail);
        phone = findViewById(R.id.userPhone);
        errorMessage = findViewById(R.id.errorMsg);
        terms = findViewById(R.id.termsCheckBox);
        countryCodePicker = findViewById(R.id.countryCodePicker);
    }

    // redirect to sign in page
    public void signIn(View view){
        if(getIntent().getStringExtra("login")!=null){
            super.onBackPressed();
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    // registering the user
    public void registerUser(View view){

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Creating account");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        errorMessage.setVisibility(View.GONE);
        Validator validator = new Validator();
        EditTextToClassConverter customerConverter = new EditTextToClassConverter();
        int countryCode = Integer.parseInt(countryCodePicker.getSelectedCountryCode());

        if (validator.validInputs(name, email, password, confPassword, phone)){
            if(validator.validEmail(email)){
                if (validator.passwordConfirmation(password, confPassword)){
                    if(terms.isChecked()){
                        dialog.create();
                        dialog.show();
                        // create the customer object
                        Customer customer = customerConverter.toCustomer(name, email, password, countryCode, phone);
                        Log.i("Customer: ", customer.toString());
                        Call<ApiResponse> call = new UserApiToJsonHandler().registerCustomer(customer);
                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                Log.d( "Api ", "onResponse: "+response );
                                if (response.isSuccessful()){

                                    dialog.dismiss();
                                    //startActivity(new Intent(RegisterActivity.this, RegistrationOTPActivity.class)
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class)
                                    .putExtra("mobile", Long.parseLong(phone.getText().toString()))
                                    .putExtra("countryCode", countryCode)
                                    .putExtra("name", name.getText().toString().trim()));
                                    RegisterActivity.this.finish();
                                }else{
                                    dialog.dismiss();
                                    Gson gson = new Gson();
                                    ApiResponse message = gson.fromJson(response.errorBody().charStream(),ApiResponse.class);
                                    Toast.makeText(RegisterActivity.this, message.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                dialog.dismiss();
                                showErrorMessage(t.getMessage());
                            }
                        });
                    }else{
                        showErrorMessage("Accept Privacy Policy");
                    }
                }else{
                    // invalid password confirmation
                    showErrorMessage("Password does not match");
                }
            }else{
                // invalid email
                showErrorMessage("Invalid Email Address");
            }
        }else{
            // all fields not filled
            showErrorMessage("Please Fill all Fields");
        }
    }

    private void showErrorMessage(String error){
        errorMessage.setText(error);
        errorMessage.setVisibility(View.VISIBLE);
    }


    // redirect to privacy policy
    public void privacyPolicy(View view){
        startActivity(new Intent(this, PrivacyPolicyActivity.class));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}