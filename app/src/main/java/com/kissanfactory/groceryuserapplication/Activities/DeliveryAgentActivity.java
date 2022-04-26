package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Models.DeliveryAgentResponse;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryAgentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    TextView nameTv, mobileTv;
    private String orderId, token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_agent);
        init();
    }

    private void init(){
        orderId = getIntent().getStringExtra("orderId");
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        toolbar = findViewById(R.id.toolbar);
        nameTv = findViewById(R.id.driverNameTv);
        mobileTv = findViewById(R.id.driverMobileTv);
        if (getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Delivery Agent");
        }

        getAgentDetails();
    }

    // get agent details
    private void getAgentDetails(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");
        dialog.create();
        dialog.show();

        Call<DeliveryAgentResponse> call = new UserApiToJsonHandler().getAgent(token, orderId);
        call.enqueue(new Callback<DeliveryAgentResponse>() {
            @Override
            public void onResponse(Call<DeliveryAgentResponse> call, Response<DeliveryAgentResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    if (response.body().getData()!=null){
                        nameTv.setText(response.body().getData().getName());
                        mobileTv.setText(response.body().getData().getMobile() + " - "+response.body().getData().getMobile());
                    }else{
                        Toast.makeText(DeliveryAgentActivity.this, "No driver selected", Toast.LENGTH_SHORT).show();
                        DeliveryAgentActivity.this.finish();
                    }

                }else{
                    Toast.makeText(DeliveryAgentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeliveryAgentResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(DeliveryAgentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}