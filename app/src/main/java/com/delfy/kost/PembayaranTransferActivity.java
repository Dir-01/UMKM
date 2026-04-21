package com.delfy.kost;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PembayaranTransferActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout btnDana, btnOvo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_transfer);

        // Inisialisasi komponen
        btnBack = findViewById(R.id.btn_back);
        btnDana = findViewById(R.id.btn_pilih_dana);
        btnOvo = findViewById(R.id.btn_pilih_ovo);

        // Aksi tombol kembali
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kembali ke halaman pemesanan sebelumnya
            }
        });

        // Aksi pilih DANA
        btnDana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PembayaranTransferActivity.this, "Memproses DANA untuk Rp. 700.000", Toast.LENGTH_SHORT).show();
            }
        });

        // Aksi pilih OVO
        btnOvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PembayaranTransferActivity.this, "Memproses OVO untuk Rp. 700.000", Toast.LENGTH_SHORT).show();
            }
        });
    }
}