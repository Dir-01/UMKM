package com.delfy.kost;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class KomplainBerhasilActivity extends AppCompatActivity {

    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komplain_berhasil);

        // Inisialisasi View
        btnBack = findViewById(R.id.btn_back);

        // Aksi Tombol Kembali
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke halaman sebelumnya (atau bisa diatur agar kembali ke Homepage)
                finish();
            }
        });
    }
}