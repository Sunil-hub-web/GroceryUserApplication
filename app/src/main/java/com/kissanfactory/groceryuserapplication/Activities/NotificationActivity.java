package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kissanfactory.groceryuserapplication.Adapters.noti.NotificationsAdapter;
import com.kissanfactory.groceryuserapplication.Models.notification.Datum;
import com.kissanfactory.groceryuserapplication.Models.notification.NotificationDto;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView notificationsView;
    private NotificationsAdapter mAdapter;
    private ArrayList<Datum> notificationDtos;

    //  SampleRecyclerViewAdapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        init();

    }

    private void init() {
        notificationsView = findViewById(R.id.notifications);
        toolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TextView title = toolbar.findViewById(R.id.tb_title);
            title.setText("Notifications");
        }
        notificationsView = (RecyclerView) findViewById(R.id.notifications);
        // Set a layout manager
        notificationsView.setLayoutManager(new LinearLayoutManager(this));
        //   notificationsView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        // Create and set an adapter
        notificationDtos = new ArrayList<>();
        mAdapter = new NotificationsAdapter(this, notificationDtos);
        notificationsView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        notificationsView.setAdapter(mAdapter);


        // Create and add a callback
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                try {
                    final int position = viewHolder.getAdapterPosition();
                    final String item = mAdapter.removeItem(position);
                    Snackbar snackbar = Snackbar.make(viewHolder.itemView, "Item " + (direction == ItemTouchHelper.LEFT ? "deleted" : "") + ".", Snackbar.LENGTH_LONG);
                    snackbar.setAction(android.R.string.cancel, new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            try {
                                mAdapter.removeItem(position);
                            } catch (Exception e) {
                                Log.e("MainActivity", e.getMessage());
                            }
                        }
                    });
                    snackbar.show();
                } catch (Exception e) {
                    Log.e("MainActivity", e.getMessage());
                }
            }

            // You must use @RecyclerViewSwipeDecorator inside the onChildDraw method
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(NotificationActivity.this, R.color.recycler_view_item_swipe_right_background))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_white_24dp)
                        /// .addSwipeRightBackgroundColor(ContextCompat.getColor(NotificationActivity.this, R.color.recycler_view_item_swipe_right_background))
                        // .addSwipeRightActionIcon(R.drawable.ic_delete_white_24dp)
                        // .addSwipeRightLabel(getString(R.string.action_delete))
                        // .setSwipeRightLabelColor(Color.WHITE)
                        //  .addSwipeLeftLabel(getString(R.string.action_delete))
                        .setSwipeLeftLabelColor(Color.WHITE)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(notificationsView);


        getNotifications();
    }


    // getting the notifications item
    private void getNotifications() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Call<NotificationDto> call = new UserApiToJsonHandler().getNotifications(token);
        call.enqueue(new Callback<NotificationDto>() {
            @Override
            public void onResponse(Call<NotificationDto> call, Response<NotificationDto> response) {
                if (response.isSuccessful()) {
                    // make the notifications list here
                    if (notificationDtos != null) {
                        notificationDtos.clear();
                        notificationDtos.addAll(response.body().getData());
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NotificationDto> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            startActivity(new Intent(this, MainActivity.class));
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}