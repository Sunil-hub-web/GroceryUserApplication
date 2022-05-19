package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Helpers.Validator;
import com.kissanfactory.groceryuserapplication.Models.Address;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.NoMsgResponse;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText houseNo, streetNo, city, state, locality, country, zip;
    private TextView err;
    private Button addAddressBtn;
    private Address address;
    private boolean updated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        init();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Add Address");
        }
        // initializing the error view
        err = findViewById(R.id.errorMsg);
        // initializing the edit texts
        houseNo = findViewById(R.id.userHouseNo);
        state = findViewById(R.id.userState);
        streetNo = findViewById(R.id.userStreetNo);
        city = findViewById(R.id.userCity);
        locality = findViewById(R.id.userLocality);
        zip = findViewById(R.id.userZip);
        country = findViewById(R.id.userCountry);
        addAddressBtn = findViewById(R.id.addAddressBtn);
        addAddressBtn.setOnClickListener(view -> createNewAddress());

        double lat = getIntent().getDoubleExtra("lat",0);
        double longi = getIntent().getDoubleExtra("long", 0);

        Log.d("latlngsunil",String.valueOf(lat));

        if(getIntent().getDoubleExtra("lat",0)>0){

            getLocationFromMap();

        }else{

            getEditInfo();
        }
    }

    // get location from latlng
    private void getLocationFromMap(){

        Intent data = getIntent();
        Geocoder geocoder;
        List<android.location.Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        double lat = data.getDoubleExtra("lat",0);
        double longi = data.getDoubleExtra("long", 0);

        Log.d("latlngsunil",String.valueOf(lat));
        try {
            addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String locality = addresses.get(0).getLocality();
        String street = addresses.get(0).getSubAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        Toast.makeText(this,(city+state+country+address+knownName+postalCode), Toast.LENGTH_SHORT).show();
        Log.e("PLace", addresses.get(0).toString());

        // set the texts
        zip.setText((postalCode!=null) ? postalCode : "110001");
        this.state.setText(state);
        this.houseNo.setText(knownName);
        this.locality.setText(locality);
        this.city.setText(city);
        this.country.setText(country);
        this.streetNo.setText(street);


    }

    // checking if its edit page
    private void getEditInfo(){

        Toast.makeText(this, "getEditInfo", Toast.LENGTH_SHORT).show();

        address = new Address();
        try {
            Address address = getIntent().getParcelableExtra("address");

            zip.setText(address.getZip());
            country.setText(address.getCountry());
            streetNo.setText(address.getStreet());
            locality.setText(address.getLocality());
            state.setText(address.getState());
            houseNo.setText(address.getHouse());
            city.setText(address.getCity());
            this.address = address;

            Toast.makeText(this,(address.getZip()+""+address.getCountry()), Toast.LENGTH_SHORT).show();


            Validator validator = new Validator();
            addAddressBtn.setText("Update Address");
            addAddressBtn.setOnClickListener(view -> {

                if (validator.validInputs(houseNo, city, state, streetNo, country)){
                    if (validator.validInputs(zip, locality)){
                        ProgressDialog dialog = new ProgressDialog(this);
                        dialog.setTitle("Editing Address");
                        dialog.setMessage("Please wait...");
                        dialog.setCancelable(false);
                        dialog.create();
                        dialog.show();
                        // update the address here
                        // create the address object
                        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                        String token = sharedPreferences.getString("token", "");
                        Call<ApiResponse> call = new UserApiToJsonHandler().editAddress(token, address.getId(), createAddress());
                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                dialog.dismiss();
                                if (response.isSuccessful()){
                                    updated = true;
                                    setResult(RESULT_OK);
                                    Toast.makeText(AddAddressActivity.this, "Address updated successfully!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Gson gson = new Gson();
                                    ApiResponse message = gson.fromJson(response.errorBody().charStream(),ApiResponse.class);
                                    Toast.makeText(AddAddressActivity.this, message.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                dialog.dismiss();
                                Toast.makeText(AddAddressActivity.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        showError("Fill all Fields");
                    }
                }else{
                    showError("Fill all Fields");
                }

            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
//        if (!updated){
//            setResult(RESULT_CANCELED);
//        }else{
//            setResult(RESULT_OK);
//        }
        super.onBackPressed();
    }

    // adding the new address
    private void createNewAddress(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Creating New Address");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.create();
        err.setVisibility(View.GONE);
        Validator validator = new Validator();

        // getting the token
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");


        if (validator.validInputs(houseNo, city, state, streetNo, country)){
            if(validator.validInputs(zip, locality)){
                dialog.show();
                // do some stuff with the backend
                Call<NoMsgResponse> call = new UserApiToJsonHandler().addAddress(createAddress(),token);
                call.enqueue(new Callback<NoMsgResponse>() {
                    @Override
                    public void onResponse(Call<NoMsgResponse> call, Response<NoMsgResponse> response) {
                        if (response.isSuccessful()){
                            // go back to all address activity
                            dialog.dismiss();
                            setResult(RESULT_OK);
                            AddAddressActivity.this.onBackPressed();
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
                showError("Fill all fields");
            }
        }else{
            showError("Fill all fields");
        }
    }

    // creating the address object
    private Address createAddress(){

        Address address = new Address();

        address.setCity(editToString(city));
        address.setCountry(editToString(country));
        address.setHouse(editToString(houseNo));
        address.setLocality(editToString(locality));
        address.setZip(Integer.parseInt(editToString(zip)));
        address.setState(editToString(state));
        address.setStreet(editToString(streetNo));

        if (getIntent().getDoubleExtra("lat",0)>0){
            // get lat and long from map
            address.setLatitude(getIntent().getDoubleExtra("lat",0));
            address.setLongitude(getIntent().getDoubleExtra("long",0));

        }else{
            // get lat and long from the existing address
            Address oldAddress = getIntent().getParcelableExtra("address");
            address.setLatitude(oldAddress.getLatitude());
            address.setLongitude(oldAddress.getLongitude());
        }

        return address;
    }

    private String editToString(EditText input){
        return input.getText().toString().trim();
    }

    // showing the error message
    private void showError(String message){
        err.setText(message);
        err.setVisibility(View.VISIBLE);
    }

    // delete address
    private void deleteAddress(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Call<ApiResponse> call = new UserApiToJsonHandler().deleteAddress(token, this.address.getId());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    setResult(RESULT_OK);
                    AddAddressActivity.this.onBackPressed();
                }else{
                    Toast.makeText(AddAddressActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(AddAddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIntent().getDoubleExtra("lat",0)==0){
            // inflate menu
            getMenuInflater().inflate(R.menu.address_action_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }else if (item.getItemId()==R.id.deleteAddress){
            // delete the address and go back
            Dialog mDialog = new Dialog(this);
            mDialog.setContentView(R.layout.dialog_log_out);
            mDialog.create();
            mDialog.show();
            mDialog.setCancelable(true);
            TextView title = mDialog.findViewById(R.id.dialogTitle);
            title.setText("Delete Address?");

            TextView cancelTv = mDialog.findViewById(R.id.tv_Cancel);
            cancelTv.setText("Cancel");
            cancelTv.setOnClickListener(v -> mDialog.dismiss());

            TextView actionTv = mDialog.findViewById(R.id.tv_Logout);
            actionTv.setText("Delete");
            actionTv.setOnClickListener(v -> {
                mDialog.dismiss();
                deleteAddress();
            });
        }
        return super.onOptionsItemSelected(item);
    }
}