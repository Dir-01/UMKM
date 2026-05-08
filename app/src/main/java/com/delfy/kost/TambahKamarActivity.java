package com.delfy.kost;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.ApiService;
import com.delfy.kost.api.KamarResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahKamarActivity extends AppCompatActivity {

    private EditText etNamaKamar, etHargaKamar;
    private Spinner spinnerStatus, spinnerTipeKamar;
    private LinearLayout btnSimpan;
    private ImageView btnBack, ivPreview;
    private Button btnPilihFoto;
    private Uri uriFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kamar);

        // 1. Hubungkan ID
        etNamaKamar = findViewById(R.id.et_nama_kamar);
        etHargaKamar = findViewById(R.id.et_harga_kamar);
        spinnerStatus = findViewById(R.id.spinner_status_kamar);
        spinnerTipeKamar = findViewById(R.id.spinner_tipe_kamar);
        btnSimpan = findViewById(R.id.btn_simpan_kamar);
        btnBack = findViewById(R.id.btn_back);
        ivPreview = findViewById(R.id.iv_preview_kamar);
        btnPilihFoto = findViewById(R.id.btn_pilih_foto);

        // 2. Cek Izin Storage saat aplikasi dibuka
        mintaIzinStorage();

        // 3. Setup Spinner
        setupSpinners();

        // 4. Klik Pilih Foto
        btnPilihFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        });

        // 5. Klik Simpan
        btnSimpan.setOnClickListener(v -> {
            validasiDanSimpan();
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void mintaIzinStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 1);
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    private void setupSpinners() {
        String[] pilihanStatus = {"Kosong", "Terisi", "Perbaikan"};
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pilihanStatus);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapterStatus);

        String[] pilihanTipe = {"Kamar Tipe 1", "Kamar Tipe 2"};
        ArrayAdapter<String> adapterTipe = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pilihanTipe);
        adapterTipe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipeKamar.setAdapter(adapterTipe);
    }

    private void validasiDanSimpan() {
        String nama = etNamaKamar.getText().toString().trim();
        String hargaStr = etHargaKamar.getText().toString().trim();
        String status = spinnerStatus.getSelectedItem().toString().toLowerCase();
        String tipe = spinnerTipeKamar.getSelectedItem().toString();

        if (nama.isEmpty() || hargaStr.isEmpty() || uriFoto == null) {
            Toast.makeText(this, "Semua data & foto wajib diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        prosesSimpanKamar(nama, tipe, Integer.parseInt(hargaStr), status);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            uriFoto = data.getData();
            ivPreview.setImageURI(uriFoto);
        }
    }

    // HELPER: Menyalin file ke cache aplikasi agar tidak Error Permission Denied
    private File getFileFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File tempFile = new File(getCacheDir(), "temp_kamar.jpg");
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return tempFile;
        } catch (Exception e) {
            Log.e("Delfy_Debug", "Gagal salin file: " + e.getMessage());
            return null;
        }
    }

    private void prosesSimpanKamar(String noKamar, String tipeKamar, int harga, String status) {
        Toast.makeText(this, "Menyimpan ke server...", Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String idPemilik = sharedPreferences.getString("id_pemilik", "PM001");

        File fileGambar = getFileFromUri(uriFoto);
        if (fileGambar == null) return;

        // Persiapkan Part Multipart
        RequestBody rbNo = RequestBody.create(MultipartBody.FORM, noKamar);
        RequestBody rbTipe = RequestBody.create(MultipartBody.FORM, tipeKamar);
        RequestBody rbHarga = RequestBody.create(MultipartBody.FORM, String.valueOf(harga));
        RequestBody rbStatus = RequestBody.create(MultipartBody.FORM, status);
        RequestBody rbPemilik = RequestBody.create(MultipartBody.FORM, idPemilik);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), fileGambar);
        MultipartBody.Part partFoto = MultipartBody.Part.createFormData("foto_kamar", fileGambar.getName(), requestFile);

        ApiService apiService = ApiClient.getClient();
        apiService.tambahKamar("Bearer " + token, rbNo, rbTipe, rbHarga, rbStatus, rbPemilik, partFoto)
                .enqueue(new Callback<KamarResponse>() {
                    @Override
                    public void onResponse(Call<KamarResponse> call, Response<KamarResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(TambahKamarActivity.this, "Berhasil!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.e("Delfy_Debug", "Server Error: " + response.code());
                            Toast.makeText(TambahKamarActivity.this, "Gagal Simpan!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<KamarResponse> call, Throwable t) {
                        Log.e("Delfy_Debug", "Koneksi Error: " + t.getMessage());
                    }
                });
    }
}