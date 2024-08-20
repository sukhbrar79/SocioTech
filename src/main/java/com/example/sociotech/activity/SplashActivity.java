package com.example.sociotech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sociotech.R;
import com.example.sociotech.utils.SharedPref;
import com.example.sociotech.utils.SharedPrefernces;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 1000; // Duration for the splash screen (1 seconds)
    private static SharedPref sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Handler to start the next activity after the splash screen duration
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPref=new SharedPref(SplashActivity.this);
                String is_login = sharedPref.getStringData("is_login","no");
                if (is_login.equals("yes")) {
                    Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();

                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
    }

