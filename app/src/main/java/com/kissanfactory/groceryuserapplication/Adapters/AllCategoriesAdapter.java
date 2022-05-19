package com.kissanfactory.groceryuserapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kissanfactory.groceryuserapplication.Models.Category;
import com.kissanfactory.groceryuserapplication.R;

import java.util.List;

public class AllCategoriesAdapter extends ArrayAdapter<Category> {

    private Activity activity;
    private List<Category> categoryArrayList;

    public AllCategoriesAdapter(Activity context, List<Category> objects) {
        super(context, android.R.layout.simple_expandable_list_item_1, objects);
        this.activity = context;
        this.categoryArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        @SuppressLint("ViewHolder") View listView = inflater.inflate(R.layout.all_categories_item, null, false);
        TextView view = listView.findViewById(R.id.catName);

        String titleTxt = categoryArrayList.get(position).getName();
        titleTxt = titleTxt.replace("_", " ");
        String[] txts = titleTxt.split(" ");
        titleTxt = "";
        for (String string: txts){

            //string = string.substring(0, 1).toUpperCase() + string.substring(1);

            titleTxt = titleTxt + string + " ";
        }
        view.setText(titleTxt);
        return listView;
    }
}
