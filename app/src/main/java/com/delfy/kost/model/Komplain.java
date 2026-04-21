package com.delfy.kost.model;

public class Komplain {
    private String id;
    private String namaPenghuni;
    private String noKamar;
    private String keluhan;
    private String status; // "Menunggu", "Selesai"

    public Komplain() {}

    public Komplain(String id, String namaPenghuni, String noKamar, String keluhan, String status) {
        this.id = id;
        this.namaPenghuni = namaPenghuni;
        this.noKamar = noKamar;
        this.keluhan = keluhan;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNamaPenghuni() { return namaPenghuni; }
    public void setNamaPenghuni(String namaPenghuni) { this.namaPenghuni = namaPenghuni; }

    public String getNoKamar() { return noKamar; }
    public void setNoKamar(String noKamar) { this.noKamar = noKamar; }

    public String getKeluhan() { return keluhan; }
    public void setKeluhan(String keluhan) { this.keluhan = keluhan; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
