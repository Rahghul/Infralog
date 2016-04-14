package com.sncf.itif.Services.PlanIDF;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.sncf.itif.R;

public class WebViewCartoActivity extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_carto);

        webView = (WebView) findViewById(R.id.webViewCarto);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://carto.metro.free.fr/cartes/rer-idf/");

    }
}