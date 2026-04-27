package com.delfy.kost.api;

import com.google.gson.annotations.SerializedName;

public class LoginPenyewaRequest {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public LoginPenyewaRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}