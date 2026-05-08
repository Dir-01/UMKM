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
        ImageView btnBack = findViewById(R.id.btn_back_detail);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Inisialisasi View
        TextView tvNama = findViewById(R.id.tv_detail_nama);
        TextView tvNoHp = findViewById(R.id.tv_detail_hp);
        TextView tvTipe = findViewById(R.id.tv_detail_tipe);
        TextView tvDurasi = findViewById(R.id.tv_detail_durasi);
        TextView tvMetode = findViewById(R.id.tv_detail_metode);
        TextView tvTotal = findViewById(R.id.tv_detail_total);
        ImageView ivBukti = findViewById(R.id.iv_bukti_bayar);
        Button btnKonfirmasi = findViewById(R.id.btn_konfirmasi_final);

        // Ambil Data dari Intent
        idTransaksi = getIntent().getStringExtra("ID_TRANSAKSI");

        // Tampilkan Nama dan No HP
        String nama = getIntent().getStringExtra("NAMA");
        String noHp = getIntent().getStringExtra("NO_HP");

        tvNama.setText("Nama: " + (nama != null ? nama : "-"));
        tvNoHp.setText("No. Hp: " + (noHp != null ? noHp : "-"));

        tvTipe.setText(getIntent().getStringExtra("TIPE"));
        tvDurasi.setText("Durasi sewa : " + getIntent().getStringExtra("DURASI") + " Bulan");
        tvMetode.setText("Metode Pembayaran: " + getIntent().getStringExtra("METODE"));
        tvTotal.setText("Total Pembayaran: Rp. " + getIntent().getStringExtra("TOTAL"));

        // Load Foto Bukti Bayar
        String namaFileFoto = getIntent().getStringExtra("BUKTI");
        String urlFoto = "http://10.0.2.2:8000/storage/bukti_bayar/" + namaFileFoto;
        //String urlFoto = "http://192.168.1.15:8000/storage/bukti_bayar/" + namaFileFoto;
        Glide.with(this).load(urlFoto).into(ivBukti);

        // Aksi Tombol Konfirmasi
        btnKonfirmasi.setOnClickListener(v -> {
            // Memanggil fungsi konfirmasiPesanan
            konfirmasiPesanan();
        });
    } // Penutup onCreate yang benar

    private void konfirmasiPesanan() {
        // 1. Validasi ID agar tidak null
        if (idTransaksi == null || idTransaksi.isEmpty()) {
            Toast.makeText(this, "Error: ID Transaksi tidak ditemukan!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = "Bearer " + pref.getString("token", "");

        ApiClient.getClient().konfirmasiPembayaran(token, idTransaksi).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetailPesananActivity.this, "Pesanan Berhasil Dikonfirmasi!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(DetailPesananActivity.this, "Gagal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(DetailPesananActivity.this, "Error Jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
} // Penutup Class yang benar