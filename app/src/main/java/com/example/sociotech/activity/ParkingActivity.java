package com.example.sociotech.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sociotech.R;
import com.example.sociotech.adapter.ParkingAdapter;
import com.example.sociotech.api.ApiService;
import com.example.sociotech.api.NetworkCall;
import com.example.sociotech.databinding.ActivityParkingBinding;
import com.example.sociotech.model.flatdetails.BlockResponse;
import com.example.sociotech.model.flatdetails.BlocksDatum;
import com.example.sociotech.model.flatdetails.FlatDatum;
import com.example.sociotech.model.flatdetails.FlatResponse;
import com.example.sociotech.model.login.LoginResponse;
import com.example.sociotech.model.parking.addParking.PostParkingResponse;
import com.example.sociotech.model.parking.getparking.GetParkingResponse;
import com.example.sociotech.model.parking.parkingAvailableSlots.AvailableSlots;
import com.example.sociotech.model.parking.parkingAvailableSlots.ParkingAvailableSlotsResponse;
import com.example.sociotech.utils.SharedPrefernces;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParkingActivity extends AppCompatActivity {
    private ActivityParkingBinding binding;
    private ApiService apiService;
    private ParkingAdapter adapter;
    String block_id,fllat_id,parking_id,allocation_date,expiration_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_parking);
        binding.tool.header.setText(R.string.my_parking);
        recyclerView();
        hit_getParkingApi();


        binding.tool.navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ParkingActivity.this, HomeActivity.class));
                finish();
            }
        });

        binding.newParkingRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llParking.setVisibility(View.GONE);
                binding.lladdParking.setVisibility(View.VISIBLE);
                hit_getAvailableSlots_api();
                hit_getFlats_api();
            }

        });

        binding.startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(binding.startDate);
            }
        });

        binding.endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(binding.endDate);
            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hit_submitParkingRequestAPI();
               /* if (!fllat_id.equals("") && !block_id.equals("") && !allocation_date.equals("") && !expiration_date.equals(""))
                {

                }
                else {
                    Toast.makeText(ParkingActivity.this, "Please select all fields", Toast.LENGTH_SHORT).show();
                }*/

            }
        });
    }

    private void hit_submitParkingRequestAPI() {
        apiService = NetworkCall.getRetrofitInstance(ParkingActivity.this).create(ApiService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("block_id", block_id);
        jsonObject.addProperty("flat_id", fllat_id);
        jsonObject.addProperty("parking_id", parking_id);
        allocation_date=binding.startDate.getText().toString();
        expiration_date=binding.endDate.getText().toString();
        jsonObject.addProperty("allocation_date", allocation_date);
        jsonObject.addProperty("expiration_date", expiration_date);

        Call<PostParkingResponse> call = apiService.addParking(jsonObject);
        call.enqueue(new Callback<PostParkingResponse>() {
            @Override
            public void onResponse(Call<PostParkingResponse> call, Response<PostParkingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PostParkingResponse loginResponse = response.body();


                    Toast.makeText(ParkingActivity.this, "Request Submitted sucessfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ParkingActivity.this, HomeActivity.class));
                    finish();


                } else {
                    Toast.makeText(ParkingActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostParkingResponse> call, Throwable t) {
                Toast.makeText(ParkingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Login error", t);
            }
        });
    }

    private void openDatePicker(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ParkingActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = year  + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        editText.setText(selectedDate);
                    }
                },
                year, month, day
        );

        // Disable back dates
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }


    private void hit_getAvailableSlots_api() {
        apiService = NetworkCall.getRetrofitInstance(ParkingActivity.this).create(ApiService.class);
        try {
            Call<ParkingAvailableSlotsResponse> call = apiService.getAvailableSlots();
            call.enqueue(new Callback<ParkingAvailableSlotsResponse>() {
                @Override
                public void onResponse(Call<ParkingAvailableSlotsResponse> call, Response<ParkingAvailableSlotsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ParkingAvailableSlotsResponse flatResponse = response.body();
                        setSpinnerAvailableSlots(flatResponse.getData());
                    } else {
                        Toast.makeText(ParkingActivity.this, " Failed", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<ParkingAvailableSlotsResponse> call, Throwable t) {
                    Toast.makeText(ParkingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    private void hit_getFlats_api() {
        apiService = NetworkCall.getRetrofitInstance(ParkingActivity.this).create(ApiService.class);
        try {
            Call<FlatResponse> call = apiService.getFlats();
            call.enqueue(new Callback<FlatResponse>() {
                @Override
                public void onResponse(Call<FlatResponse> call, Response<FlatResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        FlatResponse flatResponse = response.body();
                        setSpinner(flatResponse.getData());
                    } else {
                        Toast.makeText(ParkingActivity.this, " Failed", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<FlatResponse> call, Throwable t) {
                    Toast.makeText(ParkingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "Login error", t);
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void hit_getBlocksApi() {
        apiService = NetworkCall.getRetrofitInstance(ParkingActivity.this).create(ApiService.class);
        try {
            Call<BlockResponse> call = apiService.getBlocks();
            call.enqueue(new Callback<BlockResponse>() {
                @Override
                public void onResponse(Call<BlockResponse> call, Response<BlockResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        BlockResponse blockResponse = response.body();
                        setSpinnerBlocks(blockResponse.getData());
                    } else {
                        Toast.makeText(ParkingActivity.this, " Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BlockResponse> call, Throwable t) {
                    Toast.makeText(ParkingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "Login error", t);
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void setSpinner(List<FlatDatum> data) {
        List<String> items = new ArrayList<>();
        items.add("Select Flat");
        for (int i = 0; i < data.size(); i++) {
            items.add(data.get(i).getName());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFlats.setAdapter(adapter);

        binding.spinnerFlats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!parent.getItemAtPosition(position).toString().equals("Select Flat"))
                {
                   /* FlatDatum flatDatum= (FlatDatum) parent.getSelectedItem();
                    fllat_id = String.valueOf(flatDatum.getId());*/
                    for (int i = 0; i < items.size(); i++) {
                        if (parent.getItemAtPosition(position).toString().equals(items.get(i))) {
                            fllat_id= String.valueOf(data.get(i).getId());
                            break;
                        }
                    }
                }
                hit_getBlocksApi();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected
            }
        });
    }

    private void setSpinnerBlocks(List<BlocksDatum> data) {
        List<String> items = new ArrayList<>();
        items.add("Select Block");

        for (int i = 0; i < data.size(); i++) {
            items.add(data.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerBlocks.setAdapter(adapter);
        binding.spinnerBlocks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals("Select Block"))
                {
                   /* BlocksDatum blocksDatum= (BlocksDatum) parent.getSelectedItem();
                    block_id = String.valueOf(blocksDatum.getId());*/
                    for (int i = 0; i < items.size(); i++) {
                        if (parent.getItemAtPosition(position).toString().equals(items.get(i))) {
                            block_id= String.valueOf(data.get(i).getId());
                            break;
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected
            }
        });
    }

    private void setSpinnerAvailableSlots(List<AvailableSlots> data) {
        List<String> items = new ArrayList<>();
        items.add("Select Slot");

        for (int i = 0; i < data.size(); i++) {
            items.add(data.get(i).getSpotNumber());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAvailableSlots.setAdapter(adapter);
        binding.spinnerAvailableSlots.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!parent.getItemAtPosition(position).toString().equals("Select Slot"))
                {
                    for (int i = 0; i < items.size(); i++) {
                        if (parent.getItemAtPosition(position).toString().equals(items.get(i))) {
                           parking_id= String.valueOf(data.get(i).getId());
                            break;
                        }
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected
            }
        });
    }


    private void recyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvParking.setLayoutManager(layoutManager);
    }

    private void hit_getParkingApi() {
        apiService = NetworkCall.getRetrofitInstance(ParkingActivity.this).create(ApiService.class);
        try {
            Call<GetParkingResponse> call = apiService.getmyParking();
            call.enqueue(new Callback<GetParkingResponse>() {
                @Override
                public void onResponse(Call<GetParkingResponse> call, Response<GetParkingResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        GetParkingResponse getVisitorResponse = response.body();
                        adapter = new ParkingAdapter(ParkingActivity.this, getVisitorResponse.getData());
                        binding.rvParking.setAdapter(adapter);
                        //  Toast.makeText(MeetVisitorActivity.this, "Login sucess", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ParkingActivity.this, "Login ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GetParkingResponse> call, Throwable t) {
                    Toast.makeText(ParkingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "Login error", t);
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}

