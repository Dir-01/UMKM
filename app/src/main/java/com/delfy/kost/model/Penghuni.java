package com.delfy.kost.model;

public class Penghuni {
    private String id;
    private String namaPenghuni;
    private String noKamar;
    private String kontrak;
    private String kontak;
    private String photoUrl;

    public Penghuni() {}

    public Penghuni(String id, String namaPenghuni, String noKamar, String kontrak, String kontak) {
        this.id = id;
        this.namaPenghuni = namaPenghuni;
        this.noKamar = noKamar;
        this.kontrak = kontrak;
        this.kontak = kontak;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNamaPenghuni() { return namaPenghuni; }
    public void setNamaPenghuni(String namaPenghuni) { this.namaPenghuni = namaPenghuni; }

    public String getNoKamar() { return noKamar; }
    public void setNoKamar(String noKamar) { this.noKamar = noKamar; }

    public String getKontrak() { return kontrak; }
    public void setKontrak(String kontrak) { this.kontrak = kontrak; }

    public String getKontak() { return kontak; }
    public void setKontak(String kontak) { this.kontak = kontak; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
