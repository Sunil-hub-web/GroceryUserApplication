package com.kissanfactory.groceryuserapplication.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.kissanfactory.groceryuserapplication.R;


public class PrivacyPolicyFragmen extends Fragment {

    public PrivacyPolicyFragmen() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        WebView webView = view.findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/privacy.html");
        return view;
    }

}