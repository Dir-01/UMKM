package com.delfy.kost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class KamarTipe2Activity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout btnPesanSekarang; // Pastikan ini sesuai dengan tipe di XML kamu (LinearLayout atau Button)

    // Variabel untuk menyimpan ID Kamar yang dilempar dari Beranda
    private String idKamar = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamar_tipe2); // Pastikan nama layout XML-nya benar

        // =========================================================================
        // 1. TANGKAP ID_KAMAR DARI ADAPTER (Beranda)
        // Kita tangkap secara fleksibel (bisa Int atau String) agar tidak crash
        // =========================================================================
        if (getIntent().hasExtra("ID_KAMAR")) {
            Object idObj = getIntent().getExtras().get("ID_KAMAR");
            if (idObj != null) {
                idKamar = String.valueOf(idObj);
            }
        }

        // =========================================================================
        // 2. HUBUNGKAN ID DARI XML
        // (Sesuaikan R.id.xxx dengan ID yang ada di file activity_kamar_tipe2.xml)
        // =========================================================================
        btnBack = findViewById(R.id.btn_back);
        btnPesanSekarang = findViewById(R.id.btn_pesan);

        // =========================================================================
        // 3. AKSI TOMBOL KEMBALI
        // =========================================================================
        btnBack.setOnClickListener(v -> finish());

        // =========================================================================
        // 4. AKSI TOMBOL PESAN SEKARANG
        // =========================================================================
        btnPesanSekarang.setOnClickListener(v -> {
            // Cek dulu apakah ID Kamar berhasil ditangkap atau kosong
            if (idKamar == null || idKamar.isEmpty()) {
                Toast.makeText(this, "Error: Data kamar gagal dimuat!", Toast.LENGTH_SHORT).show();
                return; // Hentikan proses jika ID kosong
            }

            // 👇 INI KODE TAMBAHAN YANG KAMU MINTA 👇
            // Meneruskan ID_KAMAR ke halaman PemesananTipe2Activity
            Intent intent = new Intent(KamarTipe2Activity.this, PemesananTipe2Activity.class);

            // Variabel "idKamar" di bawah ini sudah berisi angka dinamis (misal: 24)
            intent.putExtra("ID_KAMAR", idKamar);

            startActivity(intent);
        });
    }
}