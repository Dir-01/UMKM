package com.delfy.kost;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast; // Tambahan untuk memunculkan pesan error
import androidx.appcompat.app.AppCompatActivity;

public class KamarTipe1Activity extends AppCompatActivity {

    // Variabel untuk menampung ID Kamar dari halaman Beranda
    private String idKamar = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamar_tipe1);

        // =========================================================================
        // 1. TANGKAP ID_KAMAR DARI BERANDA
        // =========================================================================
        if (getIntent().hasExtra("ID_KAMAR")) {
            Object idObj = getIntent().getExtras().get("ID_KAMAR");
            if (idObj != null) {
                idKamar = String.valueOf(idObj);
            }
        }

        // =========================================================================
        // 2. HUBUNGKAN ID DARI XML
        // =========================================================================
        ImageView btnBack = findViewById(R.id.btn_back);
        LinearLayout btnPesan = findViewById(R.id.btn_pesan);

        // Aksi Tombol Kembali
        btnBack.setOnClickListener(v -> finish());

        // =========================================================================
        // 3. AKSI TOMBOL PESAN
        // =========================================================================
        btnPesan.setOnClickListener(v -> {
            // Cek dulu apakah ID Kamar kosong
            if (idKamar == null || idKamar.isEmpty()) {
                Toast.makeText(this, "Error: Data kamar gagal dimuat!", Toast.LENGTH_SHORT).show();
                return; // Berhenti di sini jika ID kosong
            }

            // Bawa ID_KAMAR ke halaman PemesananActivity
            Intent intent = new Intent(this, PemesananActivity.class);
            intent.putExtra("ID_KAMAR", idKamar);
            startActivity(intent);
        });
    }
}