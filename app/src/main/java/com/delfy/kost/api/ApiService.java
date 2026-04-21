package com.delfy.kost.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    // 1. Tembak URL /api/login
    @POST("api/login")
    Call<LoginResponse> loginPemilik(@Body LoginRequest request);

    // 2. Tembak URL /api/kamar (Butuh Token)
    @GET("api/kamar")
    Call<KamarResponse> getKamar(@Header("Authorization") String token);

    // 3. Tembak URL POST /api/kamar untuk Tambah Data
    @POST("api/kamar")
    Call<KamarResponse> tambahKamar(
            @Header("Authorization") String token,
            @Body KamarRequest request
    );
}