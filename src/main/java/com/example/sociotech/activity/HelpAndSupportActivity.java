package com.example.sociotech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.sociotech.R;
import com.example.sociotech.databinding.ActivityHelpAndSupportBinding;

public class HelpAndSupportActivity extends AppCompatActivity {
    private ActivityHelpAndSupportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help_and_support);
        binding.tool.header.setText(R.string.help_and_support);
        binding.tool.navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpAndSupportActivity.this, HomeActivity.class));
                finish();
            }
        });
    }
}

