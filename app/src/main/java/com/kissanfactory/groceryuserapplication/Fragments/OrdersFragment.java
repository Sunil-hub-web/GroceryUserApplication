package com.kissanfactory.groceryuserapplication.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Adapters.OrderAdapter;
import com.kissanfactory.groceryuserapplication.Models.Order;
import com.kissanfactory.groceryuserapplication.Models.OrderResponse;
import com.kissanfactory.groceryuserapplication.Models.ProductResponse;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class OrdersFragment extends Fragment {

    private RecyclerView orders;
    ProgressDialog dialog;
    SwipeRefreshLayout swipeRefreshLayout;
    boolean loaded = false;
    TextView tv_empty_msg;

    public static OrdersFragment newInstance() {

        Bundle args = new Bundle();

        OrdersFragment fragment = new OrdersFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
     //   swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        tv_empty_msg = view.findViewById(R.id.tv_empty_msg);
       // swipeRefreshLayout.setOnRefreshListener(() -> getOrders());
        init(view);
        return view;
    }

    // init view
    private void init(View view) {
        orders = view.findViewById(R.id.ordersView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!loaded) getOrders();
    }

    // get the order
    private void getOrders() {
        loaded = true;
        dialog = new ProgressDialog(this.getContext());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(true);
        dialog.create();
        dialog.show();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Call<OrderResponse> call = new UserApiToJsonHandler().getOrders(token);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    if(response.body().getData()!=null)
                    {
                        List<Order> unsortedOrders = response.body().getData();
                        Collections.reverse(unsortedOrders);
                        fillOrders(unsortedOrders);
                    }else{
                        tv_empty_msg.setVisibility(View.VISIBLE);
                    }

                } else {
                   // swipeRefreshLayout.setRefreshing(false);
                    dialog.dismiss();

//                    Toast.makeText(OrdersFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                dialog.dismiss();
//                Toast.makeText(OrdersFragment.this.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
             //   swipeRefreshLayout.setRefreshing(false);
                tv_empty_msg.setVisibility(View.VISIBLE);
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(OrdersFragment.this.getContext(), "Canceled", Toast.LENGTH_SHORT).show();
                call.cancel();
               // swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    // sort and fill orders view
    private void fillOrders(List<Order> orderList) {
        if (orderList.size() <= 0) {
            return;
        }

//        List<String> idList = new ArrayList<>();
//        idList.add(orderList.get(0).getCart_id());
//        for (Order order : orderList) {
//            boolean alreadyIn = false;
//            for (String id : idList) {
//                if (order.getCart_id().equals(id)) {
//                    alreadyIn = true;
//                }
//            }
//            if (!alreadyIn) {
//                idList.add(order.getCart_id());
//            }
//        }
//
//        // loopthrough all the id;s and organize the orders
//        List<Order> organizedOrders = new ArrayList<>();
//        for (String id : idList) {
//            for (Order order : orderList) {
//                // if the order id matches, add it to the list
//                if (order.getCart_id().equals(id)) {
//                    organizedOrders.add(order);
//                    break;
//                }
//            }
//        }

        OrderAdapter adapter = new OrderAdapter(OrdersFragment.this.getActivity());
        adapter.setOrders(orderList);
        orders.setAdapter(adapter);
        orders.setLayoutManager(new LinearLayoutManager(OrdersFragment.this.getActivity()));
        dialog.dismiss();
      //  swipeRefreshLayout.setRefreshing(false);

//        getOrderImage(0, organizedOrders);

    }

    // get order images
    private void getOrderImage(int index, List<Order> orderList) {
        Call<ProductResponse> call = new UserApiToJsonHandler().getProductDetails(orderList.get(index).getProducts().get(0).getProductId());
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getProductDetails() != null) {
                        orderList.get(index).setImage(response.body().getProductDetails().getImages().get(0));
                    }
                    if (index != orderList.size() - 1) {
                        getOrderImage(index + 1, orderList);
                    } else {
                        OrderAdapter adapter = new OrderAdapter(OrdersFragment.this.getActivity());
                        adapter.setOrders(orderList);
                        orders.setAdapter(adapter);
                        orders.setLayoutManager(new LinearLayoutManager(OrdersFragment.this.getActivity()));
                        dialog.dismiss();
                      //  swipeRefreshLayout.setRefreshing(false);
                    }
                }else{
                 //   swipeRefreshLayout.setRefreshing(false);
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }
}