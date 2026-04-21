package com.delfy.kost.api;

public class KamarRequest {
    private String no_kamar;
    private int harga;
    private String status;

    public KamarRequest(String no_kamar, int harga, String status) {
        this.no_kamar = no_kamar;
        this.harga = harga;
        this.status = status;
    }
}