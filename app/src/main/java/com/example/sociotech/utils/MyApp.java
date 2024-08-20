package com.example.sociotech.utils;

import android.app.Application;

import com.stripe.android.PaymentConfiguration;

public class MyApp  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(
                getApplicationContext(),
                ""
        );
    }
}