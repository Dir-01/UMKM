package com.delfy.kost;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class KamarTipe2Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // PERBAIKAN 1: Ganti layout-nya ke activity_kamar_tipe2 (Bukan tipe 1 lagi)
        setContentView(R.layout.activity_kamar_tipe2);

        ImageView btnBack = findViewById(R.id.btn_back);
        LinearLayout btnPesan = findViewById(R.id.btn_pesan);

        btnBack.setOnClickListener(v -> finish());

        // PERBAIKAN 2: Arahkan ke halaman PemesananTipe2Activity
        btnPesan.setOnClickListener(v -> startActivity(new Intent(this, PemesananTipe2Activity.class)));
    }
}