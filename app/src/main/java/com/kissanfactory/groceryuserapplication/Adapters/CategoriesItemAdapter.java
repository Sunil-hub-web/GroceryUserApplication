package com.kissanfactory.groceryuserapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kissanfactory.groceryuserapplication.Models.Product;
import com.kissanfactory.groceryuserapplication.R;

import java.util.List;

public class CategoriesItemAdapter extends RecyclerView.Adapter<CategoriesItemAdapter.CategoriesItemViewHolder> {

    private List<Product> productsList;
    private Context context;

    public CategoriesItemAdapter(Context context) {
        this.context = context;
    }

    public void setProductsList(List<Product> productsList) {
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public CategoriesItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoriesItemViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.category_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class CategoriesItemViewHolder extends RecyclerView.ViewHolder{
        public CategoriesItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
