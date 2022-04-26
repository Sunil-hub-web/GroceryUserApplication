package com.kissanfactory.groceryuserapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.Cart;
import com.kissanfactory.groceryuserapplication.Models.CartItems;
import com.kissanfactory.groceryuserapplication.Models.NoMsgResponse;
import com.kissanfactory.groceryuserapplication.Models.Order;
import com.kissanfactory.groceryuserapplication.Models.PaymentHistory;
import com.kissanfactory.groceryuserapplication.Models.Product;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrderActivity extends AppCompatActivity {

    private String address, transactionId, name;
    long mobile;
    private float historyTotal = 0;
    String token;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        getIntentData();
    }

    private void getIntentData(){
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait..");
        dialog.create();
        dialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");


        Bundle myBundle = getIntent().getExtras();

        try {
            address = myBundle.getString("address");
            transactionId = myBundle.getString("transaction");
            name = myBundle.getString("firstname");
            mobile = myBundle.getLong("mobile");
            historyTotal = myBundle.getFloat("total");
        }catch (NullPointerException e){

        }


        createOrders();
    }

    // create the users order
    private void createOrders(){
        // get cart items
        Call<ApiResponse> call = new UserApiToJsonHandler().getCart(token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    // display all the cart items
                    makeOrderFromCart(response.body().getCartItems());
                }else{
                    dialog.dismiss();
                    Toast.makeText(CreateOrderActivity.this, "Cant fetch cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CreateOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeOrderFromCart(CartItems cartItems){
        List<Order> orders = new ArrayList<>();
        boolean isIn = false;
        // create the product from the cart item
        for(Cart cart : cartItems.getCart()){
            isIn = false;
            if (cart.getItem()!=null){
                float total = (cart.getItem().getPrice() *cart.getQuantity());
                float discount = (cart.getItem().getPrice() * (cart.getItem().getDiscount()/100)) *cart.getQuantity();
                total -= discount;

                if(cart.getItem().getType().equals("product")){
                    float gst = (cart.getItem().getPrice() * cart.getItem().getProductQuantity()) * (Float.parseFloat(cart.getItem().getExperience())/100);
                    total += gst;

                }else{
                    // get from weight
                    if(cart.getItem().getWeight().size()>0){
                        double gst =(cart.getItem().getPrice() * cart.getItem().getProductQuantity()) * ((double) cart.getItem().getWeight().get(0)/100);
                        total += gst;
                    }
                }

                // make product
                Product product = new Product();
                product.setId(cart.getItem().getId());
                product.setProductId();
                List<String> images = new ArrayList<>();
                images.add(cart.getItem().getImages().get(0));
                product.setImages(images);
                product.setSoldBy((cart.getItem().getSoldBy()!=null) ? cart.getItem().getSoldBy() :cart.getItem().getAddedBy());
                product.setProductQuantity(cart.getQuantity());
                product.setProductName(cart.getItem().getTitle());
                List<Product> products = new ArrayList<>();
                products.add(product);

                // check if there is an order with the same vendorid
                for(Order order1: orders){
                    if (order1.getVendorID().equalsIgnoreCase(product.getSoldBy())){
                        order1.getProducts().addAll(0, products);
                        isIn = true;
                        break;
                    }
                }

                // make the order out of the product
                if(!isIn){
                    Order order = new Order();
                    order.setVendorID((cart.getItem().getSoldBy()!=null) ? cart.getItem().getSoldBy() :cart.getItem().getAddedBy());
                    order.setTotalAmount((total *100.00)/100);
                    order.setPacked(false);
                    order.setShipped(false);
                    try {
                        order.setOrderImg(products.get(0).getImages().get(0));
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    order.setOrderStatus("ordered");
                    order.setDiscountPrice(cart.getItem().getDiscount() * cart.getItem().getPrice()/100);
                    order.setShippingName(name);
                    order.setShippindAddressID(address);
                    order.setShippingContact(mobile);
                    order.setPaymentStatus("paid");
                    order.setShippingCharge(1);
                    order.setPaymentMethod("debitCard");
                    order.setProducts(products);
                    order.setCartId(transactionId);
                    orders.add(order);
                }
            }

//            historyTotal += order.getTotalAmount();
            // add order to list
//            orders.add(order);
        }

        // display all orders
//        for(Order order: orders){
//            Log.e("Product: ", order.toString());
//        }
        // save the orders
        saveOrder(0, orders);
    }

    // saving orders based on the size of the list
    private void saveOrder(final int index, List<Order> orderList){
        Call<NoMsgResponse> call = new UserApiToJsonHandler().addOrder(token, orderList.get(index));
        call.enqueue(new Callback<NoMsgResponse>() {
            @Override
            public void onResponse(Call<NoMsgResponse> call, Response<NoMsgResponse> response) {
                if (response.isSuccessful()){
                    // if its the end index, then stop loading
                    if(index==orderList.size()-1){
                        // stop loading here
                        createPaymentHistory();
                    }else{
                        // call the save order again
                        saveOrder(index+1, orderList);
                    }
                }else{
                    dialog.dismiss();
                    Gson gson = new Gson();
                    ApiResponse message = gson.fromJson(response.errorBody().charStream(),ApiResponse.class);

                    Toast.makeText(CreateOrderActivity.this, message.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NoMsgResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CreateOrderActivity.this, "Saving order"+ t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    // creating the payment history
    private void createPaymentHistory(){
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setAmount(historyTotal);
        paymentHistory.setCartId(transactionId);
        paymentHistory.setOrderId("606c9596015a730a19fe23e5");
        paymentHistory.setSuccess(true);
        Call<NoMsgResponse> call = new UserApiToJsonHandler().addPayment(token, paymentHistory);
        call.enqueue(new Callback<NoMsgResponse>() {
            @Override
            public void onResponse(Call<NoMsgResponse> call, Response<NoMsgResponse> response) {
                if (response.isSuccessful()){
                    clearCart();
                }else{
                    dialog.dismiss();
                    Toast.makeText(CreateOrderActivity.this, "Cant create pay history", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NoMsgResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CreateOrderActivity.this, "making histy"+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    // clear cart
    private void clearCart(){
        Call<NoMsgResponse> call = new UserApiToJsonHandler().clearCart(token);
        call.enqueue(new Callback<NoMsgResponse>() {
            @Override
            public void onResponse(Call<NoMsgResponse> call, Response<NoMsgResponse> response) {
                if(response.isSuccessful()){
                    dialog.dismiss();
                    // open the confirmation activity here
                    startActivity(new Intent(CreateOrderActivity.this, ConfirmationActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .putExtra("transaction", transactionId));
                }else{
                    dialog.dismiss();
                    Toast.makeText(CreateOrderActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NoMsgResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(CreateOrderActivity.this, "Clearing cart"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}