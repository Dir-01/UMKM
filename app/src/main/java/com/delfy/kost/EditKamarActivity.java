package com.delfy.kost;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.ApiService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditKamarActivity extends AppCompatActivity {

    private String idKamar, tipeKamar, statusLama;
    private TextView tvTipe;
    private RadioGroup rgStatus;
    // Tambahkan rbPerbaikan di sini
    private RadioButton rbKosong, rbTerisi, rbPerbaikan;
    private Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kamar);

        // 1. Hubungkan ID
        tvTipe = findViewById(R.id.tv_edit_tipe);
        rgStatus = findViewById(R.id.rg_status);
        rbKosong = findViewById(R.id.rb_kosong);
        rbTerisi = findViewById(R.id.rb_terisi);
        rbPerbaikan = findViewById(R.id.rb_perbaikan); // Hubungkan ID baru
        btnSimpan = findViewById(R.id.btn_simpan_edit);

        // 2. Tangkap Data dari Adapter
        idKamar = getIntent().getStringExtra("ID_KAMAR");
        tipeKamar = getIntent().getStringExtra("TIPE_KAMAR");
        statusLama = getIntent().getStringExtra("STATUS_KAMAR");

        // 3. Tampilkan Data Awal (Cek 3 Kondisi)
        tvTipe.setText("Tipe Kamar: " + tipeKamar);

        if (statusLama != null) {
            if (statusLama.equalsIgnoreCase("terisi")) {
                rbTerisi.setChecked(true);
            } else if (statusLama.equalsIgnoreCase("perbaikan")) {
                rbPerbaikan.setChecked(true);
            } else {
                rbKosong.setChecked(true); // Default jika kosong
            }
        }

        // 4. Aksi Tombol Simpan
        btnSimpan.setOnClickListener(v -> {
            String statusBaru = "kosong"; // Default awal

            // Cek tombol mana yang sedang diklik
            if (rbTerisi.isChecked()) {
                statusBaru = "terisi";
            } else if (rbPerbaikan.isChecked()) {
                statusBaru = "perbaikan";
            }

            updateStatusKeServer(idKamar, statusBaru);
        });
    }

    private void updateStatusKeServer(String id, String status) {
        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = "Bearer " + pref.getString("token", "");

        ApiClient.getClient().updateStatusKamar(token, id, status).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditKamarActivity.this, "Status berhasil diubah!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditKamarActivity.this, "Gagal mengubah status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditKamarActivity.this, "Koneksi Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}