package com.delfy.kost.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class KamarResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    // Ini yang error tadi karena kelas KamarModel-nya sempat hilang
    @SerializedName("data")
    private List<KamarModel> data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<KamarModel> getData() { return data; }

    // --- INNER CLASS UNTUK MODEL KAMAR ---
    public static class KamarModel {
        @SerializedName("id_kamar")
        private int id_kamar; // Sudah diubah jadi INT sesuai database Navicat!

        @SerializedName("no_kamar")
        private String no_kamar;

        @SerializedName("harga")
        private int harga;

        @SerializedName("status")
        private String status;

        @SerializedName("id_pemilik")
        private String id_pemilik;

        // Getter
        public int getIdKamar() { return id_kamar; }
        public String getNoKamar() { return no_kamar; }
        public int getHarga() { return harga; }
        public String getStatus() { return status; }
        public String getIdPemilik() { return id_pemilik; }
    }
}