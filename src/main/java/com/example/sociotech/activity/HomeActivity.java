package com.example.sociotech.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sociotech.R;
import com.example.sociotech.adapter.HomeAdapter;
import com.example.sociotech.api.ApiService;
import com.example.sociotech.api.NetworkCall;
import com.example.sociotech.databinding.ActivityHomeBinding;
import com.example.sociotech.model.announcements.AnnouncementsResponse;
import com.example.sociotech.model.logout.LogoutResponse;
import com.example.sociotech.model.profile.DataClass;
import com.example.sociotech.model.profile.MyProfileResponse;
import com.example.sociotech.utils.SharedPref;
import com.example.sociotech.utils.SharedPrefernces;
import com.google.android.material.navigation.NavigationView;
import com.stripe.android.PaymentConfiguration;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActivityHomeBinding binding;
    private ApiService apiService;
    SharedPrefernces sharedPrefernces;
    SharedPref sharedPref;
    DataClass dataClass;
    private TextView name, address, mobile;
    LinearLayout llHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        sharedPrefernces = new SharedPrefernces(HomeActivity.this);
        sharedPref = new SharedPref(HomeActivity.this);
        View headerView = binding.navView.getHeaderView(0);
         llHeader = headerView.findViewById(R.id.llHeader);
        dataClass = sharedPrefernces.getUserData();
        name = headerView.findViewById(R.id.name);
        address = headerView.findViewById(R.id.address);
        mobile = headerView.findViewById(R.id.mobile);
        setupHomeWidgets();
        MyProfileAPi();
        hit_getNoticeboard_APi();

        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_YOUR_PUBLISHABLE_KEY" // Replace with your Stripe publishable key
        );
    }


    private void setupHomeWidgets() {
        ArrayList personNames = new ArrayList<>(Arrays.asList("My Invoices", "Service Request", "My Parking", "Emergency Contacts"));
        ArrayList personImages = new ArrayList<>(Arrays.asList(R.mipmap.invoice, R.mipmap.servicee, R.mipmap.parking, R.mipmap.contact));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        binding.homeRv.setLayoutManager(staggeredGridLayoutManager);
        HomeAdapter homeAdapter = new HomeAdapter(HomeActivity.this, personNames, personImages);
        binding.homeRv.setAdapter(homeAdapter);
    }

    private void setUpToolBar() {
        binding.tool.header.setText(R.string.app_name);
        binding.tool.navIcon.setImageResource(R.drawable.hamburger_icon);
        binding.tool.navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.invoice:
                        startActivity(new Intent(HomeActivity.this, InvoicesActivity.class));
                        finish();
                        break;
                    case R.id.service:
                        startActivity(new Intent(HomeActivity.this, ServiceRequestActivity.class));
                        finish();
                        break;
                    case R.id.visitor:
                        startActivity(new Intent(HomeActivity.this, MeetVisitorActivity.class));
                        finish();
                        break;
                    case R.id.parking:
                        startActivity(new Intent(HomeActivity.this, ParkingActivity.class));
                        finish();
                        break;

                    case R.id.emergency:
                        startActivity(new Intent(HomeActivity.this, EmergencyContactsActivity.class));
                        finish();
                        break;

                    case R.id.guideline:
                        startActivity(new Intent(HomeActivity.this, GuidelinesActivity.class));
                        finish();
                        break;

                    case R.id.helpAndSupport:
                        startActivity(new Intent(HomeActivity.this, HelpAndSupportActivity.class));
                        finish();
                        break;

                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setTitle("Logout");
                        builder.setMessage("Do you really want to logout?");
                        builder.setPositiveButton("OK", (dialog, which) -> {
                            hit_LogoutApi();
                        });
                        builder.setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        });
                        // Display the AlertDialog
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                }
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }


        });
        //setting header
        // Set text in header views
        if (dataClass != null) {
            name.setText(dataClass.getName().toString());
            address.setText("ID: " + dataClass.getId().toString());
            if (dataClass.getMobile() != null) {
                mobile.setText("Mobile:" + dataClass.getMobile().toString());
            }
        }
        llHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, EditProfileActivity.class));
                finish();
            }
        });
    }

    private void hit_LogoutApi() {
        apiService = NetworkCall.getRetrofitInstance(HomeActivity.this).create(ApiService.class);
        Call<LogoutResponse> call = apiService.logout();
        call.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LogoutResponse logoutResponse = response.body();
                    sharedPrefernces.saveData("is_login", "no");
                    sharedPref.clearData("token");
                    sharedPref.updateData("is_login", "no");
                    sharedPref.updateData("token", "");

                    Log.d("token", sharedPref.getStringData("token", ""));

                    Toast.makeText(getApplicationContext(), " Logout Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(HomeActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Login error", t);
            }
        });
    }

    private void MyProfileAPi() {
        apiService = NetworkCall.getRetrofitInstance(HomeActivity.this).create(ApiService.class);
        Call<MyProfileResponse> call3 = apiService.myProfile();
        call3.enqueue(new Callback<MyProfileResponse>() {
            @Override
            public void onResponse(Call<MyProfileResponse> call, Response<MyProfileResponse> response) {
                MyProfileResponse myProfileResponse = response.body();
                dataClass = myProfileResponse.getData();
                if (dataClass != null) {
                    name.setText(dataClass.getName().toString());
                    address.setText("ID: " + dataClass.getId().toString());
                    if (dataClass.getMobile() != null) {
                        mobile.setText("Mobile:" + dataClass.getMobile().toString());
                    }
                }
                setUpToolBar();
            }

            @Override
            public void onFailure(Call<MyProfileResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void hit_getNoticeboard_APi() {
        apiService = NetworkCall.getRetrofitInstance(HomeActivity.this).create(ApiService.class);
        Call<AnnouncementsResponse> call3 = apiService.getNoticeBoard();
        call3.enqueue(new Callback<AnnouncementsResponse>() {
            @Override
            public void onResponse(Call<AnnouncementsResponse> call, Response<AnnouncementsResponse> response) {
                AnnouncementsResponse announcementsResponse = response.body();
                if (announcementsResponse.getData() != null && announcementsResponse.getData().size() > 0) {
                    binding.announcmentOfDay.setVisibility(View.VISIBLE);
                    binding.announcement.setText(announcementsResponse.getData().get(0).getDescription());
                }
            }

            @Override
            public void onFailure(Call<AnnouncementsResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Handle action bar navigation icon click to open/close the drawer
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}