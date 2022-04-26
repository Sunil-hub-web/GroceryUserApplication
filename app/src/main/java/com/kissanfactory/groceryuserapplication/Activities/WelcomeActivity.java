package com.kissanfactory.groceryuserapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kissanfactory.groceryuserapplication.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void login(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void register(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }
}