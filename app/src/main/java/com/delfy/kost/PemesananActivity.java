package com.delfy.kost;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class PemesananActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        LinearLayout btnBank = findViewById(R.id.btn_bank_transfer);
        LinearLayout btnEwallet = findViewById(R.id.btn_ewallet);

        btnBank.setOnClickListener(v -> startActivity(new Intent(this, MetodePembayaranActivity.class)));
        btnEwallet.setOnClickListener(v -> startActivity(new Intent(this, PembayaranEwalletActivity.class)));
    }
}