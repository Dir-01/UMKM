package com.delfy.kost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.KomplainResponse; // <-- Ini yang baru kita tambahkan

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KomplainActivity extends AppCompatActivity {
    private EditText etKamar, etSubjek, etDeskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komplain);

        etKamar = findViewById(R.id.et_nomor_kamar);
        etSubjek = findViewById(R.id.et_subjek);
        etDeskripsi = findViewById(R.id.et_detail_keluhan);
        LinearLayout btnKirim = findViewById(R.id.btn_kirim_komplain);

        btnKirim.setOnClickListener(v -> prosesKirimKomplain());
    }

    private void prosesKirimKomplain() {
        String kamar = etKamar.getText().toString().trim();
        String subjek = etSubjek.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();

        if (kamar.isEmpty() || subjek.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(this, "Harap isi semua data komplain terlebih dahulu!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = "Bearer " + pref.getString("token", "");

        // <-- TAMBAHKAN BARIS INI UNTUK MENGAMBIL ID PENYEWA
        String idPenyewa = pref.getString("id_penyewa", "");

        // Panggil API (Perhatikan: idPenyewa disisipkan setelah token)
        ApiClient.getClient().kirimKomplain(token, idPenyewa, kamar, subjek, deskripsi)
                .enqueue(new Callback<KomplainResponse>() {
                    @Override
                    public void onResponse(Call<KomplainResponse> call, Response<KomplainResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(KomplainActivity.this, "Komplain Berhasil Dikirim!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(KomplainActivity.this, KomplainBerhasilActivity.class));
                            finish();
                        } else {
                            Toast.makeText(KomplainActivity.this, "Gagal Mengirim! Kode Error: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<KomplainResponse> call, Throwable t) {
                        Toast.makeText(KomplainActivity.this, "Error Jaringan: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}