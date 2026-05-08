package com.delfy.kost.api;

import com.google.gson.annotations.SerializedName;

public class KomplainModel {
    @SerializedName("id_komplain")
    private int idKomplain;

    @SerializedName("no_kamar")
    private String noKamar;

    @SerializedName("subjek")
    private String subjek;

    @SerializedName("deskripsi")
    private String deskripsi;

    @SerializedName("status")
    private String status;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("penyewa")
    private LoginResponse.UserData penyewa; // Menggunakan UserData yang sudah ada untuk ambil nama

    // Getter
    public int getIdKomplain() { return idKomplain; }
    public String getNoKamar() { return noKamar; }
    public String getSubjek() { return subjek; }
    public String getDeskripsi() { return deskripsi; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public LoginResponse.UserData getPenyewa() { return penyewa; }
}