package com.example.sociotech.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sociotech.R;
import com.example.sociotech.adapter.InvoicesAdapter;
import com.example.sociotech.adapter.ServiceRequestAdapter;
import com.example.sociotech.api.ApiService;
import com.example.sociotech.api.NetworkCall;
import com.example.sociotech.databinding.ActivityInvoiceBinding;
import com.example.sociotech.model.invoices.InvoicesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoicesActivity extends AppCompatActivity {
    private ActivityInvoiceBinding binding;
    private ApiService apiService;
    private InvoicesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invoice);
        recyclerView();
        hit_serviceRequest_Api();
        binding.tool.header.setText(R.string.my_invoices);
        binding.tool.navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InvoicesActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    private void recyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rv.setLayoutManager(layoutManager);
    }

    private void hit_serviceRequest_Api() {
        apiService = NetworkCall.getRetrofitInstance(InvoicesActivity.this).create(ApiService.class);
        try {
            Call<InvoicesResponse> call = apiService.getInvoice();
            call.enqueue(new Callback<InvoicesResponse>() {
                @Override
                public void onResponse(Call<InvoicesResponse> call, Response<InvoicesResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        InvoicesResponse invoicesResponse = response.body();
                        adapter = new InvoicesAdapter(InvoicesActivity.this, invoicesResponse.getData());
                        binding.rv.setAdapter(adapter);
                    } else {
                        Toast.makeText(InvoicesActivity.this, " Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<InvoicesResponse> call, Throwable t) {
                    Toast.makeText(InvoicesActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}