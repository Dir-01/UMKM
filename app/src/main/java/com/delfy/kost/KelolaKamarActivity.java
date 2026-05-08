package com.delfy.kost;

import android.widget.ImageView;
import com.delfy.kost.adapter.KamarAdapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.ApiService;
import com.delfy.kost.api.KamarResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KelolaKamarActivity extends AppCompatActivity {

    private RecyclerView rvKamar;
    private KamarAdapter adapter;
    private android.widget.LinearLayout btnTambahKamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_kamar);
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        rvKamar = findViewById(R.id.rvKamar);
        rvKamar.setLayoutManager(new LinearLayoutManager(this));

        btnTambahKamar = findViewById(R.id.btn_tambah_kamar);
        btnTambahKamar.setOnClickListener(v -> {
            Intent intent = new Intent(KelolaKamarActivity.this, TambahKamarActivity.class);
            startActivity(intent);
        });

        // Hapus pemanggilan ambilDataKamar() di sini agar tidak dobel dengan onResume
    }

    // ... (kode atasnya sama) ...

    private void ambilDataKamar() {
        // --- PASTIKAN MENCARI DI BRANKAS YANG SAMA ---
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ApiService apiService = ApiClient.getClient();
        String tokenBearer = "Bearer " + token;

// ... (lanjutkan pemanggilan API seperti biasa) ...



        apiService.getKamar(tokenBearer).enqueue(new Callback<KamarResponse>() {
            @Override
            public void onResponse(Call<KamarResponse> call, Response<KamarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<KamarResponse.KamarModel> daftarKamar = response.body().getData();
                    adapter = new KamarAdapter(daftarKamar);
                    rvKamar.setAdapter(adapter);
                } else {
                    // JIKA GAGAL, KITA INTIP APA KATA LARAVEL
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown Error";
                        Log.e("ERROR_API_KAMAR", "Pesan dari Server: " + errorBody);
                        Toast.makeText(KelolaKamarActivity.this, "Ditolak Server: Cek Logcat!", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<KamarResponse> call, Throwable t) {
                Log.e("ERROR_API_KAMAR", "Gagal total: " + t.getMessage());
                Toast.makeText(KelolaKamarActivity.this, "Koneksi Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ambilDataKamar();
    }
}