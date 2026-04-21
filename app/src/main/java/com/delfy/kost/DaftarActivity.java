package com.delfy.kost;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DaftarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        LinearLayout btnDaftar = findViewById(R.id.btn_daftar_akun);

        btnDaftar.setOnClickListener(v -> {
            Toast.makeText(this, "Pendaftaran Berhasil! Silakan Login.", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke halaman Login Penyewa
        });
    }
}