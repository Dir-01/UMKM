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
        totalBayarInt = intent.getIntExtra("TOTAL_BAYAR", 0);
        durasiInt = intent.getIntExtra("DURASI", 1);
        tanggalMulaiStr = intent.getStringExtra("TANGGAL_MULAI");
        metodePembayaranStr = intent.getStringExtra("METODE_PEMBAYARAN");
        tipeKamarStr = intent.getStringExtra("TIPE_KAMAR");
        idKamarStr = intent.getStringExtra("ID_KAMAR"); // Tangkap ID asli (misal 11 atau 13)

        // 3. Tampilkan Data & Hitung Tanggal Selesai Otomatis
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        tvTotalBayarAkhir.setText(formatRupiah.format(totalBayarInt));
        tvDurasiSewaAkhir.setText("Durasi sewa: " + durasiInt + " Bulan");

        // Panggil fungsi hitung tanggal agar UI langsung terupdate
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

            // Pastikan ID Kamar tidak kosong (Gunakan ID Kamar asli dari Navicat)
            String finalIdKamar = (idKamarStr != null) ? idKamarStr : "11";

            // Convert tanggal ke format Laravel YYYY-MM-DD
            SimpleDateFormat inputSdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            SimpleDateFormat outputSdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String tglLaravel = outputSdf.format(inputSdf.parse(tanggalMulaiStr));

            // Siapkan RequestBody
            RequestBody rbPenyewa = RequestBody.create(MediaType.parse("text/plain"), idPenyewa);
            RequestBody rbKamar = RequestBody.create(MediaType.parse("text/plain"), finalIdKamar);
            RequestBody rbTipe = RequestBody.create(MediaType.parse("text/plain"), tipeKamarStr);
            RequestBody rbTgl = RequestBody.create(MediaType.parse("text/plain"), tglLaravel);
            RequestBody rbDurasi = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(durasiInt));
            RequestBody rbTotal = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(totalBayarInt));
            RequestBody rbMetode = RequestBody.create(MediaType.parse("text/plain"), metodePembayaranStr);

            File file = uriToFile(fotoBuktiUri);
            RequestBody rbFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part partFoto = MultipartBody.Part.createFormData("bukti_bayar", file.getName(), rbFile);

            ApiService apiService = ApiClient.getClient();
            apiService.kirimPembayaran("Bearer " + token, rbPenyewa, rbKamar, rbTipe, rbTgl, rbDurasi, rbTotal, rbMetode, partFoto)
                    .enqueue(new Callback<TransaksiResponse>() {
                        @Override
                        public void onResponse(Call<TransaksiResponse> call, Response<TransaksiResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(PembayaranActivity.this, "Pembayaran Berhasil!", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Log.e("Delfy_Debug", "Error: " + response.code());
                                Toast.makeText(PembayaranActivity.this, "Gagal simpan (Error " + response.code() + ")", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<TransaksiResponse> call, Throwable t) {
                            Toast.makeText(PembayaranActivity.this, "Cek Koneksi Server!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
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