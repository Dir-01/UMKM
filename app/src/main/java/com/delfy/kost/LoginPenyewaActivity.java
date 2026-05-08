package com.delfy.kost;

import com.delfy.kost.api.LoginPenyewaRequest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.ApiService;
import com.delfy.kost.api.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPenyewaActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private LinearLayout btnLogin;
    private TextView tvLupaPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_penyewa);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login_penyewa);
        tvLupaPassword = findViewById(R.id.tv_lupa_password);

        btnLogin.setOnClickListener(v -> {
            String email = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password wajib diisi!", Toast.LENGTH_SHORT).show();
            } else {
                prosesLogin(email, password);
            }
        });

        tvLupaPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Fitur Lupa Password segera hadir", Toast.LENGTH_SHORT).show();
        });
    }

    private void prosesLogin(String email, String password) {
        Toast.makeText(this, "Sedang login penyewa...", Toast.LENGTH_SHORT).show();

        ApiService apiService = ApiClient.getClient();
        LoginPenyewaRequest request = new LoginPenyewaRequest(email, password);

        apiService.loginPenyewa(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                // 1. Cek apakah response sukses (kode 200-an)
                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse loginData = response.body();

                    // 2. Bongkar data sesuai struktur: data -> user -> nama
                    if (loginData.getData() != null && loginData.getData().getUser() != null) {

                        String token = loginData.getData().getAccessToken();
                        String idPenyewa = loginData.getData().getUser().getIdPenyewa();
                        String namaUser = loginData.getData().getUser().getNama();

                        // 3. SIMPAN KE SHAREDPREFERENCES
                        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", token);
                        editor.putString("id_penyewa", idPenyewa);
                        editor.putString("nama", namaUser); // Kunci agar Nama muncul di Profil/Home
                        editor.apply();

                        Toast.makeText(LoginPenyewaActivity.this, "Selamat Datang, " + namaUser, Toast.LENGTH_SHORT).show();

                        // 4. Pindah ke Homepage
                        startActivity(new Intent(LoginPenyewaActivity.this, HomepageActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginPenyewaActivity.this, "Data User tidak ditemukan!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Jika login gagal (Email/Password salah)
                    Toast.makeText(LoginPenyewaActivity.this, "Email atau Password salah!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LOGIN_ERROR", t.getMessage());
                Toast.makeText(LoginPenyewaActivity.this, "Koneksi Error! Cek Server.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}