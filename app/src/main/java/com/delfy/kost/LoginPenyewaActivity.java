package com.delfy.kost;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast; // Tambahkan ini untuk memunculkan pesan
import androidx.appcompat.app.AppCompatActivity;

public class LoginPenyewaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_penyewa);

        // 1. Inisialisasi ID yang BENAR-BENAR ADA di file XML
        LinearLayout btnLogin = findViewById(R.id.btn_login_penyewa);
        TextView tvLupaPassword = findViewById(R.id.tv_lupa_password);

        // 2. Aksi tombol Login Penyewa
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(LoginPenyewaActivity.this, HomepageActivity.class));
            finish(); // Tutup halaman login
        });

        // 3. Aksi teks Lupa Password
        tvLupaPassword.setOnClickListener(v -> {
            // Karena kita belum buat halaman lupa password, kita beri pesan sementara dulu
            Toast.makeText(LoginPenyewaActivity.this, "Fitur Lupa Password", Toast.LENGTH_SHORT).show();
        });
    }
}