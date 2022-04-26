package com.kissanfactory.groceryuserapplication.Adapters.noti;

import static com.kissanfactory.groceryuserapplication.Activities.MyApplication.TAG;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kissanfactory.groceryuserapplication.Models.UserNotification;
import com.kissanfactory.groceryuserapplication.Models.notification.Datum;
import com.kissanfactory.groceryuserapplication.Models.notification.NotificationDto;
import com.kissanfactory.groceryuserapplication.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotifiewHolder> {

    private Activity context;
    private List<Datum> notifications;

    public NotificationsAdapter(Activity context, ArrayList<Datum> notificationDtos) {
        this.context = context;
        this.notifications = notificationDtos;
    }


    @NonNull
    @Override
    public NotifiewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotifiewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotifiewHolder holder, int position) {
        holder.title.setText(notifications.get(position).getNotification().getTitle());
        holder.description.setText(notifications.get(position).getNotification().getBody());
    }

    @Override
    public int getItemCount() {
        return notifications != null ? notifications.size() : 0;
    }

    public static class NotifiewHolder extends RecyclerView.ViewHolder {

        private TextView title, description;

        public NotifiewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.notif_title);
            description = itemView.findViewById(R.id.notif_desc);


        }
    }

    public String removeItem(int position) {

        try {
            notifications = Collections.singletonList(notifications.get(position));
            notifications.remove(position);
            notifyItemRemoved(position);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return notifications.get(position).getId();
    }
}
