package com.delfy.kost;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.ApiService;
import com.delfy.kost.api.KamarRequest;
import com.delfy.kost.api.KamarResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahKamarActivity extends AppCompatActivity {

    private EditText etNamaKamar, etHargaKamar;
    private Spinner spinnerStatus;
    private LinearLayout btnSimpan;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kamar);

        // 1. Hubungkan ID
        etNamaKamar = findViewById(R.id.et_nama_kamar);
        etHargaKamar = findViewById(R.id.et_harga_kamar);
        spinnerStatus = findViewById(R.id.spinner_status_kamar);
        btnSimpan = findViewById(R.id.btn_simpan_kamar);
        btnBack = findViewById(R.id.btn_back);

        // 2. Isi pilihan Spinner (Dropdown)
        String[] pilihanStatus = {"Kosong", "Terisi", "Perbaikan"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pilihanStatus);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapterSpinner);

        // 3. Tombol Kembali
        btnBack.setOnClickListener(v -> finish()); // Menutup halaman dan kembali ke Kelola Kamar

        // 4. Tombol Simpan
        btnSimpan.setOnClickListener(v -> {
            String namaKamar = etNamaKamar.getText().toString().trim();
            String hargaKamarString = etHargaKamar.getText().toString().trim();
            String statusKamar = spinnerStatus.getSelectedItem().toString().toLowerCase();

            // Validasi isian kosong
            if (namaKamar.isEmpty() || hargaKamarString.isEmpty()) {
                Toast.makeText(TambahKamarActivity.this, "Semua data harus diisi!", Toast.LENGTH_SHORT).show();
            } else {
                int hargaKamar = Integer.parseInt(hargaKamarString);
                prosesSimpanKamar(namaKamar, hargaKamar, statusKamar);
            }
        });
    }

    // ... (kode atasnya sama) ...

    private void prosesSimpanKamar(String noKamar, int harga, String status) {
        Toast.makeText(this, "Sedang menyimpan...", Toast.LENGTH_SHORT).show();

        // --- PASTIKAN MENCARI DI BRANKAS YANG SAMA ---
        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            Toast.makeText(this, "Sesi habis, silakan login ulang", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient();
        String tokenBearer = "Bearer " + token;

// ... (lanjutkan proses simpan seperti biasa) ...

        // Bungkus data ke dalam Request
        KamarRequest requestData = new KamarRequest(noKamar, harga, status);

        // Tembak URL /api/kamar
        apiService.tambahKamar(tokenBearer, requestData).enqueue(new Callback<KamarResponse>() {
            @Override
            public void onResponse(Call<KamarResponse> call, Response<KamarResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TambahKamarActivity.this, "Kamar Berhasil Ditambahkan!", Toast.LENGTH_LONG).show();
                    // Jika sukses, tutup halaman ini otomatis agar kembali ke daftar kamar
                    finish();
                } else {
                    Toast.makeText(TambahKamarActivity.this, "Gagal menambahkan kamar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KamarResponse> call, Throwable t) {
                // 1. Tampilkan di Logcat dengan warna merah
                android.util.Log.e("ERROR_ASLI_API", "Penyebab: " + t.getMessage());

                // 2. Tampilkan di Layar HP (Toast) agar kamu bisa langsung baca
                Toast.makeText(TambahKamarActivity.this, "Error Asli: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}