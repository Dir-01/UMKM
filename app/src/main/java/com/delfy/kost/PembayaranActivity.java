package com.delfy.kost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.delfy.kost.api.ApiClient;
import com.delfy.kost.api.ApiService;
import com.delfy.kost.api.TransaksiResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PembayaranActivity extends AppCompatActivity {

    private ImageView btnBack, ivPreviewBukti;
    private TextView tvTotalBayarAkhir, tvDurasiSewaAkhir, tvTanggalSelesai;
    private LinearLayout btnUploadFoto, btnSelesai;
    private Uri fotoBuktiUri = null;

    private String tanggalMulaiStr, tipeKamarStr, metodePembayaranStr, idKamarStr;
    private int totalBayarInt = 0, durasiInt = 1;

    private final ActivityResultLauncher<Intent> galeriLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    fotoBuktiUri = result.getData().getData();
                    ivPreviewBukti.setImageURI(fotoBuktiUri);
                    ivPreviewBukti.setVisibility(View.VISIBLE);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        // 1. Inisialisasi UI
        btnBack = findViewById(R.id.btn_back_pembayaran);
        tvTotalBayarAkhir = findViewById(R.id.tv_total_bayar_akhir);
        tvDurasiSewaAkhir = findViewById(R.id.tv_durasi_sewa_akhir);
        tvTanggalSelesai = findViewById(R.id.tv_tanggal_selesai);
        btnUploadFoto = findViewById(R.id.btn_upload_foto);
        ivPreviewBukti = findViewById(R.id.iv_preview_bukti);
        btnSelesai = findViewById(R.id.btn_selesai_pembayaran);

        // 2. Tangkap Data dari Intent
        Intent intent = getIntent();

        // Menangkap durasi (Coba sebagai Int, kalau gagal coba sebagai String)
        durasiInt = intent.getIntExtra("DURASI", 0);
        if (durasiInt == 0) {
            String durStr = intent.getStringExtra("DURASI");
            if (durStr != null) {
                try { durasiInt = Integer.parseInt(durStr.replaceAll("[^\\d]", "")); } catch (Exception e) { durasiInt = 1; }
            } else { durasiInt = 1; }
        }

        // Menangkap total bayar (Coba sebagai Int, kalau gagal coba sebagai String)
        totalBayarInt = intent.getIntExtra("TOTAL_BAYAR", 0);
        if (totalBayarInt == 0) {
            String totalStr = intent.getStringExtra("TOTAL_BAYAR");
            // Fallback key
            if (totalStr == null) totalStr = intent.getStringExtra("TOTAL_HARGA");
            if (totalStr == null) totalStr = intent.getStringExtra("TOTAL");

            if (totalStr != null && !totalStr.isEmpty()) {
                try {
                    String cleanString = totalStr.replaceAll("[^\\d]", "");
                    totalBayarInt = Integer.parseInt(cleanString);
                } catch (Exception e) {
                    totalBayarInt = 0;
                }
            }
        }

        tanggalMulaiStr = intent.getStringExtra("TANGGAL_MULAI");
        metodePembayaranStr = intent.getStringExtra("METODE_PEMBAYARAN");
        tipeKamarStr = intent.getStringExtra("TIPE_KAMAR");
        idKamarStr = intent.getStringExtra("ID_KAMAR");

        // 3. Tampilkan Data & Hitung Tanggal Selesai Otomatis
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        tvTotalBayarAkhir.setText(formatRupiah.format(totalBayarInt));
        tvDurasiSewaAkhir.setText("Durasi sewa: " + durasiInt + " Bulan");

        hitungTanggalSelesai(tanggalMulaiStr, durasiInt);

        // 4. Listener
        btnBack.setOnClickListener(v -> finish());
        btnUploadFoto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galeriLauncher.launch(i);
        });

        btnSelesai.setOnClickListener(v -> {
            if (fotoBuktiUri == null) {
                Toast.makeText(this, "Wajib upload bukti pembayaran!", Toast.LENGTH_SHORT).show();
            } else if (totalBayarInt == 0) {
                Toast.makeText(this, "Harga tidak valid, harap ulangi pesanan", Toast.LENGTH_SHORT).show();
            } else {
                uploadDataKeServer();
            }
        });
    }

    private void hitungTanggalSelesai(String tglMulai, int durasi) {
        if (tglMulai == null || tglMulai.isEmpty()) return;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date = sdf.parse(tglMulai);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, durasi);
            tvTanggalSelesai.setText(sdf.format(cal.getTime()));
        } catch (Exception e) {
            tvTanggalSelesai.setText("-");
        }
    }

    private void uploadDataKeServer() {
        try {
            SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
            String token = pref.getString("token", "");
            String idPenyewa = pref.getString("id_penyewa", "");

            if (token.isEmpty()) {
                Toast.makeText(this, "Sesi Berakhir, silakan login ulang", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- PERBAIKAN: Jangan dipaksa ke "12" ---
            if (idKamarStr == null || idKamarStr.isEmpty()) {
                Toast.makeText(this, "ID Kamar tidak terbaca dari halaman sebelumnya!", Toast.LENGTH_LONG).show();
                return;
            }

            // Format Tanggal
            String tglLaravel;
            try {
                SimpleDateFormat inputSdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                SimpleDateFormat outputSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                tglLaravel = outputSdf.format(inputSdf.parse(tanggalMulaiStr));
            } catch (Exception e) {
                tglLaravel = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            }

            // RequestBody
            RequestBody rbPenyewa = RequestBody.create(MediaType.parse("text/plain"), idPenyewa);
            RequestBody rbKamar = RequestBody.create(MediaType.parse("text/plain"), idKamarStr);
            RequestBody rbTipe = RequestBody.create(MediaType.parse("text/plain"), tipeKamarStr != null ? tipeKamarStr : "Tipe Kamar");
            RequestBody rbTgl = RequestBody.create(MediaType.parse("text/plain"), tglLaravel);
            RequestBody rbDurasi = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(durasiInt));
            RequestBody rbTotal = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(totalBayarInt));
            RequestBody rbMetode = RequestBody.create(MediaType.parse("text/plain"), metodePembayaranStr != null ? metodePembayaranStr : "Transfer");

            File file = uriToFile(fotoBuktiUri);
            RequestBody rbFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part partFoto = MultipartBody.Part.createFormData("bukti_bayar", file.getName(), rbFile);

            ApiClient.getClient().kirimPembayaran("Bearer " + token, rbPenyewa, rbKamar, rbTipe, rbTgl, rbDurasi, rbTotal, rbMetode, partFoto)
                    .enqueue(new Callback<TransaksiResponse>() {
                        @Override
                        public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(PembayaranActivity.this, "Pembayaran Berhasil!", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                // Blok untuk menangani error dari Laravel (404, 422, 500, dll)
                                try {
                                    // Ambil isi error hanya SATU KALI
                                    String errorBody = response.errorBody().string();
                                    Log.e("Delfy_Error", "Pesan dari Server: " + errorBody);

                                    // Tampilkan pesan asli dari Laravel agar kamu tahu apa yang salah
                                    // Jika error 404, biasanya id_kamar tidak ditemukan di database
                                    Toast.makeText(PembayaranActivity.this, "Gagal: " + errorBody, Toast.LENGTH_LONG).show();

                                } catch (Exception e) {
                                    Log.e("Delfy_Error", "Gagal membaca pesan error", e);
                                    Toast.makeText(PembayaranActivity.this, "Gagal simpan data (Kode: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<TransaksiResponse> call, Throwable t) {
                            Toast.makeText(PembayaranActivity.this, "Koneksi Gagal!", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private File uriToFile(Uri uri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File file = new File(getCacheDir(), "bukti_temp.jpg");
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) outputStream.write(buffer, 0, read);
        outputStream.flush();
        return file;
    }
}