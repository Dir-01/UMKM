package com.delfy.kost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.delfy.kost.adapter.KamarHomepageAdapter;
import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.ApiService;
import com.delfy.kost.api.KamarResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomepageActivity extends AppCompatActivity {

    private RecyclerView rvKamar;
    private KamarHomepageAdapter adapter;
    private List<KamarResponse.KamarModel> listKamar = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // 1. Setup User Info
        TextView tvNamaUser = findViewById(R.id.tv_nama_user);
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        tvNamaUser.setText(sharedPreferences.getString("nama", "Penghuni Baru"));

        // 2. Setup RecyclerView
        rvKamar = findViewById(R.id.rv_kamar);
        rvKamar.setLayoutManager(new LinearLayoutManager(this));
        adapter = new KamarHomepageAdapter(this, listKamar);
        rvKamar.setAdapter(adapter);

        // 3. Ambil Data dari Laravel
        getDataKamar();

        // 4. Navigasi
        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileUserActivity.class));
            finish();
        });
    }

    private void getDataKamar() {
        String token = getSharedPreferences("USER_DATA", MODE_PRIVATE).getString("token", "");
        ApiService apiService = ApiClient.getClient();

        apiService.getKamar("Bearer " + token).enqueue(new Callback<KamarResponse>() {
            @Override
            public void onResponse(Call<KamarResponse> call, Response<KamarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listKamar.clear();
                    listKamar.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<KamarResponse> call, Throwable t) {
                Toast.makeText(HomepageActivity.this, "Gagal memuat data kamar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}