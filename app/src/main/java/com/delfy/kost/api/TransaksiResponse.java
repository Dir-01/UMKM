package com.delfy.kost.api;

import com.google.gson.annotations.SerializedName;

public class TransaksiResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    // Java akan mencari "TransaksiItem" di bawah, dan sekarang pasti ketemu!
    @SerializedName("data")
    private TransaksiItem data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public TransaksiItem getData() { return data; }

    // =========================================================
    // INI ADALAH CLASS TRANSAKSI ITEM (Harus ada di dalam sini)
    // =========================================================
    public static class TransaksiItem {
        @SerializedName("id_transaksi") private String idTransaksi;
        @SerializedName("tipe_kamar") private String tipeKamar;
        @SerializedName("tanggal_masuk") private String tanggalMasuk;
        @SerializedName("tanggal_selesai") private String tanggalSelesai;
        @SerializedName("durasi_sewa") private int durasiSewa;
        @SerializedName("total_harga") private int totalHarga;
        @SerializedName("metode_pembayaran") private String metodePembayaran;
        @SerializedName("status") private String status;
        @SerializedName("bukti_bayar") private String buktiBayar;

        // --- FUNGSI GETTER UNTUK ADAPTER ---
        public String getIdTransaksi() { return idTransaksi; }
        public String getTipeKamar() { return tipeKamar; }
        public String getTanggalMasuk() { return tanggalMasuk; }
        public String getTanggalSelesai() { return tanggalSelesai; }
        public int getDurasiSewa() { return durasiSewa; }
        public int getTotalHarga() { return totalHarga; }
        public String getMetodePembayaran() { return metodePembayaran; }
        public String getStatus() { return status; }
        public String getBuktiBayar() { return buktiBayar; }
    }
}