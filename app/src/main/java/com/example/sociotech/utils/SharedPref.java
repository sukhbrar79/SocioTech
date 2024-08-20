package com.example.sociotech.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static final String PREF_NAME = "my_preferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Save token data to SharedPreferences
    public void saveData(String key, String value) {
        editor.putString(key, value);
        editor.commit();  // Use commit() if you need synchronous saving
    }


    // Retrieve data from SharedPreferences
    public String getStringData(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }


    // Update data in SharedPreferences (same as save since it will overwrite)
    public void updateData(String key, String value) {
        saveData(key, value);
    }


    // Clear a specific preference
    public void clearData(String key) {
        editor.remove(key);
        editor.apply();
    }

    // Clear all preferences
    public void clearAllData() {
        editor.clear();
        editor.apply();
    }
}