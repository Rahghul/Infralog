package com.sncf.itif.Services.PlanIDF;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sncf.itif.R;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

public class WebViewCartoActivity extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_carto);

        webView = (WebView) findViewById(R.id.webViewCarto);

        webView.setWebViewClient(new WebViewClient());

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        //Enable Javascript
        webView.getSettings().setJavaScriptEnabled(true);
        //This line block ads
        webView.getSettings().setUserAgentString("Android");

        webView.loadUrl(getResources().getString(R.string.url_carto_metro));
        webView.clearCache(true);


    }
}