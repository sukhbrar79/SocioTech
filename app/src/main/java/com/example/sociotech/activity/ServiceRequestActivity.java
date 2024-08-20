package com.example.sociotech.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sociotech.R;
import com.example.sociotech.adapter.ServiceRequestAdapter;
import com.example.sociotech.api.ApiService;
import com.example.sociotech.api.NetworkCall;
import com.example.sociotech.databinding.ActivityServiceRequestBinding;
import com.example.sociotech.model.serviceRequest.postservicRequestModel.PostServiceRequestResponse;
import com.example.sociotech.model.serviceRequest.serviceRequestListingResponse.ServiceRequestResponse;
import com.example.sociotech.utils.ProgressBarr;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceRequestActivity extends AppCompatActivity {
    private ActivityServiceRequestBinding binding;
    private ApiService apiService;
    private ServiceRequestAdapter adapter;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_request);
        binding.tool.header.setText(R.string.service_request);
        setSpinner();
        recyclerView();
        dialog = ProgressBarr.showProgress(ServiceRequestActivity.this);
        hit_get_serviceRequest_Api();

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.subject.getText().toString().equals("") && binding.description.getText().toString().equals("")) {
                    binding.error.setVisibility(View.VISIBLE);
                } else {
                    hit_serviceRequest_Api();
                }
            }
        });

        binding.tool.navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ServiceRequestActivity.this, HomeActivity.class));
                finish();
            }
        });

        binding.newServiceRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llserviceRequests.setVisibility(View.GONE);
                binding.llAddRequest.setVisibility(View.VISIBLE);
            }
        });

    }

    private void setSpinner() {
        List<String> items = new ArrayList<>();
        items.add("low");
        items.add("medium");
        items.add("high");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinner.setAdapter(adapter);

        // Optional: Handle spinner item selection
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the selected item
                String selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected
            }
        });
    }

    private void recyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvservice.setLayoutManager(layoutManager);
    }


    private void hit_serviceRequest_Api() {
        apiService = NetworkCall.getRetrofitInstance(ServiceRequestActivity.this).create(ApiService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("block_id", 2);
        jsonObject.addProperty("flat_id", 3);
        jsonObject.addProperty("subject", binding.subject.getText().toString());
        jsonObject.addProperty("description", binding.description.getText().toString());
        jsonObject.addProperty("priority", "low");

        try {
            Call<PostServiceRequestResponse> call = apiService.serviceRequest(jsonObject);

            call.enqueue(new Callback<PostServiceRequestResponse>() {
                @Override
                public void onResponse(Call<PostServiceRequestResponse> call, Response<PostServiceRequestResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        PostServiceRequestResponse serviceResponse = response.body();
                        dialog = ProgressBarr.showProgress(ServiceRequestActivity.this);
                        hit_get_serviceRequest_Api();
                        binding.llAddRequest.setVisibility(View.GONE);
                        binding.llserviceRequests.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ServiceRequestActivity.this, " Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PostServiceRequestResponse> call, Throwable t) {
                    Toast.makeText(ServiceRequestActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", " error", t);
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void hit_get_serviceRequest_Api() {
        apiService = NetworkCall.getRetrofitInstance(ServiceRequestActivity.this).create(ApiService.class);
        try {
            Call<ServiceRequestResponse> call = apiService.serviceRequestStatus();
            call.enqueue(new Callback<ServiceRequestResponse>() {
                @Override
                public void onResponse(Call<ServiceRequestResponse> call, Response<ServiceRequestResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        dialog.dismiss();
                        ServiceRequestResponse loginResponse = response.body();
                        adapter = new ServiceRequestAdapter(ServiceRequestActivity.this, loginResponse.getData());
                        binding.rvservice.setAdapter(adapter);
                    } else {
                        Toast.makeText(ServiceRequestActivity.this, " Failed", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ServiceRequestResponse> call, Throwable t) {
                    Toast.makeText(ServiceRequestActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", " error", t);
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}