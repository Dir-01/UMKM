package com.delfy.kost.api;

public class RegisterRequest {
    private String nama;
    private String email;
    private String no_hp;
    private String password;

    public RegisterRequest(String nama, String email, String no_hp, String password) {
        this.nama = nama;
        this.email = email;
        this.no_hp = no_hp;
        this.password = password;
    }
}