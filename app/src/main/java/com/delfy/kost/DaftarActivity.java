package com.delfy.kost;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.ApiService;
import com.delfy.kost.api.LoginResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarActivity extends AppCompatActivity {

    private EditText etNama, etEmail, etNoHp, etPassword;
    private LinearLayout btnDaftar, layoutPlaceholderKtp;
    private FrameLayout btnUploadKtp;
    private ImageView ivPreviewKtp;

    private Uri fotoKtpUri = null; // Menyimpan alamat foto yang dipilih

    // Launcher untuk membuka Galeri
    private final ActivityResultLauncher<Intent> galeriLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    fotoKtpUri = result.getData().getData();

                    // Tampilkan gambar dan sembunyikan tulisan "Ketuk untuk upload"
                    ivPreviewKtp.setImageURI(fotoKtpUri);
                    ivPreviewKtp.setVisibility(View.VISIBLE);
                    layoutPlaceholderKtp.setVisibility(View.GONE);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        // 1. Inisialisasi View
        etNama = findViewById(R.id.et_daftar_nama);
        etEmail = findViewById(R.id.et_daftar_email);
        etNoHp = findViewById(R.id.et_daftar_wa);
        etPassword = findViewById(R.id.et_daftar_password);
        btnDaftar = findViewById(R.id.btn_daftar_akun);

        btnUploadKtp = findViewById(R.id.btn_upload_ktp);
        ivPreviewKtp = findViewById(R.id.iv_preview_ktp);
        layoutPlaceholderKtp = findViewById(R.id.layout_placeholder_ktp);

        // 2. Aksi Klik Upload KTP (Buka Galeri)
        btnUploadKtp.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galeriLauncher.launch(i);
        });

        // 3. Aksi Klik Daftar
        btnDaftar.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String noHp = etNoHp.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validasi Input Kosong
            if (nama.isEmpty() || email.isEmpty() || noHp.isEmpty() || password.isEmpty()) {
                Toast.makeText(DaftarActivity.this, "Semua data teks wajib diisi!", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(DaftarActivity.this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show();
            } else if (fotoKtpUri == null) {
                // Validasi agar user tidak lupa upload KTP
                Toast.makeText(DaftarActivity.this, "Foto KTP wajib diupload!", Toast.LENGTH_SHORT).show();
            } else {
                prosesPendaftaran(nama, email, noHp, password);
            }
        });
    }

    private void prosesPendaftaran(String nama, String email, String noHp, String password) {
        Toast.makeText(this, "Sedang mendaftarkan akun...", Toast.LENGTH_SHORT).show();

        try {
            // 1. Siapkan data TEKS agar menjadi RequestBody
            RequestBody rbNama = RequestBody.create(MediaType.parse("text/plain"), nama);
            RequestBody rbEmail = RequestBody.create(MediaType.parse("text/plain"), email);
            RequestBody rbNoHp = RequestBody.create(MediaType.parse("text/plain"), noHp);
            RequestBody rbPassword = RequestBody.create(MediaType.parse("text/plain"), password);

            // 2. Siapkan data FILE (Foto KTP) agar menjadi MultipartBody.Part
            File fileKtp = uriToFile(fotoKtpUri);
            RequestBody rbFile = RequestBody.create(MediaType.parse("image/*"), fileKtp);
            MultipartBody.Part partFotoKtp = MultipartBody.Part.createFormData("foto_ktp", fileKtp.getName(), rbFile);

            // 3. Panggil API
            ApiService apiService = ApiClient.getClient();
            apiService.registerPenyewaWithKtp(rbNama, rbEmail, rbNoHp, rbPassword, partFotoKtp)
                    .enqueue(new Callback<LoginResponse>() {
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
                                try {
                                    String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown Error";
                                    Log.e("ERROR_DAFTAR", "Pesan Asli Laravel: " + errorBody);
                                    Toast.makeText(DaftarActivity.this, "Gagal simpan ke database. Cek Logcat!", Toast.LENGTH_LONG).show();
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

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal memproses gambar", Toast.LENGTH_SHORT).show();
        }
    }

    // Fungsi Pembantu: Mengubah URI (Alamat Galeri) menjadi File fisik sementara
    private File uriToFile(Uri uri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File file = new File(getCacheDir(), "ktp_upload.jpg");
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();
        return file;
    }
}