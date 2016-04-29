package com.sncf.itif.Services.PlanIDF;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sncf.itif.R;

public class ActCartoMetro extends AppCompatActivity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_carto_metro);

        getSupportActionBar().setTitle(getResources().getString(R.string.act_carto_metro_tv_title));
        getSupportActionBar().setSubtitle(R.string.global_tv_short_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.webViewCarto);

        webView.setWebViewClient(new WebViewClient());

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        //Enable Javascript
        webView.getSettings().setJavaScriptEnabled(true);
        //This line block ads
        webView.getSettings().setUserAgentString("Android");

        webView.loadUrl(getResources().getString(R.string.act_carto_metro_api_url));
        webView.clearCache(true);


    }
}