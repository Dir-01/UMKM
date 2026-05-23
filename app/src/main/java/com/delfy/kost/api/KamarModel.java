package com.delfy.kost.api;

import com.google.gson.annotations.SerializedName;

public class KamarModel {
    @SerializedName("id_kamar")
    private String idKamar;

    @SerializedName("no_kamar")
    private String noKamar;

    @SerializedName("tipe_kamar")
    private String tipeKamar;

    @SerializedName("harga")
    private int harga;

    // Getter
    public String getNoKamar() { return noKamar; }
    public String getTipeKamar() { return tipeKamar; }
}