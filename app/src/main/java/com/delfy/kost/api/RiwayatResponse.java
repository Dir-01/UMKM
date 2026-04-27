package com.delfy.kost.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RiwayatResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<RiwayatModel> data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<RiwayatModel> getData() { return data; }

    // --- INILAH "RiwayatModel" YANG DICARI ADAPTERMU ---
    public static class RiwayatModel {
        @SerializedName("id_transaksi")
        private String idTransaksi;

        @SerializedName("tipe_kamar")
        private String tipeKamar;

        @SerializedName("durasi_sewa")
        private int durasiSewa;

        @SerializedName("total_harga")
        private int totalHarga;

        @SerializedName("metode_pembayaran")
        private String metodePembayaran;

        @SerializedName("bukti_bayar")
        private String buktiBayar;

        // Getter untuk mengambil data
        public String getIdTransaksi() { return idTransaksi; }
        public String getTipeKamar() { return tipeKamar; }
        public int getDurasiSewa() { return durasiSewa; }
        public int getTotalHarga() { return totalHarga; }
        public String getMetodePembayaran() { return metodePembayaran; }
        public String getBuktiBayar() { return buktiBayar; }
    }
}