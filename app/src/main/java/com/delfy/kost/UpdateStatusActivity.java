package com.delfy.kost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.KomplainResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateStatusActivity extends AppCompatActivity {

    private String idKomplain;
    private RadioGroup rgStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);

        // 1. Hubungkan ID
        ImageView btnBack = findViewById(R.id.btn_back);
        LinearLayout btnKembali = findViewById(R.id.btn_kembali);
        LinearLayout btnSimpan = findViewById(R.id.btn_simpan);

        TextView tvNama = findViewById(R.id.tvNama);
        TextView tvNoKamar = findViewById(R.id.tvNoKamar);
        TextView tvKeluhan = findViewById(R.id.tvKeluhan);
        TextView tvStatus = findViewById(R.id.tvStatus);
        rgStatus = findViewById(R.id.rgStatus);

        // 2. Ambil data dari Intent Adapter
        idKomplain = getIntent().getStringExtra("id_komplain");
        String nama = getIntent().getStringExtra("nama_penghuni");
        String kamar = getIntent().getStringExtra("no_kamar");
        String keluhan = getIntent().getStringExtra("deskripsi");
        String status = getIntent().getStringExtra("status");

        if (idKomplain == null) {
            Toast.makeText(this, "Error: ID Komplain tidak ditemukan!", Toast.LENGTH_LONG).show();
        }
        // 3. Tampilkan ke Layar
        // Set text ke view
        tvNama.setText("Nama Penghuni : " + (nama != null ? nama : "-"));
        tvNoKamar.setText("No. Kamar : " + (kamar != null ? kamar : "-"));
        tvKeluhan.setText("Keluhan : " + (keluhan != null ? keluhan : "-"));
        tvStatus.setText("Status : " + (status != null ? status : "-"));

        // 4. Aksi Tombol
        btnBack.setOnClickListener(v -> finish());
        btnKembali.setOnClickListener(v -> finish());

        btnSimpan.setOnClickListener(v -> prosesUpdateStatus());
    }

    private void prosesUpdateStatus() {
        int selectedId = rgStatus.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Pilih status terlebih dahulu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cari tahu radio button mana yang diklik
        RadioButton rbTerpilih = findViewById(selectedId);
        String statusBaru = rbTerpilih.getText().toString();

        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = "Bearer " + pref.getString("token", "");

        Toast.makeText(this, "Menyimpan...", Toast.LENGTH_SHORT).show();

        // Panggil API (Kita akan tambahkan ini di ApiService setelah ini)
        ApiClient.getClient().updateStatusKomplain(token, idKomplain, statusBaru)
                .enqueue(new Callback<KomplainResponse>() {
                    @Override
                    public void onResponse(Call<KomplainResponse> call, Response<KomplainResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(UpdateStatusActivity.this, "Status berhasil diperbarui!", Toast.LENGTH_SHORT).show();

                            // KEMBALI KE HALAMAN KOMPLAIN MASUK (Sesuai keinginanmu)
                            // FLAG_ACTIVITY_CLEAR_TOP berfungsi untuk merefresh halaman tujuan
                            Intent intent = new Intent(UpdateStatusActivity.this, KomplainMasukActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(UpdateStatusActivity.this, "Gagal Update: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<KomplainResponse> call, Throwable t) {
                        Toast.makeText(UpdateStatusActivity.this, "Error Jaringan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}