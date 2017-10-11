/*
 * Created  by unbegrenzt for Jorge Luis Morales Centeno on 10-02-17 06:13 PM
 * Copyright (c) 2017. All rights reserved.
 *
 * Last modified 10-02-17 06:13 PM
 */

package com.example.unbegrenzt.fharmaapp.web_service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class APIClient {

    public static final String GOOGLE_PLACE_API_KEY = "AIzaSyCAiZV-EBK64MkSA3hJngBjACOjfgBY1jQ";

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).addInterceptor(interceptor).build();


        Retrofit retrofit = null;

        String base_url = "https://maps.googleapis.com/maps/api/";
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        return retrofit;
    }

}
