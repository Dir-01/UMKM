package com.delfy.kost;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delfy.kost.adapter.RiwayatAdapter;
import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.RiwayatResponse; // Import untuk wadah List
import com.delfy.kost.api.TransaksiResponse; // Import untuk satuan item

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatPembayaranActivity extends AppCompatActivity {

    private RecyclerView rvRiwayat;
    private RiwayatAdapter adapter;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_pembayaran);

        // 1. Inisialisasi
        btnBack = findViewById(R.id.btn_back_riwayat);
        rvRiwayat = findViewById(R.id.rv_riwayat);

        rvRiwayat.setLayoutManager(new LinearLayoutManager(this));
        btnBack.setOnClickListener(v -> finish());

        // 2. Ambil Data dari Laravel
        muatDataRiwayat();
    }

    private void muatDataRiwayat() {
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = "Bearer " + pref.getString("token", "");

        // PENTING: Menggunakan RiwayatResponse untuk menerima banyak data (List)
        ApiClient.getClient().getRiwayat(token).enqueue(new Callback<RiwayatResponse>() {
            @Override
            public void onResponse(Call<RiwayatResponse> call, Response<RiwayatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    // --- PERBAIKAN DI SINI: Samakan tipe datanya dengan RiwayatResponse.RiwayatModel ---
                    List<RiwayatResponse.RiwayatModel> list = response.body().getData();

                    if (list != null && !list.isEmpty()) {
                        // Pastikan RiwayatAdapter kamu juga sudah menggunakan model yang sama
                        adapter = new RiwayatAdapter(list, RiwayatPembayaranActivity.this);
                        rvRiwayat.setAdapter(adapter);
                    } else {
                        Toast.makeText(RiwayatPembayaranActivity.this, "Belum ada riwayat", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RiwayatResponse> call, Throwable t) {
                Toast.makeText(RiwayatPembayaranActivity.this, "Gagal koneksi server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}