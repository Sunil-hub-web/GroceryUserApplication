package com.kissanfactory.groceryuserapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.kissanfactory.groceryuserapplication.Fragments.PrivacyPolicyFragmen;
import com.kissanfactory.groceryuserapplication.Fragments.RefundPolicyFragment;
import com.kissanfactory.groceryuserapplication.Fragments.TermsAndCondotionFragment;
import com.kissanfactory.groceryuserapplication.R;
import com.google.android.material.tabs.TabLayout;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        init();
    }

    private void init(){
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        if(getSupportActionBar()==null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");

            setTitle("Terms & Condition");
        }

        replaceFragmentView(getIntent().getIntExtra("position", 0));
        tabLayout.selectTab(tabLayout.getTabAt(getIntent().getIntExtra("position", 0)));
        tabItems();
    }

    // setting up the tab item click functionality
    private void tabItems(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                replaceFragmentView(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // replacing the fragments
    private void replaceFragmentView(int position){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment myFragment = new TermsAndCondotionFragment();
        String title = "Terms & Condition";
        switch(position){
            case 0:
                myFragment = new TermsAndCondotionFragment();
                title = "Terms & Condition";
                break;
            case 1:
                myFragment = new PrivacyPolicyFragmen();
                title = "Privacy Policy";
                break;
            case 2:
                myFragment = new RefundPolicyFragment();
                title = "Refund Policy";
                break;
        }
        transaction.replace(R.id.policyHolder, myFragment);
        transaction.commit();

        setTitle(title);
    }

    // setting the toolbar title
    private void setTitle(String title){
        TextView titletxt = toolbar.findViewById(R.id.tb_title);
        titletxt.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}