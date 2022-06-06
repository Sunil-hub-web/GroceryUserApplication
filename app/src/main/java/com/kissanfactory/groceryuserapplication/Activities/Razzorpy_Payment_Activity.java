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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.Payment_Checkout_status_pojo;
import com.kissanfactory.groceryuserapplication.Models.payment_add_order_pojo.PaymentAddOrderDto;
import com.kissanfactory.groceryuserapplication.Models.paymet_check_status.PaymentCheckStatusDto;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.UserSingleton;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Razzorpy_Payment_Activity extends AppCompatActivity implements PaymentResultWithDataListener{
    TextView tv_pay;
    private Toolbar toolbar;
    private static final String TAG = Razzorpy_Payment_Activity.class.getSimpleName();
    private String addressId = "", token = "", vFullname = "", vCartid = "", vPayment_ID = "", vOrder_Id = "", vAmount = "", vRazorpay_payment_id = "",
            signature,pay_id;
    UserSingleton userSingleton;
    int vAmount_f;
    TextView showalldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razzorpay_payment);

        showalldata = findViewById(R.id.showalldata);

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        vOrder_Id = getIntent().getStringExtra("orderid");

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

            //vPayment_ID = getIntent().getStringExtra("payment_id");
            addressId = getIntent().getStringExtra("address_id");
            //vOrder_Id = getIntent().getStringExtra("razorpay_order_id");
            vFullname = getIntent().getStringExtra("Fullname");
            vAmount = getIntent().getStringExtra("Amount");
            vCartid = getIntent().getStringExtra("cart_id");
            //vOrder_Id = getIntent().getStringExtra("order_id");
            vPayment_ID = getIntent().getStringExtra("payment_id");

            System.out.println("aadadd..." + vAmount + "\n" + vOrder_Id + "\n" + addressId);

            userSingleton = UserSingleton.getInstance();
            vAmount_f = Math.round(Float.parseFloat(vAmount) * 100);

            Log.d("sunilamount", ""+String.valueOf(vAmount_f));

            String Alldata =  vAmount + "\n" + vOrder_Id + "\n" + addressId + "\n" + vCartid + "\n" + vPayment_ID;

           // Log.d("sunilresponse",vAmount + "\n" + vOrder_Id + "\n" + addressId + "\n" + vCartid + "\n" + vPayment_ID);

            showalldata.setText(Alldata);

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

        //checkout.setKeyID("rzp_test_c7QnPWINfG4tCC");
        checkout.setKeyID("rzp_live_8QwxwLMAETEiyG");

        checkout.setImage(R.drawable.play);

        try {
            JSONObject options = new JSONObject();

            options.put("name", vFullname);
            options.put("description", "Charges");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            options.put("order_id", vOrder_Id);
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

            Log.d("sunilerror",e.getMessage());
            showalldata.setText(e.getMessage());
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

        signature = paymentData.getSignature();
        pay_id = paymentData.getPaymentId();

        // Toast.makeText(this, s.toString(), Toast.LENGTH_SHORT).show();
        System.out.println("res....." + s.toString());

        vRazorpay_payment_id = s.toString();

        Log.d("sunilresponsedata",paymentData.getData().toString());

        Toast.makeText(this, signature, Toast.LENGTH_SHORT).show();

        String alldata = signature +" , "+pay_id+" , "+vRazorpay_payment_id+" , "+vOrder_Id;

        showalldata.setText(alldata);

        Log.d("sunildataall",vPayment_ID+","+vOrder_Id+","+vRazorpay_payment_id);

        payment_history_checkstatus(vPayment_ID,vOrder_Id,vRazorpay_payment_id,"5c03fc4079b7255adf782ec2d8b6a670bcd1fc20f73548b3b0a9af2f865e6ff3");

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(Razzorpy_Payment_Activity.this, s.toString(), Toast.LENGTH_SHORT).show();
        Log.d("error....", s.toString());

//        Intent intent = new Intent(Razzorpy_Payment_Activity.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();

        Log.d("sunilresponse",vAmount + "\n" + vOrder_Id + "\n" + addressId + "\n" + vCartid + "\n" + vPayment_ID);

        showalldata.setText(s);
    }

/*

    private void payment_check_status(String signature) {

//payment_id: "6225f4590f44047c15db909f"
// razorpay_order_id: "order_J4By2i7IVznezY"
// razorpay_payment_id: "pay_J4C4lMmuIL3KE3"
// razorpay_signature: "617144098bd6781a7fe1905f0040716e397b8b1289417bf4f05d4fb923fc0252"

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

                    Intent intent = new Intent(Razzorpy_Payment_Activity.this, CreateOrderActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<PaymentAddOrderDto> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

*/

    public void payment_history_create(String FirstName,long amount,String cartId){

        String url = "https://kisaanandfactory.com/api/v1/userapp/payment/payment_history/create";

        ProgressDialog progressDialog = new ProgressDialog(Razzorpy_Payment_Activity.this);
        progressDialog.setMessage("payment history create");
        progressDialog.show();

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("FirstName",FirstName);
            jsonObject.put("amount",amount);
            jsonObject.put("cartId",cartId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();
                try {

                    String code = response.getString("code");
                    String msg = response.getString("msg");
                    String payment = response.getString("payment");
                    String data = response.getString("data");

                    if(code.equals("200")){

                        Toast.makeText(Razzorpy_Payment_Activity.this, msg, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject_payment = new JSONObject(payment);

                        String payment_id = jsonObject_payment.getString("_id");
                       /* String user_id = jsonObject_payment.getString("user_id");
                        String cart_id = jsonObject_payment.getString("cart_id");
                        String amount = jsonObject_payment.getString("amount");*/

                        JSONObject jsonObject_data = new JSONObject(data);

                        String order_id = jsonObject_data.getString("id");
                       /* String entity = jsonObject_payment.getString("entity");
                        String amount_paid = jsonObject_payment.getString("amount_paid");
                        String amount_due = jsonObject_payment.getString("amount_due");*/

                        payment_history_checkstatus(payment_id,order_id,pay_id,signature);

                    }else{

                        Toast.makeText(Razzorpy_Payment_Activity.this, "payment history not create", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("onErrorResponse", error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    Log.d("successresponceVolley", "" + error.networkResponse);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(Razzorpy_Payment_Activity.this, data, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }

                    }

                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> header = new HashMap<>();
                header.put("auth-token",token);
                return header;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Razzorpy_Payment_Activity.this);
        requestQueue.add(jsonObjectRequest);

    }

    public void payment_history_checkstatus(String payment_id,String razorpay_order_id,String razorpay_payment_id,String razorpay_signature){

        ProgressDialog progressDialog = new ProgressDialog(Razzorpy_Payment_Activity.this);
        progressDialog.setMessage("payment history checkstatus");
        progressDialog.show();

        String url = "https://kisaanandfactory.com/api/v1/userapp/payment/payment_history/checkstatus";

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("payment_id",payment_id);
            jsonObject.put("razorpay_order_id",razorpay_order_id);
            jsonObject.put("razorpay_payment_id",razorpay_payment_id);
            jsonObject.put("razorpay_signature",razorpay_signature);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("allparameters",jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();

                Log.d("sunilresponse",response.toString());

                try {

                    String code = response.getString("code");
                    String msg = response.getString("msg");

                    if(code.equals("200")){

                        Toast.makeText(Razzorpy_Payment_Activity.this, "Payment Done", Toast.LENGTH_SHORT).show();

                        payment_history_checkstatus_add_order(razorpay_payment_id,razorpay_order_id,payment_id,signature,vCartid,addressId,"wallet");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("onErrorResponse", error.toString());

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    Log.d("successresponceVolley", "" + error.networkResponse);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(Razzorpy_Payment_Activity.this, data, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }

                    }

                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> header = new HashMap<>();
                header.put("auth-token",token);
                return header;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Razzorpy_Payment_Activity.this);
        requestQueue.add(jsonObjectRequest);

    }

    public void payment_history_checkstatus_add_order(String razorpay_payment_id,String razorpay_order_id,String payment_id,String razorpay_signature,
                                                      String cartId,String AddressId,String paymentMethod){

        ProgressDialog progressDialog = new ProgressDialog(Razzorpy_Payment_Activity.this);
        progressDialog.setMessage("add orderdetails");
        progressDialog.show();

        String url = "https://kisaanandfactory.com/api/v1/userapp/payment/payment_history/checkstatus-add-order";

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("razorpay_payment_id",razorpay_payment_id);
            jsonObject.put("razorpay_order_id",razorpay_order_id);
            jsonObject.put("payment_id",payment_id);
            jsonObject.put("razorpay_signature",razorpay_signature);
            jsonObject.put("cartId",cartId);
            jsonObject.put("AddressId",AddressId);
            jsonObject.put("paymentMethod",paymentMethod);

            String Alldata =  razorpay_payment_id + "\n" + razorpay_order_id + "\n" + payment_id + "\n" + razorpay_signature + "\n" + cartId + "\n" + AddressId + "\n" + paymentMethod;
            showalldata.setText(Alldata);

            Log.d("sunilallresponse",razorpay_payment_id + "\n" + razorpay_order_id + "\n" + payment_id + "\n" + razorpay_signature + "\n" + cartId + "\n" + AddressId + "\n" + paymentMethod);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("allparameters",jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();

                Log.d("sunilresponse",response.toString());

                try {

                    String code = response.getString("code");
                    String msg = response.getString("msg");

                    if(code.equals("200")){

                        Toast.makeText(Razzorpy_Payment_Activity.this, "Data Add SuccessFully", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("onErrorResponse", error.toString());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "Please check Internet Connection", Toast.LENGTH_SHORT).show();

                } else {

                    Log.d("successresponceVolley", "" + error.networkResponse.statusCode);
                    Log.d("successresponceVolley", "" + error.networkResponse);
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {
                        try {
                            String jError = new String(networkResponse.data);
                            JSONObject jsonError = new JSONObject(jError);

                            String data = jsonError.getString("msg");
                            Toast.makeText(Razzorpy_Payment_Activity.this, data, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("successresponceVolley", "" + e);
                        }

                    }

                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String,String> header = new HashMap<>();
                header.put("auth-token",token);
                return header;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Razzorpy_Payment_Activity.this);
        requestQueue.add(jsonObjectRequest);

    }

}