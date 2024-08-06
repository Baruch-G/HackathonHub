package com.example.hackathonhub.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://hackathon-hub-server.onrender.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10000, TimeUnit.SECONDS) // Set connection timeout
        .writeTimeout(10000, TimeUnit.SECONDS)   // Set write timeout
        .readTimeout(10000, TimeUnit.SECONDS)    // Set read timeout
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Use custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}