package com.delfy.kost;

import android.content.Intent; // Pastikan ini ditambahkan
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout btnPemilik, btnPenyewa;
    private TextView tvDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi komponen View
        btnPemilik = findViewById(R.id.btn_pemilik);
        btnPenyewa = findViewById(R.id.btn_penyewa);
        tvDaftar = findViewById(R.id.tv_daftar);

        // Aksi ketika tombol PEMILIK ditekan
        btnPemilik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Berpindah ke halaman Login Pemilik
                Intent intent = new Intent(LoginActivity.this, LoginPemilikActivity.class);
                startActivity(intent);
            }
        });

        // Aksi ketika tombol PENYEWA ditekan
        btnPenyewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Berpindah ke halaman Login Penyewa
                Intent intent = new Intent(LoginActivity.this, LoginPenyewaActivity.class);
                startActivity(intent);
            }
        });

        // Aksi ketika teks DAFTAR ditekan
        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Berpindah ke halaman Daftar Akun Baru
                Intent intent = new Intent(LoginActivity.this, DaftarActivity.class);
                startActivity(intent);
            }
        });
    }
}