package com.delfy.kost;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileUserActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        // PERBAIKAN: navHome sekarang menggunakan LinearLayout, sesuai dengan yang ada di XML
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout menuKamar = findViewById(R.id.menu_kamar_sewa);
        LinearLayout menuRiwayat = findViewById(R.id.menu_riwayat);
        LinearLayout menuKomplain = findViewById(R.id.menu_komplain);
        LinearLayout btnLogout = findViewById(R.id.btn_logout);

        // Aksi Tombol Home di Navigasi Bawah
        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HomepageActivity.class));
            finish();
        });

        // Aksi Menu Profil
        menuKamar.setOnClickListener(v -> startActivity(new Intent(this, KamarSewaActivity.class)));
        menuRiwayat.setOnClickListener(v -> startActivity(new Intent(this, RiwayatPembayaranActivity.class)));
        menuKomplain.setOnClickListener(v -> startActivity(new Intent(this, KomplainActivity.class)));

        // Aksi Tombol Log Out
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            // Hapus tumpukan layar agar pengguna tidak bisa menekan tombol "Back" ke profil setelah logout
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}