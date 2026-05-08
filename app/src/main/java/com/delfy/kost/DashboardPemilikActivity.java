package com.delfy.kost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        // 1. Inisialisasi View
        ImageView btnBack = findViewById(R.id.btn_back);
        LinearLayout cardKelola = findViewById(R.id.card_kelola_kamar);
        LinearLayout cardPenghuni = findViewById(R.id.card_data_penghuni);
        LinearLayout cardKomplain = findViewById(R.id.card_komplain_masuk);
        LinearLayout cardPesanan = findViewById(R.id.card_pesanan);

        // 2. FITUR LOGOUT
        btnBack.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Toast.makeText(DashboardPemilikActivity.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginPemilikActivity.class));
            finish();
        });

        // 3. MENU NAVIGASI (Hanya 4 Menu Utama)
        cardKelola.setOnClickListener(v -> startActivity(new Intent(this, KelolaKamarActivity.class)));
        cardPenghuni.setOnClickListener(v -> startActivity(new Intent(this, DataPenghuniActivity.class)));
        cardKomplain.setOnClickListener(v -> startActivity(new Intent(this, KomplainMasukActivity.class)));
        cardPesanan.setOnClickListener(v -> startActivity(new Intent(this, PesananActivity.class)));

        // 4. TEST AMBIL DATA
        testAmbilDataKamar();
    }

    private void testAmbilDataKamar() {
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            startActivity(new Intent(this, LoginPemilikActivity.class));
            finish();
            return;
        }

        ApiService apiService = ApiClient.getClient();
        String tokenBearer = "Bearer " + token;

        apiService.getKamar(tokenBearer).enqueue(new Callback<KamarResponse>() {
            @Override
            public void onResponse(Call<KamarResponse> call, Response<KamarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<KamarResponse.KamarModel> daftarKamar = response.body().getData();
                    int totalKamar = (daftarKamar != null) ? daftarKamar.size() : 0;
                    Toast.makeText(DashboardPemilikActivity.this, "Terhubung! Ada " + totalKamar + " kamar.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KamarResponse> call, Throwable t) {
                Toast.makeText(DashboardPemilikActivity.this, "Koneksi ke Server Gagal!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}