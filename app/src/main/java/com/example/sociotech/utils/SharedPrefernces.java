package com.example.sociotech.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sociotech.activity.EditProfileActivity;
import com.example.sociotech.model.profile.DataClass;
import com.google.gson.Gson;

public class SharedPrefernces {
    private static SharedPrefernces yourPreference;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public static SharedPrefernces getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new SharedPrefernces(context);
        }
        return yourPreference;
    }

    public SharedPrefernces(Context context) {
        sharedPreferences = context.getSharedPreferences("YourCustomNamedPreference", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public void clearData(Context context)
    {
        sharedPreferences.edit().remove("token").clear().commit();
    }

    public void saveUserData(DataClass dataClass) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(dataClass);
        editor.putString("user", json);
        editor.apply(); // or editor.commit();
    }

    public DataClass getUserData() {
        String json = sharedPreferences.getString("user", null);
        return gson.fromJson(json, DataClass.class);
    }

    public void clearUserData(Context context)
    {
        sharedPreferences.edit().remove("user").clear().commit();
    }
}
