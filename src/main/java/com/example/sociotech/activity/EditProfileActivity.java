package com.example.sociotech.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.sociotech.R;
import com.example.sociotech.api.ApiService;
import com.example.sociotech.api.NetworkCall;
import com.example.sociotech.databinding.ActivityEditProfileBinding;
import com.example.sociotech.model.editProfile.EditMyProfileResponse;
import com.example.sociotech.model.parking.addParking.PostParkingResponse;
import com.example.sociotech.model.profile.DataClass;
import com.example.sociotech.model.profile.MyProfileResponse;
import com.example.sociotech.utils.ProgressBarr;
import com.example.sociotech.utils.SharedPrefernces;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private ActivityEditProfileBinding binding;
    SharedPrefernces sharedPrefernces;
    DataClass dataClass;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_PICK = 2;
    static final int REQUEST_PERMISSION = 100;
    Uri photoURI;
    private ApiService apiService;
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    Dialog dialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        binding.tool.header.setText(R.string.edit_profile);
        sharedPrefernces = new SharedPrefernces(EditProfileActivity.this);
        // getUserDetails();
       // setUserData();

        MyProfileAPi();
        binding.tool.navIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProfileActivity.this, HomeActivity.class));
                finish();
            }
        });

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_select_image, null);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                TextView gallery = dialogView.findViewById(R.id.gallery);
                TextView camera = dialogView.findViewById(R.id.camera);

                gallery.setOnClickListener(v -> {
                    dialog.dismiss();
                    if (checkPermissions()) {
                        openGallery();
                    }
                });

                camera.setOnClickListener(v -> {
                    dialog.dismiss();
                    if (checkPermissions()) {
                        openCamera();
                    }
                });
                dialog.show();

            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = ProgressBarr.showProgress(EditProfileActivity.this);
                Hit_Api_UpdateProfile();
            }
        });
    }

    private void MyProfileAPi() {
        apiService = NetworkCall.getRetrofitInstance(EditProfileActivity.this).create(ApiService.class);
        Call<MyProfileResponse> call3 = apiService.myProfile();
        call3.enqueue(new Callback<MyProfileResponse>() {
            @Override
            public void onResponse(Call<MyProfileResponse> call, Response<MyProfileResponse> response) {
                MyProfileResponse myProfileResponse = response.body();
               // sharedPrefernces.saveUserData(myProfileResponse.getData());
                dataClass = myProfileResponse.getData();
                binding.name.setText(dataClass.getName().toString());
                binding.email.setText(dataClass.getEmail().toString());
                binding.mobile.setText(dataClass.getMobile().toString());
                binding.Flat.setText(dataClass.getFlat().toString());
                binding.block.setText(dataClass.getBlock().toString());
                Glide.with(EditProfileActivity.this)
                        .load(dataClass.getAvatar())
                        .placeholder(R.drawable.user) // Optional: add a placeholder image
                        .error(R.drawable.user) // Optional: add an error image
                        .into(binding.profileImage);

            }

            @Override
            public void onFailure(Call<MyProfileResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void Hit_Api_UpdateProfile() {
        apiService = NetworkCall.getRetrofitInstance(EditProfileActivity.this).create(ApiService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", binding.name.getText().toString());
        jsonObject.addProperty("email", binding.email.getText().toString());
        jsonObject.addProperty("first_name", "" );
        jsonObject.addProperty("last_name", "" );
        jsonObject.addProperty("mobile", binding.mobile.getText().toString());

        Call<MyProfileResponse> call = apiService.updateProfile(jsonObject);
        call.enqueue(new Callback<MyProfileResponse>() {
            @Override
            public void onResponse(Call<MyProfileResponse> call, Response<MyProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dialog.dismiss();
                    MyProfileResponse editMyProfileResponse = response.body();
                    sharedPrefernces.clearUserData(EditProfileActivity.this);
                    sharedPrefernces.saveUserData(editMyProfileResponse.getData());
                    dataClass = sharedPrefernces.getUserData();
                   // startActivity(new Intent(EditProfileActivity.this, HomeActivity.class));
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                } else {
                     Toast.makeText(EditProfileActivity.this, " Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyProfileResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Login error", t);
            }
        });
    }

    public void setUserData() {
        binding.name.setText(dataClass.getName().toString());
        binding.email.setText(dataClass.getEmail().toString());
        binding.mobile.setText(dataClass.getMobile().toString());
        binding.Flat.setText(dataClass.getFlat().toString());
        binding.block.setText(dataClass.getBlock().toString());
        Glide.with(this)
                .load(dataClass.getAvatar())
                .placeholder(R.drawable.user) // Optional: add a placeholder image
                .error(R.drawable.user) // Optional: add an error image
                .into(binding.profileImage);
    }

    public void getUserDetails() {
        dataClass = sharedPrefernces.getUserData();
    }

    public boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }
        return true;
    }


    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void openGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_IMAGE_PICK);
    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            binding.profileImage.setImageURI(photoURI);
        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            binding.profileImage.setImageURI(selectedImage);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //   Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
            } else {
                //  Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

