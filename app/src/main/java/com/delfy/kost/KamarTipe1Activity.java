package com.delfy.kost;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class KamarTipe1Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamar_tipe1);

        ImageView btnBack = findViewById(R.id.btn_back);
        LinearLayout btnPesan = findViewById(R.id.btn_pesan);

        btnBack.setOnClickListener(v -> finish());
        btnPesan.setOnClickListener(v -> startActivity(new Intent(this, PemesananActivity.class)));
    }
}