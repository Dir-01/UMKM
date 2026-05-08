package com.delfy.kost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileUserActivity extends AppCompatActivity {

    private TextView tvNamaUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        // 1. Hubungkan ID
        tvNamaUser = findViewById(R.id.tv_nama_user);
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout menuRiwayat = findViewById(R.id.menu_riwayat);
        LinearLayout menuKomplain = findViewById(R.id.menu_komplain); // Tambahkan jika ingin digunakan
        LinearLayout btnLogout = findViewById(R.id.btn_logout);

        // 2. AMBIL NAMA DARI SHAREDPREFERENCES
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        // Mengambil nama yang disimpan saat login, defaultnya "Penyewa Kost"
        String namaLogin = pref.getString("nama", "Penyewa Kost");
        tvNamaUser.setText(namaLogin);

        // 3. Aksi Navigasi Bottom Bar
        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HomepageActivity.class));
            overridePendingTransition(0, 0); // Agar transisi mulus
            finish();
        });

        // 4. Aksi Menu
        menuRiwayat.setOnClickListener(v -> {
            startActivity(new Intent(this, RiwayatPembayaranActivity.class));
        });

        if (menuKomplain != null) {
            menuKomplain.setOnClickListener(v -> {
                // Hapus tanda '//' agar perintah pindah halamannya aktif
                startActivity(new Intent(this, KomplainActivity.class));

                // (Opsional) Boleh dihapus atau dibiarkan, ini hanya pesan kecil
                // Toast.makeText(this, "Fitur Komplain", Toast.LENGTH_SHORT).show();
            });
        }

        // 5. Aksi Log Out
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginPenyewaActivity.class);
            // Membersihkan tumpukan activity agar tidak bisa "back" ke profil
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}