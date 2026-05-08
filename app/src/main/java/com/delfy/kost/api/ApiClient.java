package com.delfy.kost.api; // Pastikan nama package ini sesuai dengan punyamu

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // PENTING: Ganti IP ini dengan IP Laptopmu yang aktif saat ini!
    //private static final String BASE_URL = "http://192.168.1.15:8000/";
    private static final String BASE_URL = "http://10.0.2.2:8000/";
    private static Retrofit retrofit = null;

    public static ApiService getClient() {
        // Mengatur batas waktu tunggu (Timeout) agar tidak "infinite loading"
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient) // Memasukkan OkHttp ke Retrofit
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}