package com.example.sociotech.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.sociotech.R;
import com.example.sociotech.api.ApiService;
import com.example.sociotech.api.NetworkCall;
import com.example.sociotech.databinding.ActivityLoginBinding;
import com.example.sociotech.model.login.LoginResponse;
import com.example.sociotech.utils.Constants;
import com.example.sociotech.utils.EmailValidator;
import com.example.sociotech.utils.SharedPref;
import com.example.sociotech.utils.SharedPrefernces;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ApiService apiService;
    private ActivityLoginBinding binding;
     SharedPrefernces sharedPrefernces;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        Boolean valid_email = EmailValidator.isValidEmail(binding.email.getText().toString());
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.email.getText().toString().equals("") && !valid_email && !binding.password.getText().toString().equals("")) {
                    hit_Login_API(binding.email.getText().toString(), binding.password.getText().toString());
                } else {
                    binding.error.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void hit_Login_API(String email, String passwordText) {
        apiService = NetworkCall.getRetrofitInstance(LoginActivity.this).create(ApiService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", passwordText);

        Call<LoginResponse> call = apiService.login(jsonObject);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                                     SharedPref sharedPreferencesHelper = new SharedPref(LoginActivity.this);
                    sharedPreferencesHelper.saveData("token", loginResponse.getToken());
                    sharedPreferencesHelper.saveData("is_login", "yes");
                    String token = sharedPreferencesHelper.getStringData("token", "");
                    Log.d("token",loginResponse.getToken());
                    Toast.makeText(LoginActivity.this, "Login sucess", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();


                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Login error", t);
            }
        });
    }
}