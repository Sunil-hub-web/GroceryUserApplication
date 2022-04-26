package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Adapters.OrderProductAdapter;
import com.kissanfactory.groceryuserapplication.Models.Address;
import com.kissanfactory.groceryuserapplication.Models.AddressResponse;
import com.kissanfactory.groceryuserapplication.Models.AgentLocationResponse;
import com.kissanfactory.groceryuserapplication.Models.NoMsgResponse;
import com.kissanfactory.groceryuserapplication.Models.Order;
import com.kissanfactory.groceryuserapplication.Models.Product;
import com.kissanfactory.groceryuserapplication.Models.ProductResponse;
import com.kissanfactory.groceryuserapplication.Models.SingleOrderResponse;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiniOrderDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView productsRv;
    String orderId, token;
    List<Product> orderProductList;
    ProgressDialog dialog;
    Order myOrder = new Order();
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_order_detail);

        init();
    }

    private void init() {
        orderProductList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        orderId = getIntent().getStringExtra("id");
        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Order Detail");
        }

        showHideTrack();
        productsRv = findViewById(R.id.productsRv);
        getOrderDetails();
    }

    // show hide tracking details
    private void showHideTrack() {
        LinearLayout trackingHolderLl = findViewById(R.id.trackingHolder);
        TextView showHideTv = findViewById(R.id.showHideTrackingInfo);
        showHideTv.setOnClickListener(v -> {
            if (trackingHolderLl.getVisibility() == View.VISIBLE) {
                trackingHolderLl.setVisibility(View.GONE);
                showHideTv.setText("Show");
            } else {
                trackingHolderLl.setVisibility(View.VISIBLE);
                showHideTv.setText("Hide");
            }
        });
    }

    // get the order details
    private void getOrderDetails() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setOnCancelListener(dialog -> {
            super.onBackPressed();
        });
        dialog.setMessage("Loading...");
        dialog.create();
        dialog.show();

        Call<SingleOrderResponse> call = new UserApiToJsonHandler().getOrder(token, orderId);
        call.enqueue(new Callback<SingleOrderResponse>() {
            @Override
            public void onResponse(Call<SingleOrderResponse> call, Response<SingleOrderResponse> response) {
                if (response.isSuccessful()) {
                    // get the products from the result
                    myOrder = response.body().getData();
                    Log.e("Order", myOrder.toString());
                    trackingLogic(response.body().getData());
                    getAddress(response.body().getData().getShippingDetails().getAddressId());
                    getOrderProduct(0, response.body().getData().getProducts());
                    showHideMenu();
                } else {
                    dialog.dismiss();
                    Log.e("Error", "Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<SingleOrderResponse> call, Throwable t) {
                dialog.dismiss();
                Log.e("Error", t.getMessage());
            }
        });
    }

    // show hide menu
    private void showHideMenu() {
        if (myOrder.getOrderStatus().equalsIgnoreCase("cancel")) {
            // hide all menu items
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        } else if (myOrder.getOrderStatus().equalsIgnoreCase("delivered")) {
            // hide the cancel order
            menu.getItem(0).setVisible(false);
        } else {
            // hide the replace order
            menu.getItem(1).setVisible(false);
        }
    }

    // get address
    private void getAddress(String addressId) {

        Call<AddressResponse> call = new UserApiToJsonHandler().getAddress(token, addressId);
        call.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (response.isSuccessful()) {
                    fillAddressView(response.body().getAddress());
                } else {
                    dialog.dismiss();
                    Gson gson = new Gson();
                    AddressResponse message = gson.fromJson(response.errorBody().charStream(), AddressResponse.class);
                    Log.e("Error", message.getMsg());
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {
                dialog.dismiss();
                Log.e("Error", t.getMessage());
            }
        });
    }

    // fill address view
    private void fillAddressView(Address address) {
        TextView house, street, city, stateZip;
        house = findViewById(R.id.houseTv);
        street = findViewById(R.id.streetTv);
        city = findViewById(R.id.cityTv);
        stateZip = findViewById(R.id.stateZipTvr);

        if (address == null) {
            return;
        }
        house.setText(address.getHouse());
        street.setText(address.getStreet());
        city.setText(address.getCity());
        stateZip.setText(address.getState() + " " + address.getZip());
    }

    private void getOrderProduct(int index, List<Product> products) {
        Call<ProductResponse> call = new UserApiToJsonHandler().getProductDetails(products.get(index).getProductId());
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    // add to products list
                    Product product = response.body().getProductDetails();
                    try {
                        product.setProductQuantity(products.get(index).getProductQuantity());
                        orderProductList.add(response.body().getProductDetails());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (index == products.size() - 1) {
                        // stop
                        calculateOrderTotal(orderProductList);
                        dialog.dismiss();
                        OrderProductAdapter adapter = new OrderProductAdapter(MiniOrderDetailActivity.this);
                        adapter.setListings(orderProductList);
                        productsRv.setAdapter(adapter);
                        productsRv.setLayoutManager(new LinearLayoutManager(MiniOrderDetailActivity.this));
                    } else {
                        // run again
                        getOrderProduct(index + 1, products);
                    }
                } else {
                    Toast.makeText(MiniOrderDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(MiniOrderDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // calculating the order total
    private void calculateOrderTotal(List<Product> products) {
        float total = 0;
        float gst = 0;
        float discount = 0;

        TextView subtotalTv, totalTv, discountTv;
        subtotalTv = findViewById(R.id.subTotal);
        discountTv = findViewById(R.id.discount);
        totalTv = findViewById(R.id.total);
        for (Product product : products) {
            if (product != null) {
                if (product.getType().equals("product")) {
                    // gst from experience
                    gst = Float.parseFloat(product.getExperience());
                } else {
                    // gst from weight if it exists
                    if (product.getWeight().size() > 0) {
                        gst = Float.parseFloat((String) product.getWeight().get(0));
                    }
                }
                discount += (product.getPrice() * (product.getDiscount() / 100)) * product.getProductQuantity();
                total += (product.getPrice() - (product.getPrice() * (gst / 100))) * product.getProductQuantity();
            }
        }
        String stotalTxt = "₹" + round(total, 2);
        String discountTxt = "₹" + round(discount, 2);
        String totalTxt = "₹" + round((total - discount), 2);

        discountTv.setText(discountTxt);
        subtotalTv.setText(stotalTxt);
        totalTv.setText(totalTxt);
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    // tracking logic
    private void trackingLogic(Order order) {
        View orderV, shippedV, pendingV, orderL, pendingL;

        TextView orderedTv, estimateTv, pickedTv;
        orderedTv = findViewById(R.id.orderedTv);
        estimateTv = findViewById(R.id.estimateTv);
        pickedTv = findViewById(R.id.pickedTv);
        orderL = findViewById(R.id.orderL);
        orderV = findViewById(R.id.orderV);
        pendingL = findViewById(R.id.pendingL);
        pendingV = findViewById(R.id.pendingV);
        shippedV = findViewById(R.id.shippedV);

        switch (order.getOrderStatus()) {
            case "ordered":
                orderedTv.setText("Ordered on: " + order.getCreatedAt().split("T")[0]);
                estimateTv.setText("Estimated delivery time (3 days)");
                pickedTv.setText("Pending vendor confirmation");
                break;
            case "cancel":
                orderV.setBackgroundColor(getResources().getColor(R.color.light_gray));
                pendingV.setBackgroundColor(getResources().getColor(R.color.light_gray));
                shippedV.setBackgroundColor(getResources().getColor(R.color.light_gray));
                orderL.setVisibility(View.GONE);
                pendingL.setVisibility(View.GONE);
                pickedTv.setText("Canceled");
                estimateTv.setText("Canceled");
                orderedTv.setText("Canceled");
                break;
            case "delivered":
                orderL.setBackgroundColor(getResources().getColor(R.color.blue_primary));
                pendingL.setBackgroundColor(getResources().getColor(R.color.blue_primary));
//                pendingL.setLayoutParams(new LinearLayout.LayoutParams(50, 100));
                orderV.setBackgroundColor(getResources().getColor(R.color.blue_primary));
                pendingV.setBackgroundColor(getResources().getColor(R.color.blue_primary));
                shippedV.setBackgroundColor(getResources().getColor(R.color.blue_primary));
                pickedTv.setText("Confirmed By Vendor (dispatched)");
                estimateTv.setText("Delivered - (Signature confirmed)");
                orderedTv.setText("Ordered on: " + order.getCreatedAt().split("T")[0]);
                break;
            default:
                orderV.setBackgroundColor(getResources().getColor(R.color.blue_primary));
                pendingV.setBackgroundColor(getResources().getColor(R.color.blue_primary));
                shippedV.setBackgroundColor(getResources().getColor(R.color.light_gray));
                orderL.setBackgroundColor(getResources().getColor(R.color.blue_primary));
                pendingL.setBackgroundColor(getResources().getColor(R.color.blue_primary));
                pickedTv.setText("Confirmed By Vendor (dispatched)");
                estimateTv.setText("Estimated delivery time (3 days)");
                orderedTv.setText("Ordered on: " + order.getCreatedAt().split("T")[0]);
                break;
        }

    }

    // cancel order function
    private void cancelOrder() {
        dialog.show();
        Order order = new Order();
        order.setOrderId(orderId);
        Call<NoMsgResponse> call = new UserApiToJsonHandler().cancelOrder(token, order);
        call.enqueue(new Callback<NoMsgResponse>() {
            @Override
            public void onResponse(Call<NoMsgResponse> call, Response<NoMsgResponse> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    // go back
                    setResult(RESULT_OK);
                    MiniOrderDetailActivity.this.onBackPressed();
                } else {
                    Toast.makeText(MiniOrderDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NoMsgResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MiniOrderDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // get driver location
    private void getAgentLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Call<AgentLocationResponse> call = new UserApiToJsonHandler().agentLocation(token, orderId);
        call.enqueue(new Callback<AgentLocationResponse>() {
            @Override
            public void onResponse(Call<AgentLocationResponse> call, Response<AgentLocationResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getData().size() > 0) {
                        startActivity(new Intent(MiniOrderDetailActivity.this, TrackOrderActivity.class)
                                .putExtra("lat", response.body().getData().get(0).getCurrentLocation().getLatitude())
                                .putExtra("long", response.body().getData().get(0).getCurrentLocation().getLongitude()));
                    } else {
                        // no driver location
                        Toast.makeText(MiniOrderDetailActivity.this, "Driver location not updated", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MiniOrderDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AgentLocationResponse> call, Throwable t) {
                Toast.makeText(MiniOrderDetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.order_action_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        } else if (item.getItemId() == R.id.orderCancel) {
            Dialog mDialog = new Dialog(this);
            mDialog.setContentView(R.layout.dialog_log_out);
            mDialog.create();
            mDialog.show();
            mDialog.setCancelable(true);
            TextView title = mDialog.findViewById(R.id.dialogTitle);
            title.setText("Cancel Order?");

            TextView cancelTv = mDialog.findViewById(R.id.tv_Cancel);
            cancelTv.setText("Exit");
            cancelTv.setOnClickListener(v -> mDialog.dismiss());

            TextView actionTv = mDialog.findViewById(R.id.tv_Logout);
            actionTv.setText("Cancel Order");
            actionTv.setOnClickListener(v -> {
                mDialog.dismiss();
                cancelOrder();
            });
        } else if (item.getItemId() == R.id.replaceOrder) {
            Toast.makeText(this, "This is replace order", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.deliveryDriver) {
            startActivity(new Intent(this, DeliveryAgentActivity.class).putExtra("orderId", orderId));
        } else if (item.getItemId() == R.id.trackOrder) {
            getAgentLocation();
        }
        return super.onOptionsItemSelected(item);
    }
}