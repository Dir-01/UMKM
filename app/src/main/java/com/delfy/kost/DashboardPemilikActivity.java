package com.delfy.kost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Import untuk API
import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.ApiService;
import com.delfy.kost.api.KamarResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardPemilikActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_pemilik);

        ImageView btnBack = findViewById(R.id.btn_back);
        LinearLayout cardKelola = findViewById(R.id.card_kelola_kamar);
        LinearLayout cardPenghuni = findViewById(R.id.card_data_penghuni);
        LinearLayout cardRingkasan = findViewById(R.id.card_ringkasan_kost);
        LinearLayout cardKomplain = findViewById(R.id.card_komplain_masuk);

        // --- 1. FITUR LOGOUT (Hapus Token) ---
        btnBack.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);

            // PERBAIKAN 1: Tambahkan baris pemanggilan 'editor' ini agar tidak Error
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(DashboardPemilikActivity.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();

            // Ubah arah logout agar kembali ke Login Pemilik
            startActivity(new Intent(this, LoginPemilikActivity.class));
            finish();
        });

        // --- 2. MENU NAVIGASI ---
        cardKelola.setOnClickListener(v -> startActivity(new Intent(this, KelolaKamarActivity.class)));
        cardPenghuni.setOnClickListener(v -> startActivity(new Intent(this, DataPenghuniActivity.class)));
        cardRingkasan.setOnClickListener(v -> startActivity(new Intent(this, RingkasanKostActivity.class)));
        cardKomplain.setOnClickListener(v -> startActivity(new Intent(this, KomplainMasukActivity.class)));

        // --- 3. TEST AMBIL DATA DARI LARAVEL ---
        testAmbilDataKamar();
    }

    private void testAmbilDataKamar() {
        // PERBAIKAN 2: Ganti "KostPrefs" jadi "USER_DATA" dan "TOKEN" jadi "token"
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        // Jika token tidak ada (misal di-clear atau belum login)
        if (token == null) {
            Toast.makeText(this, "Sesi habis, silakan login ulang!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginPemilikActivity.class));
            finish();
            return;
        }

        // Siapkan alat penembak API
        ApiService apiService = ApiClient.getClient();
        String tokenBearer = "Bearer " + token;

        // Tembak API kamar
        apiService.getKamar(tokenBearer).enqueue(new Callback<KamarResponse>() {
            @Override
            public void onResponse(Call<KamarResponse> call, Response<KamarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<KamarResponse.KamarModel> daftarKamar = response.body().getData();

                    // Tampilkan jumlah kamar yang berhasil ditarik dari database Laravel
                    int totalKamar = daftarKamar.size();
                    Toast.makeText(DashboardPemilikActivity.this, "Sukses terhubung! Ada " + totalKamar + " kamar di database.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DashboardPemilikActivity.this, "Gagal mengambil data dari server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KamarResponse> call, Throwable t) {
                Toast.makeText(DashboardPemilikActivity.this, "Koneksi Error: Pastikan Laravel menyala!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}