package com.delfy.kost.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    /*
    |--------------------------------------------------------------------------
    | 🔓 PUBLIC ROUTES (Login & Register)
    |--------------------------------------------------------------------------
    */

    @POST("api/login_pemilik")
    Call<LoginResponse> loginPemilik(@Body LoginRequest request);

    @POST("api/login_penyewa")
    Call<LoginResponse> loginPenyewa(@Body LoginPenyewaRequest request);

    @POST("api/register_penyewa")
    Call<LoginResponse> registerPenyewa(@Body RegisterRequest request);

    // Ini dibuat tetap bisa menerima Header supaya DashboardPemilik kamu tidak error!
    @GET("api/kamar")
    Call<KamarResponse> getKamar(@Header("Authorization") String token);


    /*
    |--------------------------------------------------------------------------
    | 🔒 PROTECTED ROUTES (Wajib Pakai @Header("Authorization") token)
    |--------------------------------------------------------------------------
    */

    // 1. Tambah Kamar (Oleh Pemilik)
    @Headers("Accept: application/json")
    @POST("api/tambah-kamar")
    Call<KamarResponse> tambahKamar(
            @Header("Authorization") String token,
            @Body KamarRequest request
    );

    // 2. Transaksi / Bayar Kost (Oleh Penyewa)
    @Multipart
    @Headers("Accept: application/json")
    @POST("api/transaksi")
    Call<TransaksiResponse> kirimPembayaran(
            @Header("Authorization") String token,
            @Part("id_penyewa") RequestBody idPenyewa,
            @Part("id_kamar") RequestBody idKamar,
            @Part("tipe_kamar") RequestBody tipeKamar,
            @Part("tanggal_masuk") RequestBody tglMasuk,
            @Part("durasi_sewa") RequestBody durasi,
            @Part("total_harga") RequestBody total,
            @Part("metode_pembayaran") RequestBody metode,
            @Part MultipartBody.Part bukti_bayar
    );

    // 3. Riwayat Transaksi Penyewa
    @Headers("Accept: application/json")
    @GET("api/riwayat-penyewa")
    Call<RiwayatResponse> getRiwayat(@Header("Authorization") String token);

    // 4. Semua Transaksi (Oleh Pemilik)
    @Headers("Accept: application/json")
    @GET("api/semua-transaksi")
    Call<RiwayatResponse> getSemuaPesanan(@Header("Authorization") String token);

    // 5. Konfirmasi Pembayaran (Oleh Pemilik)
    @Headers("Accept: application/json")
    @PUT("api/konfirmasi-bayar/{id}")
    Call<LoginResponse> konfirmasiPembayaran(
            @Header("Authorization") String token,
            @Path("id") String idTransaksi
    );

    // 6. Kelola Kamar (Update & Delete)
    @Headers("Accept: application/json")
    @PUT("api/update-kamar/{id}")
    Call<KamarResponse> updateKamar(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body KamarRequest request
    );

    @Headers("Accept: application/json")
    @DELETE("api/hapus-kamar/{id}")
    Call<KamarResponse> deleteKamar(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    // 7. Logout
    @Headers("Accept: application/json")
    @POST("api/logout")
    Call<LoginResponse> logout(@Header("Authorization") String token);
}