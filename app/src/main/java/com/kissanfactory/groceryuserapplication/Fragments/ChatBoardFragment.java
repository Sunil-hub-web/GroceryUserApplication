package com.kissanfactory.groceryuserapplication.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.kissanfactory.groceryuserapplication.R;


public class ChatBoardFragment extends Fragment {

    private WebView mywebview;
    private Button button4;
    String url = "https://kisaanandfactory.com/chat-board";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_board, container, false);


//        button4 = view.findViewById(R.id.button4);
        mywebview = (WebView) view.findViewById(R.id.webView);


        mywebview.setWebViewClient(new WebViewClient());
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.loadUrl(url);
        mywebview.getSettings().setUseWideViewPort(true);
        mywebview.getSettings().setLoadWithOverviewMode(true);

        return view;
    }

}