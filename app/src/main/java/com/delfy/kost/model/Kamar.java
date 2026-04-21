package com.delfy.kost.model;

import com.google.gson.annotations.SerializedName;

public class Kamar {
    // Sesuaikan dengan id_kamar (int) dari database
    @SerializedName("id_kamar")
    private int idKamar;

    // Sesuaikan dengan no_kamar dari Laravel
    @SerializedName("no_kamar")
    private String noKamar;

    @SerializedName("harga")
    private int harga;

    @SerializedName("status")
    private String status;

    @SerializedName("id_pemilik")
    private String idPemilik;

    public Kamar() {}

    // Getter dan Setter
    public int getIdKamar() { return idKamar; }
    public void setIdKamar(int idKamar) { this.idKamar = idKamar; }

    public String getNoKamar() { return noKamar; }
    public void setNoKamar(String noKamar) { this.noKamar = noKamar; }

    public int getHarga() { return harga; }
    public void setHarga(int harga) { this.harga = harga; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getIdPemilik() { return idPemilik; }
    public void setIdPemilik(String idPemilik) { this.idPemilik = idPemilik; }
}