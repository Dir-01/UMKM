package com.delfy.kost.api;

import okhttp3.ResponseBody;
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
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

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

    @Multipart
    @POST("api/register-penyewa")
    Call<LoginResponse> registerPenyewaWithKtp(
            @Part("nama") RequestBody nama, // SUDAH DIPERBAIKI (bukan nama_lengkap)
            @Part("email") RequestBody email,
            @Part("no_hp") RequestBody noHp,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part fotoKtp
    );

    @GET("api/kamar")
    Call<KamarResponse> getKamar(@Header("Authorization") String token);


    /*
    |--------------------------------------------------------------------------
    | 🔒 PROTECTED ROUTES (Wajib Pakai @Header("Authorization") token)
    |--------------------------------------------------------------------------
    */

    @Multipart
    @Headers("Accept: application/json")
    @POST("api/tambah-kamar")
    Call<KamarResponse> tambahKamar(
            @Header("Authorization") String token,
            @Part("no_kamar") RequestBody noKamar,
            @Part("tipe_kamar") RequestBody tipeKamar,
            @Part("harga") RequestBody harga,
            @Part("status") RequestBody status,
            @Part("id_pemilik") RequestBody idPemilik,
            @Part MultipartBody.Part fotoKamar // File fisik foto
    );

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

    @Headers("Accept: application/json")
    @GET("api/riwayat-penyewa")
    Call<RiwayatResponse> getRiwayat(@Header("Authorization") String token);

    @Headers("Accept: application/json")
    @GET("api/semua-transaksi")
    Call<RiwayatResponse> getSemuaPesanan(@Header("Authorization") String token);

    @Headers("Accept: application/json")
    @PUT("api/konfirmasi-bayar/{id}")
    Call<LoginResponse> konfirmasiPembayaran(
            @Header("Authorization") String token,
            @Path("id") String idTransaksi
    );

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

    @Headers("Accept: application/json")
    @POST("api/logout")
    Call<LoginResponse> logout(@Header("Authorization") String token);

    @GET("api/pesanan/penghuni") // Sesuaikan route-nya jika berbeda di web.php/api.php
    Call<RiwayatResponse> getDaftarPenghuni(@Header("Authorization") String token);

    @GET("api/penyewa/{id}")
    Call<PenyewaDetailResponse> getDetailPenyewa(
            @Header("Authorization") String token,
            @Path("id") String idPenyewa
    );
    @GET("api/komplain-masuk")
    Call<ListKomplainResponse> getKomplainMasuk(
            @Header("Authorization") String token
    );

    // Untuk POV Penyewa: Mengirim data komplain ke database
    @FormUrlEncoded
    @POST("api/kirim-komplain")
    Call<KomplainResponse> kirimKomplain(
            @Header("Authorization") String token,
            @Field("id_penyewa") String idPenyewa, // <-- TAMBAHKAN BARIS INI
            @Field("no_kamar") String noKamar,
            @Field("subjek") String subjek,
            @Field("deskripsi") String deskripsi
    );
    @FormUrlEncoded
    @PUT("api/update-status-komplain/{id}")
    Call<KomplainResponse> updateStatusKomplain(
            @Header("Authorization") String token,
            @Path("id") String idKomplain,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("api/kamar/update-status/{id}") // Sesuaikan dengan route Laravel kamu
    Call<ResponseBody> updateStatusKamar(
            @Header("Authorization") String token,
            @Path("id") String idKamar,
            @Field("status") String status
    );
}