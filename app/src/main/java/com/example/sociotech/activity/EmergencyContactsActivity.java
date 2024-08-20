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
import com.example.sociotech.adapter.EmergencyContactsAdapter;
import com.example.sociotech.api.ApiService;
import com.example.sociotech.api.NetworkCall;
import com.example.sociotech.databinding.ActivityEmergencyContactsBinding;
import com.example.sociotech.model.emergencyContacts.EmergencyContactsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmergencyContactsActivity extends AppCompatActivity {
    private ActivityEmergencyContactsBinding binding;
    private ApiService apiService;
    private EmergencyContactsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_emergency_contacts);
        binding.tool.header.setText(R.string.emergency_contacts);
        recyclerView();
        hit_emergencyContacts_Api();
        binding.tool.navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmergencyContactsActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    private void recyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvContacts.setLayoutManager(layoutManager);
    }

    private void hit_emergencyContacts_Api() {
        apiService = NetworkCall.getRetrofitInstance(EmergencyContactsActivity.this).create(ApiService.class);
        try {
            Call<EmergencyContactsResponse> call = apiService.getEmergencyContacts();
            call.enqueue(new Callback<EmergencyContactsResponse>() {
                @Override
                public void onResponse(Call<EmergencyContactsResponse> call, Response<EmergencyContactsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        EmergencyContactsResponse emergencyContactsResponse = response.body();
                        adapter = new EmergencyContactsAdapter(EmergencyContactsActivity.this, emergencyContactsResponse.getData());
                        binding.rvContacts.setAdapter(adapter);
                    } else {
                        Toast.makeText(EmergencyContactsActivity.this, " Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<EmergencyContactsResponse> call, Throwable t) {
                    Toast.makeText(EmergencyContactsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", " error", t);
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}

