package com.kissanfactory.groceryuserapplication.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.Payment_Checkout_status_pojo;
import com.kissanfactory.groceryuserapplication.Models.payment_add_order_pojo.PaymentAddOrderDto;
import com.kissanfactory.groceryuserapplication.Models.paymet_check_status.PaymentCheckStatusDto;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.UserSingleton;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Razzorpy_Payment_Activity extends AppCompatActivity implements PaymentResultWithDataListener {
    TextView tv_pay;
    private Toolbar toolbar;
    private static final String TAG = Razzorpy_Payment_Activity.class.getSimpleName();
    private String addressId = "", token = "", vFullname = "", vCartid = "", vPayment_ID = "", vOrder_Id = "", vAmount = "", vRazorpay_payment_id = "";
    UserSingleton userSingleton;
    int vAmount_f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razzorpay_payment);

        Checkout.preload(getApplicationContext());
        init();
        startPayment();

    }

    private void init() {

        toolbar = findViewById(R.id.toolbar);
        tv_pay = findViewById(R.id.tv_pay);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        if (getIntent() != null) {

            vPayment_ID = getIntent().getStringExtra("payment_id");
            addressId = getIntent().getStringExtra("address_id");
            vOrder_Id = getIntent().getStringExtra("razorpay_order_id");
            vFullname = getIntent().getStringExtra("Fullname");
            vAmount = getIntent().getStringExtra("Amount");

            System.out.println("aadadd..." + vAmount + "\n" + vOrder_Id + "\n" + addressId);
            userSingleton = UserSingleton.getInstance();
            vAmount_f = Math.round(Float.parseFloat(vAmount) * 100);

            Log.d("sunilamount", ""+String.valueOf(vAmount_f));

        }

        tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Razzorpy_Payment_Activity.this, MainActivity.class);
                startActivity(intent);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();

            }
        });
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Payment");
        }


    }

    public void startPayment() {

        final Activity activity = this;

        Checkout checkout = new Checkout();

       // checkout.setKeyID("rzp_test_c7QnPWINfG4tCC");
        checkout.setKeyID("rzp_live_8QwxwLMAETEiyG");

        checkout.setImage(R.drawable.splash_image);

        try {
            JSONObject options = new JSONObject();

            options.put("name", vFullname);
            options.put("description", "Charges");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
           // options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", vAmount_f);
            //options.put("amount", 500);

            JSONObject preFill = new JSONObject();

            preFill.put("email", userSingleton.getEmail());
            preFill.put("contact", userSingleton.getMobile());
            options.put("prefill", preFill);

            JSONObject retryObj = new JSONObject();

            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }
    }
//        try {
//            JSONObject options = new JSONObject();
//
//            options.put("name", "Merchant Name");
//            options.put("description", "Charges");
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", vOrder_Id);//from response of step 3.
//            options.put("theme.color", "#3399cc");
//            options.put("currency", "INR");
//            options.put("amount", vAmount);//pass amount in currency subunits
//            options.put("prefill.email", userSingleton.getEmail());
//            options.put("prefill.contact", userSingleton.getMobile());
//            JSONObject retryObj = new JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);
//
//            checkout.open(activity, options);
//
//        } catch (Exception e) {
//            Log.e(TAG, "Error in starting Razorpay Checkout", e);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_action_menu, menu);
        menu.getItem(0).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        } else if (item.getItemId() == R.id.db_notification) {
            startActivity(new Intent(this, NotificationActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        String signature = paymentData.getSignature();
        String pay_id = paymentData.getPaymentId();


        // Toast.makeText(this, s.toString(), Toast.LENGTH_SHORT).show();
        System.out.println("res....." + s.toString());

        vRazorpay_payment_id = s.toString();
        payment_check_status(signature);

        /*Intent intent = new Intent(Razzorpy_Payment_Activity.this,CreateOrderActivity.class);
        startActivity(intent);*/


    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(Razzorpy_Payment_Activity.this, s.toString(), Toast.LENGTH_SHORT).show();
        Log.e("error....", s.toString());
//        Intent intent = new Intent(Razzorpy_Payment_Activity.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
    }

    private void payment_check_status(String signature) {

/*payment_id: "6225f4590f44047c15db909f"
 razorpay_order_id: "order_J4By2i7IVznezY"
 razorpay_payment_id: "pay_J4C4lMmuIL3KE3"
 razorpay_signature: "617144098bd6781a7fe1905f0040716e397b8b1289417bf4f05d4fb923fc0252"*/
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
        Payment_Checkout_status_pojo payment_checkout_status_pojo = new Payment_Checkout_status_pojo();
        payment_checkout_status_pojo.setPayment_id(vPayment_ID);
        payment_checkout_status_pojo.setRazorpay_order_id(vOrder_Id);
        payment_checkout_status_pojo.setRazorpay_payment_id(vRazorpay_payment_id);
        payment_checkout_status_pojo.setRazorpay_signature(signature);

        Call<PaymentCheckStatusDto> call = new UserApiToJsonHandler().api_payment_checkout_status(token, payment_checkout_status_pojo);
        call.enqueue(new Callback<PaymentCheckStatusDto>() {
            @Override
            public void onResponse(Call<PaymentCheckStatusDto> call, Response<PaymentCheckStatusDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Razzorpy_Payment_Activity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    String VCartid = response.body().getPayment().getCartId();
                    String vPaymedhod = response.body().getPayment().getPaymentStatus().getWallet();

                    payment_add_order(VCartid, vPaymedhod, signature);
                }
            }

            @Override
            public void onFailure(Call<PaymentCheckStatusDto> call, Throwable t) {
                Toast.makeText(Razzorpy_Payment_Activity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void payment_add_order(String vCartid, String vPaymedhod, String signature) {

//        {
//            "razorpay_payment_id": "pay_J4CBWlUpaZOt2r",
//                "razorpay_order_id": "order_J4CBPWSplYyk98",
//                "payment_id": "6225f7500f44047c15db90aa",
//                "razorpay_signature": "5c03fc4079b7255adf782ec2d8b6a670bcd1fc20f73548b3b0a9af2f865e6ff3",
//                "cartId": "6225f6dc9b08928dcb9af4d7",
//                "AddressId": "614f2facaf042e9f6b3e4697",
//                "paymentMethod":"wallet"
//        }
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
        Payment_Checkout_status_pojo payment_checkout_status_pojo = new Payment_Checkout_status_pojo();
        payment_checkout_status_pojo.setPayment_id(vPayment_ID);
        payment_checkout_status_pojo.setRazorpay_order_id(vOrder_Id);
        payment_checkout_status_pojo.setRazorpay_payment_id(vRazorpay_payment_id);
        payment_checkout_status_pojo.setRazorpay_signature(signature);
        payment_checkout_status_pojo.setCartId(vCartid);
        payment_checkout_status_pojo.setAddressId(addressId);
        payment_checkout_status_pojo.setPaymentMethod(vPaymedhod);


        Call<PaymentAddOrderDto> call = new UserApiToJsonHandler().api_payment_add_order(token, payment_checkout_status_pojo);
        call.enqueue(new Callback<PaymentAddOrderDto>() {
            @Override
            public void onResponse(Call<PaymentAddOrderDto> call, Response<PaymentAddOrderDto> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(Razzorpy_Payment_Activity.this, "Payment Done", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Razzorpy_Payment_Activity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PaymentAddOrderDto> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }


}