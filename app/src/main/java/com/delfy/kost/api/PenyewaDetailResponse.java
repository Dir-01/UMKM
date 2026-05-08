package com.delfy.kost.api;

import com.google.gson.annotations.SerializedName;

public class PenyewaDetailResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private PenyewaModel data; // Berisi satu objek penyewa

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public PenyewaModel getData() { return data; }

    public static class PenyewaModel {
        @SerializedName("id_penyewa")
        private String idPenyewa;

        @SerializedName("nama")
        private String nama;

        @SerializedName("email")
        private String email;

        @SerializedName("no_hp")
        private String noHp;

        @SerializedName("jenis_kelamin")
        private String jenisKelamin;

        @SerializedName("foto_ktp") // Pastikan nama field ini sama dengan di Navicat
        private String fotoKtp;

        // Getter
        public String getIdPenyewa() { return idPenyewa; }
        public String getNama() { return nama; }
        public String getEmail() { return email; }
        public String getNoHp() { return noHp; }
        public String getJenisKelamin() { return jenisKelamin; }
        public String getFotoKtp() { return fotoKtp; }
    }
}