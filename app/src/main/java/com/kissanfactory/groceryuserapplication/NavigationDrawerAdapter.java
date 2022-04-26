package com.kissanfactory.groceryuserapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Parkhya on 04/01/18.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    int pos = 0;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());

        if (pos == position) {
            holder.title.setTextColor(context.getResources().getColor(R.color.light_blue));
            holder.bottom_line.setVisibility(View.VISIBLE);
        } else {
            holder.title.setTextColor(context.getResources().getColor(R.color.extralight_gray));
            holder.bottom_line.setVisibility(View.GONE);

        }
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    holder.title.setTextColor(context.getResources().getColor(R.color.light_blue));
                    holder.bottom_line.setVisibility(View.VISIBLE);
                    pos = position;
                    notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView bottom_line;
        View view;
        private LinearLayout layout_logout;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            bottom_line = itemView.findViewById(R.id.bottom_line);

        }
    }
}
