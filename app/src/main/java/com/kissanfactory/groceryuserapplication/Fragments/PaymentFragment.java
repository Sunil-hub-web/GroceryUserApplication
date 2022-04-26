package com.kissanfactory.groceryuserapplication.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.Adapters.PaymentHistoryAdapter;
import com.kissanfactory.groceryuserapplication.Models.paymenthistory.Datum;
import com.kissanfactory.groceryuserapplication.Models.paymenthistory.PaymentHistoryDto;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class PaymentFragment extends Fragment {

    private RecyclerView allPayments;
    TextView totalPaymentTv;

    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        allPayments = view.findViewById(R.id.payments);
        totalPaymentTv = view.findViewById(R.id.totalPaymentTv);
        getPayments();

    }

    // getting the payments of the user
    private void getPayments() {
        ProgressDialog dialog = new ProgressDialog(this.getContext());
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Call<PaymentHistoryDto> call = new UserApiToJsonHandler().getPaymentshistory(token);
        call.enqueue(new Callback<PaymentHistoryDto>() {
            @Override
            public void onResponse(Call<PaymentHistoryDto> call, Response<PaymentHistoryDto> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(PaymentFragment.this.getActivity());
                    List<Datum> paymentHistories = response.body().getData();
                    Collections.reverse(paymentHistories);
                    adapter.setPaymentHistoryList(paymentHistories);
                    float total = 0;
//                    for (Datum history: paymentHistories){
//                        total += history.getAmount();
//                    }
                    // show the total spent
                    totalPaymentTv.setText("₹" + response.body().getWallet());

                    allPayments.setAdapter(adapter);
                    allPayments.setLayoutManager(new LinearLayoutManager(PaymentFragment.this.getActivity()));
                } else {
                    Toast.makeText(PaymentFragment.this.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentHistoryDto> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(PaymentFragment.this.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // show the total


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.getItem(0).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}