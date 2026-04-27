package com.delfy.kost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileUserActivity extends AppCompatActivity {

    private TextView tvNamaUser; // Tambahkan variabel TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        // 1. Hubungkan ID
        tvNamaUser = findViewById(R.id.tv_nama_user); // Pastikan di XML ID-nya ini
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout menuRiwayat = findViewById(R.id.menu_riwayat);
        LinearLayout btnLogout = findViewById(R.id.btn_logout);

        // 2. AMBIL NAMA DARI BRANKAS
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String namaLogin = pref.getString("nama", "Penyewa Kost");
        tvNamaUser.setText(namaLogin); // <--- Nama otomatis ganti sesuai akun

        // 3. Aksi Navigasi
        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HomepageActivity.class));
            finish();
        });

        menuRiwayat.setOnClickListener(v -> startActivity(new Intent(this, RiwayatPembayaranActivity.class)));

        // 4. Aksi Log Out
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear(); // Hapus data login
            editor.apply();

            Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginPenyewaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}