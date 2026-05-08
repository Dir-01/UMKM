package com.delfy.kost.adapter;

import com.delfy.kost.EditKamarActivity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;
import com.delfy.kost.R;
import com.delfy.kost.api.KamarResponse;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class KamarAdapter extends RecyclerView.Adapter<KamarAdapter.KamarViewHolder> {

    private List<KamarResponse.KamarModel> listKamar;

    public KamarAdapter(List<KamarResponse.KamarModel> listKamar) {
        this.listKamar = listKamar;
    }

    @NonNull
    @Override
    public KamarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kamar, parent, false);
        return new KamarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KamarViewHolder holder, int position) {
        KamarResponse.KamarModel kamar = listKamar.get(position);

        // 1. Tampilkan Data Teks
        holder.tvTipeKamar.setText(kamar.getTipeKamar());
        holder.tvStatusKamar.setText(kamar.getStatus());

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        holder.tvHargaKamar.setText(formatRupiah.format(kamar.getHarga()).replace("Rp", "Rp "));

        // 2. Logika Badge Status
        if (kamar.getStatus().equalsIgnoreCase("kosong")) {
            holder.tvStatusKamar.setBackgroundResource(R.drawable.bg_badge_grey);
        } else if (kamar.getStatus().equalsIgnoreCase("terisi")) {
            holder.tvStatusKamar.setBackgroundResource(R.drawable.bg_badge_green);
        } else {
            holder.tvStatusKamar.setBackgroundResource(R.drawable.bg_badge_yellow);
        }

        // 3. LOGIKA LOAD GAMBAR (Ini yang tadi hilang)
        String urlGambar = "http://10.0.2.2:8000/storage/kamar/" + kamar.getFotoKamar();
        // Ganti IP-nya agar foto kamar tidak blank
        //String urlGambar = "http://192.168.1.15:8000/storage/kamar/" + kamar.getFotoKamar();


        Glide.with(holder.itemView.getContext())
                .load(urlGambar)
                .signature(new ObjectKey(System.currentTimeMillis())) // Menghindari Cache agar foto selalu update
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.img_kamar_1)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.imgKamar);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditKamarActivity.class);
            intent.putExtra("ID_KAMAR", String.valueOf(kamar.getIdKamar()));
            intent.putExtra("TIPE_KAMAR", kamar.getTipeKamar());
            intent.putExtra("STATUS_KAMAR", kamar.getStatus());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listKamar != null ? listKamar.size() : 0;
    }

    public class KamarViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipeKamar, tvStatusKamar, tvHargaKamar;
        ImageView imgKamar; // Tambahkan variabel ImageView

        public KamarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipeKamar = itemView.findViewById(R.id.tvTipeKamar);
            tvStatusKamar = itemView.findViewById(R.id.tvStatusKamar);
            tvHargaKamar = itemView.findViewById(R.id.tvHargaKamar);
            imgKamar = itemView.findViewById(R.id.img_kamar); // Hubungkan dengan ID di XML
        }
    }
}