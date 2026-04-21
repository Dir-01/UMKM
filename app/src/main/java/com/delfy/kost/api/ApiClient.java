package com.delfy.kost.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // GANTI dengan IPv4 laptop kamu!
    // Cara cek: Buka CMD di laptop -> ketik 'ipconfig' -> lihat tulisan IPv4 Address
    // Contoh format: "http://192.168.1.15:8000/" (JANGAN LUPA tanda garis miring / di akhir)
    private static final String BASE_URL = "http://10.0.2.2:8000/";

    private static Retrofit retrofit = null;

    public static ApiService getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Pengubah JSON otomatis
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}