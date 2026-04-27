package com.delfy.kost;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.ApiService;
import com.delfy.kost.api.LoginResponse;
import com.delfy.kost.api.RegisterRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarActivity extends AppCompatActivity {

    private EditText etNama, etEmail, etNoHp, etPassword;
    private LinearLayout btnDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        // 1. Hubungkan ID XML dengan Variabel Java (SUDAH DISESUAIKAN DENGAN XML)
        etNama = findViewById(R.id.et_daftar_nama);
        etEmail = findViewById(R.id.et_daftar_email);
        etNoHp = findViewById(R.id.et_daftar_wa);
        etPassword = findViewById(R.id.et_daftar_password);
        btnDaftar = findViewById(R.id.btn_daftar_akun);

        // 2. Aksi saat tombol Daftar diklik
        btnDaftar.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String noHp = etNoHp.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validasi Input Kosong
            if (nama.isEmpty() || email.isEmpty() || noHp.isEmpty() || password.isEmpty()) {
                Toast.makeText(DaftarActivity.this, "Semua data wajib diisi!", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(DaftarActivity.this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show();
            } else {
                prosesPendaftaran(nama, email, noHp, password);
            }
        });
    }

    private void prosesPendaftaran(String nama, String email, String noHp, String password) {
        Toast.makeText(this, "Sedang mendaftarkan akun...", Toast.LENGTH_SHORT).show();

        ApiService apiService = ApiClient.getClient();
        RegisterRequest requestData = new RegisterRequest(nama, email, noHp, password);

        apiService.registerPenyewa(requestData).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(DaftarActivity.this, "Pendaftaran Berhasil! Silakan Login.", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(DaftarActivity.this, "Gagal: " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // --- INI YANG KITA UBAH AGAR TIDAK ASAL TEBAK ---
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown Error";
                        android.util.Log.e("ERROR_DAFTAR", "Pesan Asli Laravel: " + errorBody);
                        Toast.makeText(DaftarActivity.this, "Gagal! Cek pesan merah di Logcat", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(DaftarActivity.this, "Koneksi Error: Pastikan server menyala!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}