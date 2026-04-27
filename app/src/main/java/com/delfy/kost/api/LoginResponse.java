package com.delfy.kost.api;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private TokenData data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public TokenData getData() { return data; }

    public static class TokenData {
        @SerializedName("access_token")
        private String access_token;

        @SerializedName("token_type")
        private String token_type;

        // TAMBAHAN: Untuk menangkap data user (nama)
        @SerializedName("user")
        private UserData user;

        public String getAccessToken() { return access_token; }
        public UserData getUser() { return user; }
    }

    // TAMBAHAN: Class untuk membaca isi data user
    // Cari class UserData di bagian bawah file LoginResponse.java
    public static class UserData {
        @SerializedName("nama")
        private String nama;

        // TAMBAHKAN INI: Agar Java kenal id_penyewa dari Laravel
        @SerializedName("id_penyewa")
        private String id_penyewa;

        public String getNama() {
            return nama;
        }

        // TAMBAHKAN GETTER INI: Supaya bisa dipanggil di Activity
        public String getIdPenyewa() {
            return id_penyewa;
        }
    }
}