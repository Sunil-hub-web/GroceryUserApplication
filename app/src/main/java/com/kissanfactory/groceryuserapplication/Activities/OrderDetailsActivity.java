package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Adapters.MiniOrdersAdapter;
import com.kissanfactory.groceryuserapplication.Adapters.OrderProductAdapter;
import com.kissanfactory.groceryuserapplication.Models.Order;
import com.kissanfactory.groceryuserapplication.Models.OrderResponse;
import com.kissanfactory.groceryuserapplication.Models.Product;
import com.kissanfactory.groceryuserapplication.Models.ProductResponse;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {

    List<Product> orderProductList = new ArrayList<>();
    private Toolbar toolbar;
    private TextView paymentMethodTv, date, status, totalTv, addressName, addressNum, orderIdTv, sizeTv, tWithDiscountTv, discountTv, oTotaltv;
    private LinearLayout addressLl;
    private TabLayout tabLayout;
    private String cartId;
    ProgressDialog dialog;
    RecyclerView productsRv, miniOrdersRv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        init();
    }

    private void init(){
        productsRv = findViewById(R.id.productsRv);
        date = findViewById(R.id.orderDate);
        status = findViewById(R.id.orderStatus);
        totalTv = findViewById(R.id.orderTotal);
        orderIdTv = findViewById(R.id.orderId);

        oTotaltv = findViewById(R.id.oTotal);
        sizeTv = findViewById(R.id.orderSize);
        tWithDiscountTv = findViewById(R.id.orderWithDiscount);
        discountTv = findViewById(R.id.orderDiscount);

        addressName = findViewById(R.id.orderDelName);
        addressNum = findViewById(R.id.orderDelNum);

        cartId = getIntent().getStringExtra("cartId");
        tabLayout = findViewById(R.id.tabLayout);
        toggleTab();
        addressLl = findViewById(R.id.addressHolder);
        paymentMethodTv = findViewById(R.id.paymentMethod);
        miniOrdersRv = findViewById(R.id.miniOrdersHolder);

        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Order Details");
        }

        getAllOrders();

    }

    // toggling the tab view
    private void toggleTab(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    // show the payment type tv
                    paymentMethodTv.setVisibility(View.VISIBLE);
                    addressLl.setVisibility(View.GONE);
                }else{
                    // show the address card
                    addressLl.setVisibility(View.VISIBLE);
                    paymentMethodTv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // displaying all mini orders in the list
    private void displayMiniOrders(List<Order> orderList){
        MiniOrdersAdapter adapter = new MiniOrdersAdapter(this);
        adapter.setOrders(orderList);

        miniOrdersRv.setAdapter(adapter);
        miniOrdersRv.setLayoutManager(new LinearLayoutManager(this));
    }

    // get all orders
    private void getAllOrders(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Call<OrderResponse> call = new UserApiToJsonHandler().getOrders(token);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if(response.isSuccessful()){
                    getAllSimilarOrders(response.body().getData());
                }else{
                    dialog.dismiss();
                    Toast.makeText(OrderDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(OrderDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            // refresh the page
            getAllOrders();
        }
    }

    // get all orders with the same cart id
    private void getAllSimilarOrders(List<Order> orderList){
        float total = 0;
        float discountTotal = 0;
        List<Order> similarOrders = new ArrayList<>();
        for(Order order: orderList){
            if (order.getCart_id().equals(cartId)){
                // add to new array
                similarOrders.add(order);

                total += order.getTotalAmount();
                discountTotal += order.getDiscountPrice();
            }
        }

        oTotaltv.setText("₹"+(total));
        tWithDiscountTv.setText("Before Discount\t:\t₹"+(total+discountTotal));

        totalTv.setText("Order Total\t:\t₹"+(total));
        discountTv.setText("Order Discount\t:\t₹"+discountTotal);

//        if(similarOrders.size()>0){
//            getOrderProduct(0, similarOrders);
//        }

        displayMiniOrders(similarOrders);

        String concatDate ="Order Date\t:\t" + similarOrders.get(0).getCreatedAt().split("T")[0] + " - " + similarOrders.get(0).getCreatedAt().split("T")[1].split("[.]")[0];
        date.setText(concatDate);
        paymentMethodTv.setText(similarOrders.get(0).getPaymentDetails().getPaymentMethod().toUpperCase());
        status.setText("Order Status\t:\t"+similarOrders.get(0).getOrderStatus());
        orderIdTv.setText("Order ID\t:\t"+cartId);

        addressName.setText(similarOrders.get(0).getShippingDetails().getName());
        addressNum.setText("(+91) "+ similarOrders.get(0).getShippingDetails().getContacts());
        similarOrders.get(0).getCreatedAt();

        sizeTv.setText("Order Size\t:\t"+similarOrders.size() +"Item (s)");

        dialog.dismiss();
    }

    // fill details with order products
    private void fillDetails(){
        float total = 0;
        float discount = 0;
        for(Product product: orderProductList){
            total +=product.getPrice() * product.getProductQuantity();
            discount += (product.getPrice() * (product.getDiscount()/100)) * product.getProductQuantity();

            if(product.getType().equals("product")){
                float gst = (product.getPrice() * product.getProductQuantity()) * (Float.parseFloat(product.getExperience())/100);
                total += gst;

            }else{
                // get from weight
                if(product.getWeight().size()>0){
                    double gst =(product.getPrice() * product.getProductQuantity()) * ((double) product.getWeight().get(0)/100);
                    total += gst;
                }
            }
        }

        oTotaltv.setText("₹"+(total-discount));
        tWithDiscountTv.setText("Before Discount\t:\t₹"+(total));

        totalTv.setText("Order Total\t:\t₹"+(total-discount));
        discountTv.setText("Order Discount\t:\t₹"+discount);

    }


    // get the products of the order
    private void getOrderProduct(int index, List<Order> orderList){
        Call<ProductResponse> call = new UserApiToJsonHandler().getProductDetails(orderList.get(index).getProducts().get(0).getProductId());
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()){
                    // add to products list
                    Product product = response.body().getProductDetails();
                    product.setProductQuantity(orderList.get(index).getProducts().get(0).getProductQuantity());
                    orderProductList.add(response.body().getProductDetails());
                    if(index == orderList.size()-1){
                        // stop
                        OrderProductAdapter adapter = new OrderProductAdapter(OrderDetailsActivity.this);
                        adapter.setListings(orderProductList);

                        productsRv.setAdapter(adapter);
                        productsRv.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this));

                        fillDetails();

                        Toast.makeText(OrderDetailsActivity.this, orderProductList.size()+"", Toast.LENGTH_SHORT).show();
                    }else{
                        // run again
                        getOrderProduct(index+1, orderList);
                    }
                }else{
                    Toast.makeText(OrderDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(OrderDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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