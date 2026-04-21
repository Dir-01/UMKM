package com.delfy.kost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log; // Tambahan untuk Log

import androidx.appcompat.app.AppCompatActivity;

import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.ApiService;
import com.delfy.kost.api.LoginRequest;
import com.delfy.kost.api.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPemilikActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private LinearLayout btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pemilik);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btn_login_pemilik);

        TextView tvLupaPassword = findViewById(R.id.tv_lupa_password);
        tvLupaPassword.setPaintFlags(tvLupaPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginPemilikActivity.this, "Username dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            } else {
                prosesLogin(username, password);
            }
        });
    }

    private void prosesLogin(String username, String password) {
        Toast.makeText(this, "Mencoba masuk...", Toast.LENGTH_SHORT).show();

        ApiService apiService = ApiClient.getClient();
        LoginRequest request = new LoginRequest(username, password);

        apiService.loginPemilik(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    // --- BAGIAN INI YANG TADI TERPOTONG ---
                    if (loginResponse.isSuccess()) {

                        // Ambil token dari JSON response
                        String token = loginResponse.getData().getAccessToken();

                        // Pasang CCTV Logcat
                        Log.d("CCTV_LOGIN", "Token Berhasil Didapat: " + token);

                        // Simpan ke SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", token);
                        editor.apply();

                        Toast.makeText(LoginPemilikActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();

                        // Pindah halaman
                        startActivity(new Intent(LoginPemilikActivity.this, DashboardPemilikActivity.class));
                        finish();

                    } else {
                        Toast.makeText(LoginPemilikActivity.this, "Gagal: " + loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginPemilikActivity.this, "Username atau Password salah!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginPemilikActivity.this, "Koneksi Gagal! Pastikan server menyala.", Toast.LENGTH_LONG).show();
            }
        });
    }
}