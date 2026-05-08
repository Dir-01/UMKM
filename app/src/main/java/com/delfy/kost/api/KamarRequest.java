package com.delfy.kost.api; // Sesuaikan dengan struktur folder kamu

import com.google.gson.annotations.SerializedName;

public class KamarRequest {

    @SerializedName("no_kamar")
    private String noKamar;

    // 👇 INI DIA WADAH BARUNYA 👇
    @SerializedName("tipe_kamar")
    private String tipeKamar;

    @SerializedName("harga")
    private int harga;

    @SerializedName("status")
    private String status;

    @SerializedName("id_pemilik")
    private String idPemilik;

    // Constructor (Urutannya sudah disesuaikan dengan TambahKamarActivity kamu)
    public KamarRequest(String noKamar, String tipeKamar, int harga, String status, String idPemilik) {
        this.noKamar = noKamar;
        this.tipeKamar = tipeKamar;
        this.harga = harga;
        this.status = status;
        this.idPemilik = idPemilik;
    }

    // Getter (Opsional, tapi bagus untuk kelengkapan)
    public String getNoKamar() { return noKamar; }
    public String getTipeKamar() { return tipeKamar; }
    public int getHarga() { return harga; }
    public String getStatus() { return status; }
    public String getIdPemilik() { return idPemilik; }
}