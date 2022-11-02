package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kissanfactory.groceryuserapplication.FragmentNavigationDrawer;
import com.kissanfactory.groceryuserapplication.Fragments.ChatBoardFragment;
import com.kissanfactory.groceryuserapplication.Fragments.DashBoardFragment;
import com.kissanfactory.groceryuserapplication.Fragments.OrdersFragment;
import com.kissanfactory.groceryuserapplication.Fragments.PaymentFragment;
import com.kissanfactory.groceryuserapplication.Models.ApiResponse;
import com.kissanfactory.groceryuserapplication.R;
import com.kissanfactory.groceryuserapplication.UserSingleton;
import com.kissanfactory.groceryuserapplication.WebServices.UserApiToJsonHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kissanfactory.groceryuserapplication.FragmentNavigationDrawer.img_user;
import static com.kissanfactory.groceryuserapplication.FragmentNavigationDrawer.sideBarText;
import static com.kissanfactory.groceryuserapplication.FragmentNavigationDrawer.sideBaraddress;

public class MainActivity extends AppCompatActivity implements FragmentNavigationDrawer.FragmentDrawerListener {

    private RelativeLayout new_toolbar;
    private FragmentNavigationDrawer navigationView;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    FragmentManager manager;
    ImageView menu_icon;
    // fragments
    DashBoardFragment dashBoardFragment;
    OrdersFragment ordersFragment;
    PaymentFragment paymentFragment;
    ChatBoardFragment chatBoardFragment;
    Fragment activeFragment;

    private final String CHAT = "Chat";
    private final String HOME = "Home";
    private final String ORDERS = "Orders";
    private final String WALLET = "Wallet";

    private boolean atHome = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        getUser();
        setUpToolbar();
        // navigationClick();
        bottomNavView();
        setUpAllFragments();
//        replaceFragment(new DashBoardFragment(), DASHBOARD);
        setTitle("Home");
        atHome = true;


    }


    // setting up the toolbar
    private void setUpToolbar() {
        new_toolbar = findViewById(R.id.new_toolbar);

//        if (getSupportActionBar() == null) {
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setTitle("");
//        }
        menu_icon = findViewById(R.id.menu_icon);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = (FragmentNavigationDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        navigationView.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout));
        navigationView.setDrawerListener(this);
//        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
//        drawerLayout.addDrawerListener(drawerToggle);
//        drawerToggle.syncState();


        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager inputManager = (InputMethodManager) MainActivity.this
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            MainActivity.this.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {

                }
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

    }

    // setting up the bottom navigation view click functionality
    @SuppressLint("NonConstantResourceId")
    private void bottomNavView() {
        bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.dashboard:
                    if (!atHome) {
                        setTitle("Home");
                        showFragment(dashBoardFragment);
//                        replaceFragment(new DashBoardFragment(), DASHBOARD);
                        atHome = true;
                        item.setChecked(true);
                    }
                    break;
                case R.id.orders:
                    if (loggedIn()) {
                        setTitle("My Order");
                        showFragment(ordersFragment);
//                        replaceFragment(new OrdersFragment(), ORDERS);
                        item.setChecked(true);
                    } else {
                        showAlert();
                    }
                    break;
                case R.id.payment:
                    if (loggedIn()) {
                        setTitle("Payment");
                        showFragment(paymentFragment);
//                        replaceFragment(new PaymentFragment(), WALLET);
                        item.setChecked(true);
                    } else {
                        showAlert();
                    }
                    break;
                case R.id.chatBoard:
                    setTitle("Chat");
                    showFragment(chatBoardFragment);
//                    replaceFragment(new ChatBoardFragment(), CHAT);
                    item.setChecked(true);
                    break;
            }
            return false;
        });
    }

    private void getUser() {

        if (loggedIn()) {
            // there is user

            sideBaraddress.setVisibility(View.GONE);
            img_user.setVisibility(View.VISIBLE);
            String token = sharedPreferences.getString("token", "");
            System.out.println("token:-......" + token);
            Call<ApiResponse> call = new UserApiToJsonHandler().customerProfile(token);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        // save user to singleton
                        UserSingleton userSingleton = UserSingleton.getInstance();
                        userSingleton.setName(response.body().getUserData().getName());
                        userSingleton.setEmail(response.body().getUserData().getEmailID());
                        userSingleton.setMobile(response.body().getUserData().getMobile());
                        sideBarText.setText(response.body().getUserData().getName());
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                }
            });
        } else {
            // no user
            // set the lorem ipsum to login/signup
            sideBaraddress.setVisibility(View.GONE);
            img_user.setVisibility(View.GONE);

            sideBarText.setGravity(Gravity.CENTER);

            sideBarText.setText("Login/SignUp");
            sideBarText.setOnClickListener(view -> {
                startActivity(new Intent(this, WelcomeActivity.class));
            });
        }
    }

    // setting toolbar title
    private void setTitle(String title) {
        TextView titletxt = new_toolbar.findViewById(R.id.tb_title);
        titletxt.setText(title);

        ImageView notification_icon = new_toolbar.findViewById(R.id.notification_icon);
        notification_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });


    }

    // show hide fragments
    private void showFragment(Fragment fragment) {
        atHome = fragment == dashBoardFragment;
        manager.beginTransaction().hide(activeFragment).show(fragment).commit();
        activeFragment = fragment;
    }

    // saving the bottom nav fragment states
    private void setUpAllFragments() {
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        // all fragments
        dashBoardFragment = new DashBoardFragment();
        ordersFragment = OrdersFragment.newInstance();
        paymentFragment = new PaymentFragment();
        chatBoardFragment = new ChatBoardFragment();
        transaction.add(R.id.appPageHolder, ordersFragment).hide(ordersFragment);
        transaction.add(R.id.appPageHolder, paymentFragment).hide(paymentFragment);
        transaction.add(R.id.appPageHolder, chatBoardFragment).hide(chatBoardFragment);
        transaction.add(R.id.appPageHolder, dashBoardFragment);
        transaction.commit();
        activeFragment = dashBoardFragment;
    }

    // function to replace fragments
    private void replaceFragment(Fragment fragment, String TAG) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.appPageHolder, fragment, TAG);
        transaction.commitAllowingStateLoss();
        switch (TAG) {
            case ORDERS:
                setTitle("My Orders");
                break;
            case WALLET:
                setTitle("Payment");
                break;
            case CHAT:
                setTitle("Chat Board");
                break;
            case HOME:
                setTitle("Home");
        }
        atHome = false;
    }

    // check if user is logged in
    private boolean loggedIn() {
        return sharedPreferences.getBoolean("exist", false);
    }

    // show not logged in alert
    private void showAlert() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pop_up_alert);

        TextView yes = dialog.findViewById(R.id.yesBtn);
        TextView no = dialog.findViewById(R.id.noBtn);

        yes.setOnClickListener(view -> {
            dialog.dismiss();
            startActivity(new Intent(this, WelcomeActivity.class));
        });

        no.setOnClickListener(view -> dialog.dismiss());
        dialog.create();
        dialog.show();
    }

    // side bar click functionality
//    @SuppressLint("NonConstantResourceId")
//    private void navigationClick() {
//        if (!loggedIn()) {
//            navigationView.getMenu().getItem(2).setVisible(false);
//            navigationView.getMenu().getItem(0).setVisible(false);
//        }
//        navigationView.setNavigationItemSelectedListener(item -> {
//            drawerLayout.closeDrawer(GravityCompat.START);
//            switch (item.getItemId()) {
//                case R.id.side_terms:
//                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                    } else if (!atHome) {
//                        // go to dashboard
//                        showFragment(dashBoardFragment);
////            replaceFragment(new DashBoardFragment(), "Dashboard");
//                        bottomNavigationView.getMenu().getItem(0).setChecked(true);
//                    } else {
//                        super.onBackPressed();
//                    }
//
//                    break;
//
////                case R.id.side_profile:
////                    if (loggedIn()) {
////                        startActivity(new Intent(this, ProfileActivity.class));
////                    } else {
////                        showAlert();
////                    }
////                    break;
////                case R.id.side_cart:
////                    if (loggedIn()) {
////                        startActivity(new Intent(MainActivity.this, CartActivity.class));
////                    } else {
////                        showAlert();
////                    }
////                    break;
////                case R.id.side_terms:
////                    startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class).putExtra("position", 0));
////                    break;
////                case R.id.side_policy:
////                    startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class).putExtra("position", 1));
////                    break;
////                case R.id.side_about_us:
////                    startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
////                    break;
////                case R.id.side_orders:
////                    if (loggedIn()) {
////                        showFragment(ordersFragment);
//////                        replaceFragment(new OrdersFragment(), ORDERS);
////                        bottomNavigationView.getMenu().getItem(1).setChecked(true);
////                    } else {
////                        showAlert();
////                    }
////                    break;
////                case R.id.side_wallet:
////                    if (loggedIn()) {
////                        showFragment(paymentFragment);
//////                        replaceFragment(new PaymentFragment(), WALLET);
////                        bottomNavigationView.getMenu().getItem(2).setChecked(true);
////                    } else {
////                        showAlert();
////                    }
////                    break;
////                case R.id.side_favorites:
////                    if (loggedIn()) {
////                        startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
////                    } else {
////                        showAlert();
////                    }
////                    break;
////                case R.id.side_notification:
////                    if (loggedIn()) {
////                        startActivity(new Intent(MainActivity.this, NotificationActivity.class));
////                    } else {
////                        showAlert();
////                    }
////                    break;
////                case R.id.side_chat:
////                    showFragment(chatBoardFragment);
//////                    replaceFragment(new ChatBoardFragment(), CHAT);
////                    bottomNavigationView.getMenu().getItem(3).setChecked(true);
////                    break;
////                case R.id.side_address:
////                    if (loggedIn()) {
////                        startActivity(new Intent(MainActivity.this, DeliveryAddressActivity.class));
////                    } else {
////                        showAlert();
////                    }
////                    break;
//                case R.id.side_logout:
//                    logout();
//                    break;
//            }
//            return false;
//        });
//    }

    // logout dialog


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!atHome) {
            // go to dashboard
            showFragment(dashBoardFragment);
            setTitle("Home");
//            replaceFragment(new DashBoardFragment(), "Dashboard");
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.db_cart && loggedIn()) {
            startActivity(new Intent(this, CartActivity.class));
        } else if (item.getItemId() == R.id.db_notification && loggedIn()) {
            startActivity(new Intent(this, NotificationActivity.class));
        } else {
            showAlert();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView_parent(position);
    }

    private void displayView_parent(int position) {
        switch (position) {

            case 0:
                showFragment(dashBoardFragment);
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                setTitle("Home");
                if (activeFragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    int backCount = fragmentManager.getBackStackEntryCount();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
                    if (backCount == 1) {
                        fragmentManager.popBackStack();
                    }
                    fragmentTransaction.replace(R.id.appPageHolder, activeFragment);
                    fragmentTransaction.commit();
                }
                break;
            case 1:
                if (loggedIn()) {
                    setTitle("Cart");
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);
                } else {
                    showAlert();
                }

                break;
            case 2:
                if (loggedIn()) {
                    setTitle("Orders");
                    showFragment(ordersFragment);
//                        replaceFragment(new OrdersFragment(), ORDERS);
                    bottomNavigationView.getMenu().getItem(1).setChecked(true);

                } else {
                    showAlert();
                }
                break;
            case 3:
                setTitle("Product Listing");
                break;
            case 4:
                if (loggedIn()) {
                    setTitle("Payment History");
                    showFragment(paymentFragment);
//                        replaceFragment(new PaymentFragment(), WALLET);
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                } else {
                    showAlert();
                }
                break;
            case 5:
                if (loggedIn()) {
                    setTitle("My Account");
                    startActivity(new Intent(this, ProfileActivity.class));
                } else {
                    showAlert();
                }

                break;

            default:
                break;
        }

    }

}