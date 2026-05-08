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

        @SerializedName("user")
        private UserData user;

        public String getAccessToken() { return access_token; }
        public UserData getUser() { return user; }
    }

    public static class UserData {
        @SerializedName("nama") // Harus sama dengan kolom 'nama' di tabel penyewa Navicat
        private String nama;

        @SerializedName("id_penyewa")
        private String id_penyewa;

        @SerializedName("email") // Tambahkan ini sekalian buat jaga-jaga
        private String email;

        public String getNama() { return nama; }
        public String getIdPenyewa() { return id_penyewa; }
        public String getEmail() { return email; }
    }
}