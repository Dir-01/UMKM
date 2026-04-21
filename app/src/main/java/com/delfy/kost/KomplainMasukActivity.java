package com.delfy.kost;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class KomplainMasukActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komplain_masuk);

        LinearLayout btnUpdateStatus = findViewById(R.id.btn_update_status_1);
        btnUpdateStatus.setOnClickListener(v -> startActivity(new Intent(this, UpdateStatusActivity.class)));
    }
}