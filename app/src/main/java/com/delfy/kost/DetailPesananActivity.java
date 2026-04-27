package com.delfy.kost;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPesananActivity extends AppCompatActivity {

    private String idTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);

        // Tombol Back
        findViewById(R.id.btn_back_detail).setOnClickListener(v -> finish());

        // Inisialisasi View
        TextView tvTipe = findViewById(R.id.tv_detail_tipe);
        TextView tvDurasi = findViewById(R.id.tv_detail_durasi);
        TextView tvMetode = findViewById(R.id.tv_detail_metode);
        TextView tvTotal = findViewById(R.id.tv_detail_total);
        ImageView ivBukti = findViewById(R.id.iv_bukti_bayar);
        Button btnKonfirmasi = findViewById(R.id.btn_konfirmasi_final);

        // Ambil Data dari Intent (Kiriman Adapter)
        idTransaksi = getIntent().getStringExtra("ID_TRANSAKSI");
        tvTipe.setText(getIntent().getStringExtra("TIPE"));
        tvDurasi.setText("Durasi sewa : " + getIntent().getStringExtra("DURASI") + " Bulan");
        tvMetode.setText("Metode Pembayaran: " + getIntent().getStringExtra("METODE"));
        tvTotal.setText("Total Pembayaran: Rp. " + getIntent().getStringExtra("TOTAL"));

        // Load Foto Bukti Bayar Pakai Glide
        String namaFileFoto = getIntent().getStringExtra("BUKTI");
        // SESUAIKAN IP INI DENGAN IP LAPTOPMU SEPERTI DI APICLIENT
        String urlFoto = "http://10.0.2.2:8000/storage/bukti_bayar/" + namaFileFoto;

        Glide.with(this).load(urlFoto).into(ivBukti);

        // Aksi Tombol Konfirmasi
        btnKonfirmasi.setOnClickListener(v -> konfirmasiPesanan());
    }

    private void konfirmasiPesanan() {
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = "Bearer " + pref.getString("token", "");

        ApiClient.getClient().konfirmasiPembayaran(token, idTransaksi).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailPesananActivity.this, "Pesanan Berhasil Dikonfirmasi!", Toast.LENGTH_SHORT).show();
                    finish(); // Tutup halaman ini, kembali ke list
                } else {
                    Toast.makeText(DetailPesananActivity.this, "Gagal mengonfirmasi pesanan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(DetailPesananActivity.this, "Error Jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }
}