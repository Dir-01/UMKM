package com.delfy.kost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // WAJIB ADA INI
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delfy.kost.adapter.PesananAdapter;
import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.RiwayatResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PesananActivity extends AppCompatActivity {
    private RecyclerView rvPesanan;
    private PesananAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);

        // Tombol Back
        findViewById(R.id.btn_back_pesanan).setOnClickListener(v -> finish());

        // Inisialisasi RecyclerView
        rvPesanan = findViewById(R.id.rv_pesanan);
        rvPesanan.setLayoutManager(new LinearLayoutManager(this));

        // Panggil Data
        ambilDataPesanan();
    }

    private void ambilDataPesanan() {
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = pref.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Sesi habis, login ulang!", Toast.LENGTH_SHORT).show();
            return;
        }

        String tokenBearer = "Bearer " + token;

        // Tembak API
        ApiClient.getClient().getSemuaPesanan(tokenBearer).enqueue(new Callback<RiwayatResponse>() {
            @Override
            public void onResponse(Call<RiwayatResponse> call, Response<RiwayatResponse> response) {
                // --- BAGIAN DEBUGGING ---
                if (response.isSuccessful() && response.body() != null) {
                    List<RiwayatResponse.RiwayatModel> list = response.body().getData();

                    // Cek jumlah data yang datang di Logcat
                    Log.d("Delfy_Debug", "Jumlah data pesanan: " + (list != null ? list.size() : 0));

                    if (list != null && list.size() > 0) {
                        adapter = new PesananAdapter(list, PesananActivity.this);
                        rvPesanan.setAdapter(adapter);
                    } else {
                        // Jika data 0, biasanya karena id_pemilik di database masih NULL
                        Log.w("Delfy_Debug", "Response sukses tapi list kosong (id_pemilik mungkin Null)");
                        Toast.makeText(PesananActivity.this, "Tidak ada pesanan untuk akun ini", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Cek jika error 401 (Unauthorized) atau 500 (Server Error)
                    Log.e("Delfy_Debug", "Error Server! Code: " + response.code());
                    Toast.makeText(PesananActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RiwayatResponse> call, Throwable t) {
                // Cek jika koneksi ke laptop terputus
                Log.e("Delfy_Debug", "Koneksi Gagal: " + t.getMessage());
                Toast.makeText(PesananActivity.this, "Koneksi Bermasalah!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}