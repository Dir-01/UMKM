package com.delfy.kost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView; // <-- Ini yang baru saja ditambahkan
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delfy.kost.adapter.KomplainAdapter;
import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.KomplainModel;
import com.delfy.kost.api.ListKomplainResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KomplainMasukActivity extends AppCompatActivity {

    private RecyclerView rvKomplain;
    private KomplainAdapter adapter;
    private TextView tvSelesai, tvMenunggu;
    private List<KomplainModel> listKomplain = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komplain_masuk);

        // Inisialisasi View
        rvKomplain = findViewById(R.id.rv_komplain_masuk); // Pastikan ID ini ada di XML
        tvSelesai = findViewById(R.id.tv_count_selesai);   // Beri ID pada angka '3' di XML kamu
        tvMenunggu = findViewById(R.id.tv_count_menunggu); // Beri ID pada angka '2' di XML kamu

        // Setup RecyclerView
        rvKomplain.setLayoutManager(new LinearLayoutManager(this));
        adapter = new KomplainAdapter(this, listKomplain, komplain -> {
            // Aksi saat tombol 'Update Status' diklik
            Intent intent = new Intent(this, UpdateStatusActivity.class);
            intent.putExtra("ID_KOMPLAIN", komplain.getIdKomplain());
            startActivity(intent);
        });
        rvKomplain.setAdapter(adapter);

        loadDataKomplain();
    }

    private void loadDataKomplain() {
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = "Bearer " + pref.getString("token", "");

        ApiClient.getClient().getKomplainMasuk(token).enqueue(new Callback<ListKomplainResponse>() {
            @Override
            public void onResponse(Call<ListKomplainResponse> call, Response<ListKomplainResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listKomplain = response.body().getData();
                    adapter.updateData(listKomplain);
                    updateStatistik(listKomplain);
                }
            }

            @Override
            public void onFailure(Call<ListKomplainResponse> call, Throwable t) {
                Toast.makeText(KomplainMasukActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatistik(List<KomplainModel> data) {
        int selesai = 0;
        int menunggu = 0;

        for (KomplainModel k : data) {
            if (k.getStatus().equalsIgnoreCase("Selesai")) selesai++;
            else if (k.getStatus().equalsIgnoreCase("Menunggu")) menunggu++;
        }

        tvSelesai.setText(String.valueOf(selesai));
        tvMenunggu.setText(String.valueOf(menunggu));
    }
}