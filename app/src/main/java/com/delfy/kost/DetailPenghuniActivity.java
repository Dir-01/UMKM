package com.delfy.kost;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.PenyewaDetailResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPenghuniActivity extends AppCompatActivity {

    private TextView tvNama, tvEmail, tvHp;
    private ImageView ivKtp, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_penghuni);

        // Inisialisasi View
        tvNama = findViewById(R.id.tv_det_nama);
        tvEmail = findViewById(R.id.tv_det_email);
        tvHp = findViewById(R.id.tv_det_hp);
        ivKtp = findViewById(R.id.iv_foto_ktp);
        btnBack = findViewById(R.id.btn_back_detail_penghuni);
        btnBack.setOnClickListener(v -> finish());

        // Ambil ID dari Intent (dikirim oleh PenghuniAdapter)
        String idPenyewa = getIntent().getStringExtra("ID_PENYEWA");

        if (idPenyewa != null) {
            getDetailDariServer(idPenyewa);
        } else {
            Toast.makeText(this, "ID tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDetailDariServer(String id) {
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = "Bearer " + pref.getString("token", "");

        ApiClient.getClient().getDetailPenyewa(token, id).enqueue(new Callback<PenyewaDetailResponse>() {
            @Override
            public void onResponse(Call<PenyewaDetailResponse> call, Response<PenyewaDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PenyewaDetailResponse.PenyewaModel penyewa = response.body().getData();

                    // Tampilkan data ke UI
                    tvNama.setText("Nama: " + penyewa.getNama());
                    tvEmail.setText("Email: " + penyewa.getEmail());
                    tvHp.setText("No. HP: " + penyewa.getNoHp());


                    // Tampilkan Foto KTP dari storage Laravel
                    String urlKtp = "http://10.0.2.2:8000/storage/ktp/" + penyewa.getFotoKtp();
                    // Ganti IP-nya agar foto KTP bisa terlihat
                    //String urlKtp = "http://192.168.1.15:8000/storage/ktp/" + penyewa.getFotoKtp();
                    Glide.with(DetailPenghuniActivity.this)
                            .load(urlKtp)
                            .placeholder(android.R.drawable.ic_menu_gallery)
                            .into(ivKtp);
                } else {
                    Toast.makeText(DetailPenghuniActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PenyewaDetailResponse> call, Throwable t) {
                Toast.makeText(DetailPenghuniActivity.this, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }
}