package com.delfy.kost.api;

public class KamarRequest {
    private String no_kamar;
    private int harga;
    private String status;

    // --- TAMBAHKAN BARIS INI ---
    private String id_pemilik;

    // UBAH JUGA GENERATORNYA (CONSTRUCTOR) JADI SEPERTI INI:
    public KamarRequest(String no_kamar, int harga, String status, String id_pemilik) {
        this.no_kamar = no_kamar;
        this.harga = harga;
        this.status = status;
        this.id_pemilik = id_pemilik; // Masukkan datanya
    }
}