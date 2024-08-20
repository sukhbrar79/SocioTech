package com.example.sociotech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sociotech.R;
import com.example.sociotech.adapter.GuidelinesAdapter;
import com.example.sociotech.adapter.InvoicesAdapter;
import com.example.sociotech.api.ApiService;
import com.example.sociotech.api.NetworkCall;
import com.example.sociotech.databinding.ActivityGuidelinesBinding;
import com.example.sociotech.databinding.ActivityInvoiceBinding;
import com.example.sociotech.model.guidelines.GuidelinesResponse;
import com.example.sociotech.model.invoices.InvoicesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuidelinesActivity extends AppCompatActivity {
    private ActivityGuidelinesBinding binding;
    private ApiService apiService;
    private GuidelinesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guidelines);
        hit_guidelines_api();
        binding.tool.header.setText(R.string.guideline);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rv.setLayoutManager(layoutManager);
        binding.tool.navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuidelinesActivity.this, HomeActivity.class));
                finish();
            }
        });

    }

    private void hit_guidelines_api() {
        apiService = NetworkCall.getRetrofitInstance(GuidelinesActivity.this).create(ApiService.class);
        try {
            Call<GuidelinesResponse> call = apiService.getGuidelines();
            call.enqueue(new Callback<GuidelinesResponse>() {
                @Override
                public void onResponse(Call<GuidelinesResponse> call, Response<GuidelinesResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        GuidelinesResponse guidelinesResponse = response.body();
                        adapter = new GuidelinesAdapter(GuidelinesActivity.this, guidelinesResponse.getData());
                        binding.rv.setAdapter(adapter);
                    } else {
                        Toast.makeText(GuidelinesActivity.this, " Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GuidelinesResponse> call, Throwable t) {
                    Toast.makeText(GuidelinesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}

