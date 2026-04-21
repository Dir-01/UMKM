package com.delfy.kost;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PemesananTipe2Activity extends AppCompatActivity {

    private TextView chip1, chip3, chip6, chip12;
    private TextView tvTanggalMulai;
    private LinearLayout btnBankTransfer, btnEwallet, btnLanjutkan;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan_tipe2);

        // Inisialisasi
        chip1 = findViewById(R.id.chip_1);
        chip3 = findViewById(R.id.chip_3);
        chip6 = findViewById(R.id.chip_6);
        chip12 = findViewById(R.id.chip_12);
        tvTanggalMulai = findViewById(R.id.tv_tanggal_mulai);
        btnBankTransfer = findViewById(R.id.btn_bank_transfer);
        btnEwallet = findViewById(R.id.btn_ewallet);
        btnLanjutkan = findViewById(R.id.btn_lanjutkan);
        btnBack = findViewById(R.id.btn_back);

        // Aksi tombol kembali
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kembali ke halaman detail kamar
            }
        });

        // Logika untuk memilih durasi
        View.OnClickListener chipListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView clickedChip = (TextView) v;
                Toast.makeText(PemesananTipe2Activity.this, "Durasi: " + clickedChip.getText().toString() + " Bulan", Toast.LENGTH_SHORT).show();
            }
        };

        chip1.setOnClickListener(chipListener);
        chip3.setOnClickListener(chipListener);
        chip6.setOnClickListener(chipListener);
        chip12.setOnClickListener(chipListener);

        tvTanggalMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PemesananTipe2Activity.this, "Pilih Tanggal Mulai", Toast.LENGTH_SHORT).show();
            }
        });

        btnBankTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PemesananTipe2Activity.this, "Bank Transfer Dipilih", Toast.LENGTH_SHORT).show();
            }
        });

        btnEwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PemesananTipe2Activity.this, "E-Wallet Dipilih", Toast.LENGTH_SHORT).show();
            }
        });

        btnLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PemesananTipe2Activity.this, "Melanjutkan pembayaran Kamar Tipe 2...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}