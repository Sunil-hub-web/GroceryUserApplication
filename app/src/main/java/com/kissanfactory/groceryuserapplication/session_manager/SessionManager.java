package com.kissanfactory.groceryuserapplication.session_manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.kissanfactory.groceryuserapplication.Activities.LoginActivity;
import com.google.gson.Gson;


public class SessionManager {
    private static final String PREF_NAME = "KissanFactoryUSER";
    private static final String IS_LOGIN = "IsLogin";
    private static final String USER_DETAIL = "user_info";
    public static final String DEVICE_TOKEN = "DEVICE_TOKEN";
    public static final String FCM_TOKEN = "FCM_TOKEN";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    int PRIVATE_MODE = 0;
    Context _context;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;


    public SessionManager(Context context) {
        this._context = context;
        this.preferences = this._context.getSharedPreferences(PREF_NAME, this.PRIVATE_MODE);
        this.editor = this.preferences.edit();
    }

    public void login() {
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }

    // Get Installed State...
    public boolean isLogin() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public void Logout() {
        //clear alluser data
        editor.clear();
        editor.commit();
// after logout user goes to login screen
        Intent intent = new Intent(_context, LoginActivity.class);
        intent.putExtra("come_from_one", "no");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);

    }


    public void setDeviceToken(String vDeviceToken) {
        this.editor.putString(DEVICE_TOKEN, vDeviceToken);
        editor.commit();
    }

    public String getDeviceToken() {
        String deviceToken = preferences.getString(DEVICE_TOKEN, "");
        return deviceToken;
    }

    public void setName(String vName) {
        this.editor.putString(USER_NAME, vName);
        editor.commit();
    }

    public String getName() {
        String deviceToken = preferences.getString(USER_NAME, "");
        return deviceToken;
    }

    public void setFCM_TOKEN(String vFCM_TOKEN) {
        this.editor.putString(FCM_TOKEN, vFCM_TOKEN);
        editor.commit();
    }

    public String getFCM_TOKEN() {
        String vFCM_TOKEN = preferences.getString(FCM_TOKEN, "");
        return vFCM_TOKEN;
    }

    public Object getUser_details() {
        Gson gson = new Gson();
        String vJson = preferences.getString(USER_DETAIL, "");
        Object user_details_dto = gson.fromJson(vJson, Object.class);
        return user_details_dto;
    }

    public void setUserDetail(Object userDTO) {
        // Storing login value as TRUE
        Gson gson = new Gson();
        String vUserDTO = gson.toJson(userDTO);
        editor.putString(USER_DETAIL, vUserDTO);
        // commit changes
        editor.commit();
    }
}
