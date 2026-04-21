package com.delfy.kost;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class KomplainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komplain);

        ImageView btnBack = findViewById(R.id.btn_back);
        LinearLayout btnKirim = findViewById(R.id.btn_kirim_komplain);

        btnBack.setOnClickListener(v -> finish());
        btnKirim.setOnClickListener(v -> {
            startActivity(new Intent(this, KomplainBerhasilActivity.class));
            finish();
        });
    }
}