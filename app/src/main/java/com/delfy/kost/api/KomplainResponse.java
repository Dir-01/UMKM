package com.delfy.kost.api;

import com.google.gson.annotations.SerializedName;

public class KomplainResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    // Getter untuk mengecek apakah data berhasil masuk ke database Laravel
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}