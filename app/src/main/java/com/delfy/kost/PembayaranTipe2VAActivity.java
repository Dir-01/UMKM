package com.delfy.kost;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PembayaranTipe2VAActivity extends AppCompatActivity {

    private LinearLayout btnBankBca, btnBankBni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_tipe2_va);

        // Inisialisasi komponen
        btnBankBca = findViewById(R.id.btn_bank_bca);
        btnBankBni = findViewById(R.id.btn_bank_bni);

        // Aksi pilih Bank BCA
        btnBankBca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PembayaranTipe2VAActivity.this, "Memproses Virtual Account BCA untuk Rp. 800.000", Toast.LENGTH_SHORT).show();
            }
        });

        // Aksi pilih Bank BNI
        btnBankBni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PembayaranTipe2VAActivity.this, "Memproses Virtual Account BNI untuk Rp. 800.000", Toast.LENGTH_SHORT).show();
            }
        });
    }
}