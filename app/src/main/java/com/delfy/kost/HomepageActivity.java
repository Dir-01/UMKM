package com.delfy.kost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // --- FITUR GANTI NAMA OTOMATIS ---
        TextView tvNamaUser = findViewById(R.id.tv_nama_user);

        // Buka brankas USER_DATA
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        // Ambil data "nama", jika kosong maka tulis "Penghuni Baru"
        String namaUser = sharedPreferences.getString("nama", "Penghuni Baru");

        // Set tulisan di layar
        tvNamaUser.setText(namaUser);
        // ---------------------------------

        LinearLayout cardKamar1 = findViewById(R.id.card_tipe_1);
        LinearLayout cardKamar2 = findViewById(R.id.card_tipe_2);
        LinearLayout navProfile = findViewById(R.id.nav_profile);

        cardKamar1.setOnClickListener(v -> startActivity(new Intent(this, KamarTipe1Activity.class)));
        cardKamar2.setOnClickListener(v -> startActivity(new Intent(this, KamarTipe2Activity.class)));

        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileUserActivity.class));
            finish(); // Pindah tab
        });
    }
}