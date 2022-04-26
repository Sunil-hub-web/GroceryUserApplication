package com.kissanfactory.groceryuserapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.kissanfactory.groceryuserapplication.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Handler handler = new Handler();
        handler.postDelayed(() -> startActivity(new Intent(this, MainActivity.class)
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK)), 3000);
    }

    // cehecking if there is a user token in the shared prefs
    private void checkToken(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("exist", false)){
            // open the main activity
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

        }else{
            // ask the user to login
            startActivity(new Intent(SplashScreenActivity.this, WelcomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}