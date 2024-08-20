package com.example.sociotech.api;

import android.content.Context;

import com.example.sociotech.utils.Constants;
import com.example.sociotech.utils.SharedPref;
import com.example.sociotech.utils.SharedPrefernces;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkCall {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://18.130.25.92/";
    private static SharedPref sharedPrefernces;


    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            // Create the logging interceptor
            sharedPrefernces = new SharedPref(context);
            Interceptor headerInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    Request requestWithHeaders = originalRequest.newBuilder()
                            .header("Authorization", "Bearer " +   sharedPrefernces.getStringData("token", ""))  // Add Authorization header
                            .header("Accept", "application/json")         // Add Accept header
                            .build();
                    return chain.proceed(requestWithHeaders);
                }
            };
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


            // Create the OkHttpClient and add the logging interceptor
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(headerInterceptor)
                    .build();

          /*  Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();*/


            // Build the Retrofit instance with the OkHttpClient
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

