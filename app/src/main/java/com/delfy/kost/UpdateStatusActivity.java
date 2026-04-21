package com.delfy.kost;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateStatusActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);

        ImageView btnBack = findViewById(R.id.btn_back);
        LinearLayout btnKembali = findViewById(R.id.btn_kembali);
        LinearLayout btnSimpan = findViewById(R.id.btn_simpan);

        btnBack.setOnClickListener(v -> finish());
        btnKembali.setOnClickListener(v -> finish());
        btnSimpan.setOnClickListener(v -> {
            startActivity(new Intent(this, UpdateStatusBerhasilActivity.class));
            finish();
        });
    }
}