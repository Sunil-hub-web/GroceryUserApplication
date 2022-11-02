package com.kissanfactory.groceryuserapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kissanfactory.groceryuserapplication.R;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

       // getTransactionId();
    }

    // getting the transaction id
   /* private void getTransactionId(){
        String transaction = getIntent().getStringExtra("transaction");
        TextView transIdView = findViewById(R.id.refNo);
        transIdView.setText("Your Transaction ID is: \n"+ transaction);
    }*/

    public void continueShopping(View view){
        startActivity(new Intent(this, MainActivity.class)
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
        this.finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
        this.finish();
    }
}