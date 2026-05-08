package com.delfy.kost.api; // Sesuaikan jika foldernya berbeda

import com.google.gson.annotations.SerializedName;

public class PenyewaModel {

    @SerializedName("nama")
    private String nama;

    @SerializedName("no_hp")
    private String noHp;

    public String getNama() {
        return nama;
    }

    public String getNoHp() {
        return noHp;
    }
}