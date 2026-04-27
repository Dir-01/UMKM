package com.delfy.kost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class PemesananActivity extends AppCompatActivity {

    // Deklarasi Variabel UI
    private TextView tvDurasi1, tvDurasi3, tvDurasi6, tvDurasi12;
    private TextView tvTotalHarga;
    private EditText etTanggalMulai;
    private LinearLayout btnLanjutkan;
    private ImageView btnBack;

    // Bagian Opsi Pembayaran & Detailnya
    private LinearLayout opsiBank, opsiEwallet;
    private LinearLayout layoutDetailBank, layoutDetailEwallet;
    private ImageView icArrowBank, icArrowEwallet;

    // Deklarasi Variabel Data (Harga Tipe 1: 700.000)
    private int durasiBulan = 1;
    private int hargaKamarPerBulan = 700000;
    private String metodePembayaran = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        // 1. Hubungkan ID
        tvDurasi1 = findViewById(R.id.tv_durasi_1);
        tvDurasi3 = findViewById(R.id.tv_durasi_3);
        tvDurasi6 = findViewById(R.id.tv_durasi_6);
        tvDurasi12 = findViewById(R.id.tv_durasi_12);
        tvTotalHarga = findViewById(R.id.tv_total_harga);
        etTanggalMulai = findViewById(R.id.et_tanggal_mulai);
        btnLanjutkan = findViewById(R.id.btn_lanjutkan_pemesanan);
        btnBack = findViewById(R.id.btn_back_pemesanan);

        // Hubungkan ID Pembayaran
        opsiBank = findViewById(R.id.opsi_bank_transfer);
        opsiEwallet = findViewById(R.id.opsi_ewallet);
        layoutDetailBank = findViewById(R.id.layout_detail_bank);
        layoutDetailEwallet = findViewById(R.id.layout_detail_ewallet);
        icArrowBank = findViewById(R.id.ic_arrow_bank);
        icArrowEwallet = findViewById(R.id.ic_arrow_ewallet);

        // Set harga awal saat aplikasi dibuka
        updateTotalHarga();

        // 2. Aksi Tombol Kembali
        btnBack.setOnClickListener(v -> finish());

        // 3. Aksi Pilihan Durasi
        tvDurasi1.setOnClickListener(v -> pilihDurasi(1));
        tvDurasi3.setOnClickListener(v -> pilihDurasi(3));
        tvDurasi6.setOnClickListener(v -> pilihDurasi(6));
        tvDurasi12.setOnClickListener(v -> pilihDurasi(12));

        // 4. Aksi Pilih Metode Pembayaran
        opsiBank.setOnClickListener(v -> {
            metodePembayaran = "Transfer Bank"; // Disesuaikan formatnya

            layoutDetailBank.setVisibility(View.VISIBLE);
            layoutDetailEwallet.setVisibility(View.GONE);
            icArrowBank.setRotation(90);
            icArrowEwallet.setRotation(0);
        });

        opsiEwallet.setOnClickListener(v -> {
            metodePembayaran = "E-Wallet";

            layoutDetailEwallet.setVisibility(View.VISIBLE);
            layoutDetailBank.setVisibility(View.GONE);
            icArrowEwallet.setRotation(90);
            icArrowBank.setRotation(0);
        });

        // 5. Aksi Tombol Lanjutkan Pemesanan
        btnLanjutkan.setOnClickListener(v -> {
            String tanggalMulai = etTanggalMulai.getText().toString().trim();

            if (tanggalMulai.isEmpty()) {
                Toast.makeText(this, "Tanggal Mulai harus diisi!", Toast.LENGTH_SHORT).show();
            } else if (metodePembayaran.isEmpty()) {
                Toast.makeText(this, "Pilih metode pembayaran terlebih dahulu!", Toast.LENGTH_SHORT).show();
            } else {
                int totalBayar = hargaKamarPerBulan * durasiBulan;

                // ARAHKAN KE HALAMAN PEMBAYARAN DAN BAWA SEMUA DATANYA
                Intent intent = new Intent(this, PembayaranActivity.class);

                // Ubah semua angka jadi String untuk mempermudah upload ke Laravel
                intent.putExtra("TOTAL_BAYAR", String.valueOf(totalBayar));
                intent.putExtra("DURASI", String.valueOf(durasiBulan));
                intent.putExtra("TANGGAL_MULAI", tanggalMulai);

                // INI YANG PALING PENTING:
                intent.putExtra("METODE_PEMBAYARAN", metodePembayaran);

                // Bawa juga Tipe Kamar jika sebelumnya dari halaman Detail Kamar
                String tipeKamar = getIntent().getStringExtra("TIPE_KAMAR");
                if(tipeKamar != null) {
                    intent.putExtra("TIPE_KAMAR", tipeKamar);
                } else {
                    intent.putExtra("TIPE_KAMAR", "Kamar Tipe 1"); // Nilai default jika kosong
                }

                startActivity(intent);
            }
        });
    }

    private void pilihDurasi(int bulan) {
        durasiBulan = bulan;

        // Reset semua (Pastikan kamu punya drawable bg_durasi_unselected & bg_durasi_selected)
        tvDurasi1.setBackgroundResource(R.drawable.bg_durasi_unselected);
        tvDurasi3.setBackgroundResource(R.drawable.bg_durasi_unselected);
        tvDurasi6.setBackgroundResource(R.drawable.bg_durasi_unselected);
        tvDurasi12.setBackgroundResource(R.drawable.bg_durasi_unselected);

        // Set selected
        if (bulan == 1) tvDurasi1.setBackgroundResource(R.drawable.bg_durasi_selected);
        if (bulan == 3) tvDurasi3.setBackgroundResource(R.drawable.bg_durasi_selected);
        if (bulan == 6) tvDurasi6.setBackgroundResource(R.drawable.bg_durasi_selected);
        if (bulan == 12) tvDurasi12.setBackgroundResource(R.drawable.bg_durasi_selected);

        updateTotalHarga();
    }

    private void updateTotalHarga() {
        int total = hargaKamarPerBulan * durasiBulan;
        Locale localeID = new Locale("id", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        tvTotalHarga.setText(formatRupiah.format(total));
    }
}