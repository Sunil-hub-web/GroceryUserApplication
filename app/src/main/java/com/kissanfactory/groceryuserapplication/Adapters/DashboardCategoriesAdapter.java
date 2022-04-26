package com.kissanfactory.groceryuserapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kissanfactory.groceryuserapplication.Activities.CategoryDetailActivity;
import com.kissanfactory.groceryuserapplication.Models.Category;
import com.kissanfactory.groceryuserapplication.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class DashboardCategoriesAdapter extends RecyclerView.Adapter<DashboardCategoriesAdapter.DashboardCategoriesViewHolder> {

    private List<Category> categories;

    private Context context;

    public DashboardCategoriesAdapter(Context context) {
        this.context = context;
    }

    public void setCategories(List<Category> categories){
        this.categories = categories;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DashboardCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DashboardCategoriesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardCategoriesViewHolder holder, int position) {
        switch (position){
            case 0:
                Glide.with(context).load(R.drawable.ic_products_from_village_01).into(holder.image);
                break;
            case 1:
                Glide.with(context).load(R.drawable.ic_fertilizer_01).into(holder.image);
                break;
            case 2:
                Glide.with(context).load(R.drawable.ic_seeds_01).into(holder.image);
                break;
            case 3:
                Glide.with(context).load(R.drawable.ic_animal_feed_01).into(holder.image);
                break;
            case 4:
                Glide.with(context).load(R.drawable.ic_home_made_products_01).into(holder.image);
                break;
        }
        holder.name.setText(StringUtils.capitalize(categories.get(position).getName()));
        holder.rootView.setOnClickListener(view -> {
            context.startActivity(new Intent(context, CategoryDetailActivity.class)
            .putExtra("category", categories.get(position).getId())
            .putExtra("name", categories.get(position).getName()));
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class DashboardCategoriesViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout rootView;
        private TextView name;
        private ImageView image;

        public DashboardCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.rootView);
            name = itemView.findViewById(R.id.catName);
            image = itemView.findViewById(R.id.catImage);
        }
    }
}
