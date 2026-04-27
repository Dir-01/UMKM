package com.delfy.kost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.ApiService;
import com.delfy.kost.api.LoginRequest;
import com.delfy.kost.api.LoginResponse;

import org.json.JSONObject;

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
                Toast.makeText(this, "Isi username dan password!", Toast.LENGTH_SHORT).show();
            } else {
                prosesLogin(username, password);
            }
        });
    }

    private void prosesLogin(String username, String password) {
        Toast.makeText(this, "Menghubungkan ke server...", Toast.LENGTH_SHORT).show();

        ApiService apiService = ApiClient.getClient();
        // Sekarang mengirim 'username', bukan 'email'
        LoginRequest request = new LoginRequest(username, password);

        apiService.loginPemilik(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        // Simpan Token
                        String token = response.body().getData().getAccessToken();
                        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                        pref.edit().putString("token", token).apply();

                        Toast.makeText(LoginPemilikActivity.this, "Selamat Datang, Pemilik!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginPemilikActivity.this, DashboardPemilikActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginPemilikActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Menangkap pesan error asli dari Laravel (Validator)
                    try {
                        String errorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorBody);
                        String pesan = jsonObject.optString("message", "Username/Password Salah");
                        Toast.makeText(LoginPemilikActivity.this, pesan, Toast.LENGTH_LONG).show();
                        Log.e("Delfy_Debug", "Error 422: " + errorBody);
                    } catch (Exception e) {
                        Toast.makeText(LoginPemilikActivity.this, "Gagal Login (Error " + response.code() + ")", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("Delfy_Debug", "Failure: " + t.getMessage());
                Toast.makeText(LoginPemilikActivity.this, "Cek koneksi internet/IP Server!", Toast.LENGTH_LONG).show();
            }
        });
    }
}