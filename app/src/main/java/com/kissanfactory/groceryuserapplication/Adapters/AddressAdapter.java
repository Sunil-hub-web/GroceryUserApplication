package com.kissanfactory.groceryuserapplication.Adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kissanfactory.groceryuserapplication.Activities.AddAddressActivity;
import com.kissanfactory.groceryuserapplication.Models.Address;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.Models.DefaultAddress;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<Address> addressesList;
    private Activity context;

    public AddressAdapter(Activity context) {
        this.context = context;
    }

    public void setAddressesList(List<Address> addressesList) {
        this.addressesList = addressesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        if (addressesList.get(position).isSelected()){
            holder.makeDefault.setVisibility(View.GONE);
            holder.active.setChecked(true);
        }else{
            holder.makeDefault.setVisibility(View.VISIBLE);
            holder.active.setChecked(false);
        }

        holder.stateZip.setText(addressesList.get(position).getState() + " - " + addressesList.get(position).getZip());
        holder.houseStreet.setText(addressesList.get(position).getHouse() + "\t" + addressesList.get(position).getStreet());
        holder.localityCity.setText(addressesList.get(position).getLocality() + "\t" + addressesList.get(position).getCity());

        holder.makeDefault.setOnClickListener(view -> {
            // check if there is any defaults
            boolean defaultExists = false;
            for (Address address: addressesList){
                if (address.isSelected()){
                    defaultExists = true;
                    makeDefault(address.getId(), addressesList.get(position).getId());
                    break;
                }
            }

            if (!defaultExists) {
                // no defaults exist, send null
                makeDefault(null, addressesList.get(position).getId());
            }
        });

        holder.rootView.setOnClickListener(view -> {
            editAddress(addressesList.get(position));
        });
    }

    // opening address details page
    private void editAddress(Address address){
        Bundle bundle = new Bundle();
        bundle.putString("zip", String.valueOf(address.getZip()));
        bundle.putString("locality", address.getLocality());
        bundle.putString("state", address.getState());
        bundle.putString("city", address.getCity());
        bundle.putString("country", address.getCountry());
        bundle.putString("house", address.getHouse());
        bundle.putString("street", address.getStreet());
        context.startActivityForResult(new Intent(context, AddAddressActivity.class).putExtra("address", address), 200);
    }

    // make default function
    private void makeDefault(String currentId, String newId){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Logging in");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.create();
        dialog.show();
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        DefaultAddress address = new DefaultAddress();
        address.setAddressId(newId);
        address.setCurrentAddressId(currentId);

        Call<ApiResponse> call = new UserApiToJsonHandler().makeDefaultAddress(token, address);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                    context.setResult(Activity.RESULT_OK);
                    context.finish();
                }else{
                    dialog.dismiss();
                    Gson gson = new Gson();
                    ApiResponse message = gson.fromJson(response.errorBody().charStream(),ApiResponse.class);
                    Toast.makeText(context,message.getMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressesList.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder{
        private TextView houseStreet, localityCity, stateZip;
        private Button makeDefault;
        private RadioButton active;
        private RelativeLayout rootView;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            active = itemView.findViewById(R.id.checkedState);
            houseStreet = itemView.findViewById(R.id.houseStreet);
            localityCity = itemView.findViewById(R.id.cityLocality);
            stateZip = itemView.findViewById(R.id.stateZip);
            rootView = itemView.findViewById(R.id.rootView);

            makeDefault = itemView.findViewById(R.id.makeDefault);
        }
    }
}
