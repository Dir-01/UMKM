package com.delfy.kost;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class HomepageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        LinearLayout cardKamar1 = findViewById(R.id.card_tipe_1);
        LinearLayout cardKamar2 = findViewById(R.id.card_tipe_2);
        LinearLayout navProfile = findViewById(R.id.nav_profile);

        cardKamar1.setOnClickListener(v -> startActivity(new Intent(this, KamarTipe1Activity.class)));
        cardKamar2.setOnClickListener(v -> startActivity(new Intent(this, KamarTipe2Activity.class)));
        navProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileUserActivity.class));
            finish(); // Pindah tab
        });
    }
}