package com.delfy.kost;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class KomplainBerhasilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komplain_berhasil);

        // Menghubungkan tombol back dari XML
        ImageView btnBack = findViewById(R.id.btn_back);

        // Aksi ketika tombol back ditekan
        btnBack.setOnClickListener(v -> {
            // finish() akan menutup halaman ini dan otomatis kembali ke halaman sebelumnya
            finish();
        });
    }
}