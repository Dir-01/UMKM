package com.delfy.kost.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class KamarResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<KamarModel> data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<KamarModel> getData() { return data; }

    public static class KamarModel {
        @SerializedName("id_kamar")
        private String id_kamar;

        @SerializedName("no_kamar")
        private String no_kamar;

        @SerializedName("harga")
        private int harga;

        @SerializedName("status")
        private String status;

        @SerializedName("id_pemilik")
        private String id_pemilik;

        @SerializedName("tipe_kamar")
        private String tipe_kamar;

        @SerializedName("foto_kamar")
        private String foto_kamar;

        // Getter untuk Adapter
        public String getIdKamar() { return id_kamar; }
        public String getNoKamar() { return no_kamar; }
        public int getHarga() { return harga; }
        public String getStatus() { return status; }
        public String getIdPemilik() { return id_pemilik; }
        public String getTipeKamar() { return tipe_kamar; }
        public String getFotoKamar() { return foto_kamar; }
    }
}