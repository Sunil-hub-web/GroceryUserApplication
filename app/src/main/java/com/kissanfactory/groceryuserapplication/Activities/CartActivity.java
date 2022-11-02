package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kissanfactory.groceryuserapplication.Adapters.CartAdapter;
import com.kissanfactory.groceryuserapplication.Models.Address;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.Cart;
import com.kissanfactory.groceryuserapplication.Models.CartItems;
import com.kissanfactory.groceryuserapplication.Models.Checkout_Create_Pojo;
import com.kissanfactory.groceryuserapplication.Models.CustomerData;
import com.kissanfactory.groceryuserapplication.Models.NoMsgResponse;
import com.kissanfactory.groceryuserapplication.Models.payment_create.PaymentCreateDto;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.UserSingleton;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.gson.Gson;
import com.kissanfactory.groceryuserapplication.session_manager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    public static CartActivity cartActivity;
    private Toolbar toolbar;
    private NestedScrollView container;
    private ProgressBar loading;
    private boolean hasDefault;

    private RecyclerView productsList;

    SessionManager sessionManager;
    List<Cart> cartList;

    // cart details
    private TextView total, discount, shipping, payable, noItemsTv;
    private String addressId = "", vFullname = "", vCartid = "", vPayment_ID = "", vOrder_Id = "";
    private double vAmount;
    // address text view
    private TextView houseStreet, localityCity, stateZip;
    private CustomerData customer = new CustomerData();
    private Button checkoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_new);
        cartActivity = this;
        sessionManager = new SessionManager(CartActivity.this);
        init();

    }

    private void init() {

        loading = findViewById(R.id.loading);
        container = findViewById(R.id.container);
        container.setVisibility(View.VISIBLE);

        houseStreet = findViewById(R.id.houseStreet);
        localityCity = findViewById(R.id.localityCity);
        stateZip = findViewById(R.id.stateZip);

        total = findViewById(R.id.total);
        discount = findViewById(R.id.discount);
        shipping = findViewById(R.id.shipping);
        payable = findViewById(R.id.payable);
        noItemsTv = findViewById(R.id.noItemsTv);
        checkoutBtn = findViewById(R.id.checkoutBtn);

        productsList = findViewById(R.id.cartItems);

        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Cart");
        }
        getUserInfo();
        getCart();


        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String payamount = String.valueOf(totalNum);
                int pay_amount = (int) totalNum;

                payment_history_create(vFullname,pay_amount,vCartid);
            }
        });
    }

    public void create_checkout() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.create();
        dialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        UserSingleton userSingleton = UserSingleton.getInstance();
        Checkout_Create_Pojo checkout_create_pojo = new Checkout_Create_Pojo();
        checkout_create_pojo.setFirstName(userSingleton.getName());
        //checkout_create_pojo.setAmount(300.0);
        checkout_create_pojo.setCartId(vCartid);


        Call<PaymentCreateDto> call = new UserApiToJsonHandler().api_create_checkout(token, checkout_create_pojo);
        call.enqueue(new Callback<PaymentCreateDto>() {
            @Override
            public void onResponse(Call<PaymentCreateDto> call, Response<PaymentCreateDto> response) {
                if (response.isSuccessful()) {

                    String payamount = String.valueOf(totalNum);

                    dialog.dismiss();
                    if (response.code() == 200) {
                        vPayment_ID = response.body().getPayment().getId() != null ? response.body().getPayment().getId() : "";
                        vOrder_Id = response.body().getData().getId() != null ? response.body().getData().getId() : "";

                        Intent intent = new Intent(CartActivity.this, Razzorpy_Payment_Activity.class);
                        intent.putExtra("Fullname", vFullname);
                        intent.putExtra("Amount", payamount);
                        intent.putExtra("address_id", addressId);
                        intent.putExtra("cart_id", vCartid);
                        startActivity(intent);
                        finish();

                        Log.d("finalamount", String.valueOf(vAmount));

                    } else {
                        Toast.makeText(CartActivity.this, response.body().getMsg().toString(), Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onFailure(Call<PaymentCreateDto> call, Throwable t) {

                dialog.dismiss();

                Toast.makeText(CartActivity.this, ""+t, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void payment_history_create(String FirstName,int amount,String cartId){

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        String url = "https://kisaanandfactory.com/api/v1/userapp/payment/payment_history/create";

        ProgressDialog progressDialog = new ProgressDialog(CartActivity.this);
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                progressDialog.dismiss();
                try {

                    String code = response.getString("code");
                    String msg = response.getString("msg");
                    String payment = response.getString("payment");
                    String data = response.getString("data");

                    if(code.equals("200")){

                        Toast.makeText(CartActivity.this, msg, Toast.LENGTH_SHORT).show();

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

                        //payment_history_checkstatus(payment_id,order_id,pay_id,signature);

                        String payamount = String.valueOf(totalNum);

                        Intent intent = new Intent(CartActivity.this, Razzorpy_Payment_Activity.class);
                        intent.putExtra("Fullname", vFullname);
                        intent.putExtra("Amount", payamount);
                        intent.putExtra("address_id", addressId);
                        intent.putExtra("cart_id", vCartid);
                        intent.putExtra("orderid", order_id);
                        intent.putExtra("payment_id", payment_id);
                        startActivity(intent);
                        finish();

                    }else{

                        Toast.makeText(CartActivity.this, "payment history not create", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
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
                            Toast.makeText(CartActivity.this, data, Toast.LENGTH_SHORT).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
        requestQueue.add(jsonObjectRequest);

    }

    // get profileinfo
    private void getUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Call<ApiResponse> call = new UserApiToJsonHandler().customerProfile(token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    customer = response.body().getUserData();
                    vFullname = customer.getName() != null ? customer.getName() : "";
                    System.out.println("name...." + vFullname);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    // get the user cart
    private void getCart() {
        ProgressDialog dialog = new ProgressDialog(CartActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait..");
        dialog.create();
        dialog.show();
        container.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Call<ApiResponse> call = new UserApiToJsonHandler().getCart(token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getCartItems() != null) {
                        showHideCart(true);
                        CartItems cartItems = response.body().getCartItems();
                        cartList = new ArrayList<>();
                        for (Cart cart : cartItems.getCart()) {
                            if (cart.getItem() != null) {
                                cartList.add(cart);
                            }
                        }
                        vCartid = cartItems._id != null ? cartItems._id : "";
                        System.out.println("Cart_id...." + vCartid);
                        getCartTotals(cartList);
                        CartAdapter adapter = new CartAdapter(CartActivity.this);

                        adapter.setCartList(cartList);
                        adapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
                            @Override
                            public void delete(int position) {
                                dialog.setTitle("Deleting");
                                dialog.setMessage("Please wait..");
                                dialog.create();
                                dialog.show();
                                Call<NoMsgResponse> deleteCall = new UserApiToJsonHandler().deleteFromCart(token, cartList.get(position).getItem().getId());
                                deleteCall.enqueue(new Callback<NoMsgResponse>() {
                                    @Override
                                    public void onResponse(Call<NoMsgResponse> call, Response<NoMsgResponse> response) {
                                        dialog.dismiss();
                                        if (response.isSuccessful()) {
                                            // delete it from the list
                                            getCart();
                                        } else {
                                            Log.e("Error", response.code() + "");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<NoMsgResponse> call, Throwable t) {
                                        dialog.dismiss();
                                        Log.e("Failure", t.getMessage());
                                    }
                                });
                            }

                            @Override
                            public void changeQTy(int position, boolean increase) {
                                Cart cart = new Cart();
                                if (increase) {
                                    cart.setQuantity(cartList.get(position).getQuantity() + 1);
                                    updateQuantity(cartList.get(position).getItem().getId(), token, cart);
                                } else {
                                    if (cartList.get(position).getQuantity() > 1) {
                                        cart.setQuantity(cartList.get(position).getQuantity() - 1);
                                        updateQuantity(cartList.get(position).getItem().getId(), token, cart);
                                    } else {
                                        delete(position);
                                    }
                                }

                            }
                        });

                        productsList.setAdapter(adapter);
                        productsList.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                    } else {
                        showHideCart(false);
                        Toast.makeText(CartActivity.this, "No items in cart", Toast.LENGTH_SHORT).show();
                    }
                    container.setVisibility(View.VISIBLE);
                    getAddress();
                } else {
                    // show error
                    Toast.makeText(CartActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dialog.dismiss();
                // show error
            }
        });
    }

   /* public void cart_Total(double Total, String name) {
        vAmount = Total;
    }*/


    // show hide cart
    private void showHideCart(boolean show) {
        CardView addHolder = findViewById(R.id.cartMid);
        CardView amountHolder = findViewById(R.id.cartTop);
        Button checkoutBtn = findViewById(R.id.checkoutBtn);
        RelativeLayout rl_bottom= findViewById(R.id.rl_bottom);

        addHolder.setVisibility(show ? View.VISIBLE : View.GONE);
        noItemsTv.setVisibility(!show ? View.VISIBLE : View.GONE);
        amountHolder.setVisibility(show ? View.VISIBLE : View.GONE);
        checkoutBtn.setVisibility(show ? View.VISIBLE : View.GONE);
        rl_bottom.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    // updating quantity method
    private void updateQuantity(String id, String token, Cart cart) {
        ProgressDialog dialog = new ProgressDialog(CartActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("Updating");
        dialog.setMessage("Please wait..");
        dialog.create();
        dialog.show();
        Call<ApiResponse> changeQuantityCall = new UserApiToJsonHandler().changeQuantity(id, token, cart);
        changeQuantityCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    getCart();
                } else {
                    Gson gson = new Gson();
                    ApiResponse message = gson.fromJson(response.errorBody().charStream(), ApiResponse.class);
                    Toast.makeText(CartActivity.this, message.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // get the address
    private void getAddress() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Call<ApiResponse> call = new UserApiToJsonHandler().getAllAddresses(token);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getAddress() != null) {
                        // get the default address
                        hasDefault = false;
                        for (Address address : response.body().getAddress()) {
                            if (address.isSelected()) {
                                addressId = address.getId();
                                hasDefault = true;
                                houseStreet.setText(address.getHouse() + "\t" + address.getStreet());
                                localityCity.setText(address.getLocality() + "\t" + address.getCity());
                                stateZip.setText(address.getState() + " - " + address.getZip());
                                break;
                            }
                        }

                        if (!hasDefault) {
                            houseStreet.setText(response.body().getAddress().get(0).getHouse() + "\t" + response.body().getAddress().get(0).getStreet());
                            localityCity.setText(response.body().getAddress().get(0).getLocality() + "\t" + response.body().getAddress().get(0).getCity());
                            stateZip.setText(response.body().getAddress().get(0).getState() + " - " + response.body().getAddress().get(0).getZip());
                        }
                    } else {
                        Toast.makeText(CartActivity.this, "No address", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // show error
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                getAddress();
            } else {

            }
        }
    }

    // calculate the totals
    float totalNum = 0;
    float discountNum = 0;
    int cartSize = 0;

    private void getCartTotals(List<Cart> cartList) {
        totalNum = 0;
        discountNum = 0;
        cartSize = 0;
        cartSize = cartList.size();


        for (Cart cart : cartList) {
            float gst = 0;

            float price = cart.getItem().getPrice();
            float disc_price = cart.getItem().getDiscount();
            float total_Amount,total_price = 0;

            if(disc_price == 0){

                total_Amount = price;

            }else{

                total_price = price * disc_price / 100;
                total_Amount = price - total_price;
            }

            if (cart.getItem() != null) {
                if (cart.getItem().getType().equals("product")) {
                    // gst from experience
                    //gst = Float.parseFloat(cart.getItem().getExperience());
                    gst = (18 / 100) * (total_Amount * cart.getQuantity());

                } else {
                    // gst from weight
                    if (cart.getItem().getWeight().size() > 0) {
                        gst = Float.parseFloat(String.valueOf(cart.getItem().getWeight().get(0)));
                        gst = (gst / 100) * (total_Amount * cart.getQuantity());

                    }
                }
                if (gst != 0) {
                    totalNum += (cart.getQuantity() * total_Amount) + gst;
                } else {
                    totalNum += (cart.getQuantity() * total_Amount);
                }
                //  discountNum += (cart.getQuantity() * ((cart.getItem().getDiscount() * cart.getItem().getPrice()) / 100));
                //   totalNum -= discountNum;
            }

        }
        //  this.discount.setText("₹" + discountNum);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        totalNum = Float.valueOf(decimalFormat.format(totalNum));

        this.total.setText("₹" + totalNum);
        this.shipping.setText("FREE");
        // this.payable.setText("₹" + (totalNum - discountNum));
        this.payable.setText("₹" + totalNum);
    }


    // changing billing address
    public void changeBillingAddress(View view) {
        startActivityForResult(new Intent(this, DeliveryAddressActivity.class), 100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_action_menu, menu);
        menu.getItem(0).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (item.getItemId() == R.id.db_notification) {
            startActivity(new Intent(this, NotificationActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}