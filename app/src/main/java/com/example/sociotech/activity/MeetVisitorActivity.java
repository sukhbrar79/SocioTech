package com.example.sociotech.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sociotech.R;
import com.example.sociotech.adapter.MeetVisitorAdapter;
import com.example.sociotech.api.ApiService;
import com.example.sociotech.api.NetworkCall;
import com.example.sociotech.databinding.ActivityMeetVisitorBinding;
import com.example.sociotech.model.flatdetails.BlockResponse;
import com.example.sociotech.model.flatdetails.BlocksDatum;
import com.example.sociotech.model.flatdetails.FlatDatum;
import com.example.sociotech.model.flatdetails.FlatResponse;
import com.example.sociotech.model.meetVisitor.addVisitor.PostVisitorResponse;
import com.example.sociotech.model.meetVisitor.getVisitor.GetVisitorResponse;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetVisitorActivity extends AppCompatActivity {
    private ActivityMeetVisitorBinding binding;
    private ApiService apiService;
    private MeetVisitorAdapter adapter;
    String block_id, fllat_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_meet_visitor);
        binding.tool.header.setText(R.string.meet_visitor);
        recyclerView();
        hit_get_visitors_api();
        hit_getFlats_api();

        binding.newVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llVisitors.setVisibility(View.GONE);
                binding.lladdVisitors.setVisibility(View.VISIBLE);
            }
        });


        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.name.getText().toString().equals("")
                        & !binding.contact.getText().toString().equals("")
                        & !binding.purpose.getText().toString().equals("")
                        & !binding.startDate.getText().toString().equals("")
                        & !binding.endDate.getText().toString().equals("")) {
                    hit_add_visitor_api();
                } else {
                    binding.error.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.tool.navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MeetVisitorActivity.this, HomeActivity.class));
                finish();
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


        binding.startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker(binding.startTime);

            }

        });

        binding.endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker(binding.endTime);
            }
        });

    }


    private void openTimePicker(EditText editText) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(MeetVisitorActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        editText.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, true);
        timePickerDialog.show();


    }

    private void openDatePicker(EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MeetVisitorActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        editText.setText(selectedDate);
                    }
                },
                year, month, day
        );

        // Disable back dates
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    private void hit_get_visitors_api() {
        apiService = NetworkCall.getRetrofitInstance(MeetVisitorActivity.this).create(ApiService.class);
        try {
            Call<GetVisitorResponse> call = apiService.getmyVisitors();
            call.enqueue(new Callback<GetVisitorResponse>() {
                @Override
                public void onResponse(Call<GetVisitorResponse> call, Response<GetVisitorResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        GetVisitorResponse getVisitorResponse = response.body();
                        adapter = new MeetVisitorAdapter(MeetVisitorActivity.this, getVisitorResponse.getData());
                        binding.rv.setAdapter(adapter);
                    } else {
                        Toast.makeText(MeetVisitorActivity.this, " visitors Failed", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<GetVisitorResponse> call, Throwable t) {
                    Toast.makeText(MeetVisitorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "Login error", t);
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void hit_getFlats_api() {
        apiService = NetworkCall.getRetrofitInstance(MeetVisitorActivity.this).create(ApiService.class);
        try {
            Call<FlatResponse> call = apiService.getFlats();
            call.enqueue(new Callback<FlatResponse>() {
                @Override
                public void onResponse(Call<FlatResponse> call, Response<FlatResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        FlatResponse flatResponse = response.body();
                        setSpinner(flatResponse.getData());
                    } else {
                        Toast.makeText(MeetVisitorActivity.this, "flat  Failed", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<FlatResponse> call, Throwable t) {
                    Toast.makeText(MeetVisitorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "Login error", t);
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void hit_getBlocksApi() {
        apiService = NetworkCall.getRetrofitInstance(MeetVisitorActivity.this).create(ApiService.class);
        try {
            Call<BlockResponse> call = apiService.getBlocks();
            call.enqueue(new Callback<BlockResponse>() {
                @Override
                public void onResponse(Call<BlockResponse> call, Response<BlockResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        BlockResponse blockResponse = response.body();
                        setSpinnerBlocks(blockResponse.getData());
                    } else {
                        Toast.makeText(MeetVisitorActivity.this, " block Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BlockResponse> call, Throwable t) {
                    Toast.makeText(MeetVisitorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

                if (!parent.getItemAtPosition(position).toString().equals("Select Flat")) {
                   /* FlatDatum flatDatum= (FlatDatum) parent.getSelectedItem();
                    fllat_id = String.valueOf(flatDatum.getId());*/
                    for (int i = 0; i < items.size(); i++) {
                        if (parent.getItemAtPosition(position).toString().equals(items.get(i))) {
                            fllat_id = String.valueOf(data.get(i).getId());
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
                if (!parent.getItemAtPosition(position).toString().equals("Select Block")) {
                   /* BlocksDatum blocksDatum= (BlocksDatum) parent.getSelectedItem();
                    block_id = String.valueOf(blocksDatum.getId());*/
                    for (int i = 0; i < items.size(); i++) {
                        if (parent.getItemAtPosition(position).toString().equals(items.get(i))) {
                            block_id = String.valueOf(data.get(i).getId());
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
        binding.rv.setLayoutManager(layoutManager);
    }

    private void hit_add_visitor_api() {
        apiService = NetworkCall.getRetrofitInstance(MeetVisitorActivity.this).create(ApiService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("block_id", block_id);
        jsonObject.addProperty("flat_id", fllat_id);
        jsonObject.addProperty("name", binding.name.getText().toString());
        jsonObject.addProperty("contact_number", binding.contact.getText().toString());
        jsonObject.addProperty("vehicle_number", "");
        jsonObject.addProperty("purpose", binding.purpose.getText().toString());
        jsonObject.addProperty("check_out_date", binding.endDate.getText().toString());
        jsonObject.addProperty("check_in_date", binding.startDate.getText().toString());

        try {
            Call<PostVisitorResponse> call = apiService.addVisitor(jsonObject);
            call.enqueue(new Callback<PostVisitorResponse>() {
                @Override
                public void onResponse(Call<PostVisitorResponse> call, Response<PostVisitorResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        PostVisitorResponse getVisitorResponse = response.body();
                        hit_get_visitors_api();
                        binding.llVisitors.setVisibility(View.VISIBLE);
                        binding.lladdVisitors.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(MeetVisitorActivity.this, "  add visitor Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PostVisitorResponse> call, Throwable t) {
                    Toast.makeText(MeetVisitorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", " error", t);
                }
            });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

