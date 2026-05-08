package com.delfy.kost;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.delfy.kost.adapter.PenghuniAdapter; // Kamu perlu buat Adapter ini
import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.RiwayatResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataPenghuniActivity extends AppCompatActivity {

    private RecyclerView rvPenghuni;
    private PenghuniAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_penghuni);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        rvPenghuni = findViewById(R.id.rv_penghuni); // Pastikan ID ini ada di XML kamu
        rvPenghuni.setLayoutManager(new LinearLayoutManager(this));

        loadDataPenghuni();
    }

    private void loadDataPenghuni() {
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = "Bearer " + pref.getString("token", "");

        ApiClient.getClient().getDaftarPenghuni(token).enqueue(new Callback<RiwayatResponse>() {
            @Override
            public void onResponse(Call<RiwayatResponse> call, Response<RiwayatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new PenghuniAdapter(DataPenghuniActivity.this, response.body().getData());
                    rvPenghuni.setAdapter(adapter);
                } else {
                    Toast.makeText(DataPenghuniActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RiwayatResponse> call, Throwable t) {
                Toast.makeText(DataPenghuniActivity.this, "Error Jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}