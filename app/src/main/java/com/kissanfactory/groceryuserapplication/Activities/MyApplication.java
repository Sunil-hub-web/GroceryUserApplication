package com.kissanfactory.groceryuserapplication.Activities;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;


/**
 * Created by Devendra on 28/06/18.
 */
//extent via application for volley and other global instant use in app..
public class MyApplication extends Application {
    public static final String TAG = MyApplication.class
            .getSimpleName();

    private static MyApplication mInstance;
  //  private RequestQueue mRequestQueue;
    // private SessionManager sessionManager;
    private static boolean activityVisible = false;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //  super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
  //     MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
     //  MultiDex.install(this);
        mInstance = this;
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        //  sessionManager = new SessionManager(this);
    }
//
//    public RequestQueue getRequestQueue() {
//        if (mRequestQueue == null) {
//            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
//        }
//        return mRequestQueue;
//    }
//
//    public <T> void addToRequestQueue(Request<T> req, String tag) {
//        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
//        getRequestQueue().add(req);
//    }
//
//    public <T> void addToRequestQueue(Request<T> req) {
//        req.setTag(TAG);
//        getRequestQueue().add(req);
//    }
//
//    public void cancelPendingRequests(Object tag) {
//        if (mRequestQueue != null) {
//            mRequestQueue.cancelAll(tag);
//        }
//    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = true;
    }

    public static void activityStop() {
        activityVisible = true;
    }

    public static void activity_distroy() {
        activityVisible = false;
    }

}