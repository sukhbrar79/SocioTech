package com.example.sociotech.activity;


import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.sociotech.R;
import com.example.sociotech.databinding.CheckoutBinding;

public class WebviewActivity extends AppCompatActivity {
    private CheckoutBinding binding;
    private static final String SUCCESS_URL = "https://www.example.com/payment-success"; // Replace with your payment gateway's success URL


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.checkout);
        Intent intent = getIntent();

        // Retrieving the data passed
        String url = intent.getStringExtra("url");
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();

                if (url.equals(SUCCESS_URL)) {
                    startActivity(new Intent(WebviewActivity.this, InvoicesActivity.class));
                    finish(); // Close this activity and return to the previous one
                    return true;
                }

                // Allow WebView to load the URL
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        // Load the payment URL (replace with your actual payment URL)
        binding.webView.loadUrl(url);
    }

}

