package com.kissanfactory.groceryuserapplication.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kissanfactory.groceryuserapplication.Activities.CategoryDetailActivity;
import com.kissanfactory.groceryuserapplication.Models.top_productsdto.Datum;
import com.kissanfactory.groceryuserapplication.R;

import java.util.List;

public class TopCategoriesHeadingAdapter extends RecyclerView.Adapter<TopCategoriesHeadingAdapter.TopCategoriesHeadingViewHolder> {
    Activity context;
    private List<Datum> productList;

    public TopCategoriesHeadingAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TopCategoriesHeadingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopCategoriesHeadingViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topcategory_heading_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopCategoriesHeadingViewHolder holder, int position) {

        holder.tv_top_cat.setText(productList.get(position).getName());

        CategoryProductAdapter topCategoriesItemAdapter = new CategoryProductAdapter(context);
        holder.categoriesList.setAdapter(topCategoriesItemAdapter);
        topCategoriesItemAdapter.setProductList(productList.get(position).getData());
        holder.categoriesList.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        if (holder.categoriesList.getItemDecorationCount() <= 0) {
            holder.categoriesList.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.getItemOffsets(outRect, view, parent, state);
                    outRect.left = 20;
                }
            });
        }
        holder.viewAllCat.setOnClickListener(view1 -> context.startActivity(new Intent(context, CategoryDetailActivity.class)
                .putExtra("category", "1").putExtra("Url", productList.get(position).getUrl())
                .putExtra("name", productList.get(position).getName())));

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setProductList(List<Datum> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    public class TopCategoriesHeadingViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_top_cat, viewAllCat;
        RecyclerView categoriesList;

        public TopCategoriesHeadingViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_top_cat = itemView.findViewById(R.id.tv_top_cat);
            viewAllCat = itemView.findViewById(R.id.viewAllCat);
            categoriesList = itemView.findViewById(R.id.categoriesList);
        }
    }
}
