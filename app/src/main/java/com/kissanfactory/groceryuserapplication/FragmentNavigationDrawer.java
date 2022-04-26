package com.kissanfactory.groceryuserapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kissanfactory.groceryuserapplication.Activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class FragmentNavigationDrawer extends Fragment {

    //    public static final Integer[] images = {
//            R.drawable.ic_icon_language,
//            R.drawable.ic_icon_gallery,
//            R.drawable.ic_icon_videocam,
//            R.drawable.ic_icon_lock_apps,
//            R.drawable.ic_icon_key,
//            R.drawable.ic_icon_unlock,
//            R.drawable.ic_icon_theme,
//            R.drawable.ic_icon_share,
//
//
//    };
    private static String TAG = FragmentNavigationDrawer.class.getSimpleName();
    private static String[] titles = null;
    TextView txt_logout;
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private FragmentDrawerListener drawerListener;
    ImageView imgCusImage, menu_icon_side;
    public static TextView sideBarText, sideBaraddress;
    RelativeLayout header_view_one, header_view_two;
    View layout;
    ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    public static CircleImageView img_user;

    public FragmentNavigationDrawer() {

    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();


        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            //  navItem.setImageID(images[i]);
            data.add(navItem);
        }
        return data;
    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        layout = inflater.inflate(R.layout.nav_header_main, container, false);
        sharedPreferences = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        try {
            WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            System.out.println("width..." + width);
            System.out.println("width..." + height);
//            if (width == 480 && height == 800) {
//                header_view_one = (RelativeLayout) layout.findViewById(R.id.header_one);
//                header_view_one.setVisibility(View.GONE);
//                header_view_two.setVisibility(View.VISIBLE);
//            }
        } catch (Exception e) {

        }

        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        txt_logout = layout.findViewById(R.id.txt_logout);
        sideBarText = layout.findViewById(R.id.sideBarText);
        menu_icon_side = layout.findViewById(R.id.menu_icon_side);
        img_user = layout.findViewById(R.id.img_user);
        sideBaraddress = layout.findViewById(R.id.sideBaraddress);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_log_out);
        if (loggedIn()) {
            txt_logout.setVisibility(View.VISIBLE);
        } else {
            txt_logout.setVisibility(View.GONE);

        }

        txt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        menu_icon_side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(containerView);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return layout;
    }

    private void logout() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_log_out);

        TextView logout = dialog.findViewById(R.id.tv_Logout);
        TextView cancel = dialog.findViewById(R.id.tv_Cancel);

        logout.setOnClickListener(view -> {
            // clear shared prefs
            clearSharedPrefs();
            // open the login activity
            dialog.dismiss();
            startActivity(new Intent(getContext(), MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            getActivity().finish();
        });

        cancel.setOnClickListener(view -> {
            // close dialog
            dialog.dismiss();
        });
        dialog.create();

        dialog.show();
    }

    private void clearSharedPrefs() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove("token");
        editor.putBoolean("exist", false);

        editor.apply();
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                // toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        //  toolbar.getBackground().setAlpha(0);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
                //  toolbar.setNavigationIcon(R.drawable.ic_menu);

            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    // check if user is logged in
    private boolean loggedIn() {
        return sharedPreferences.getBoolean("exist", false);
    }

    // showing the user name in side bar


}
