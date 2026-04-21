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

    // WAJIB menggunakan "static" agar Gson tidak gagal membacanya
    public static class TokenData {

        // WAJIB menggunakan SerializedName agar namanya persis dengan Laravel
        @SerializedName("access_token")
        private String access_token;

        @SerializedName("token_type")
        private String token_type;

        public String getAccessToken() {
            return access_token;
        }
    }
}