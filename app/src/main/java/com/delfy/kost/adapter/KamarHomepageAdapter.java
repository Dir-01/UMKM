package com.delfy.kost.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.delfy.kost.KamarTipe1Activity;
import com.delfy.kost.KamarTipe2Activity;
import com.delfy.kost.R;
import com.delfy.kost.api.KamarResponse;
import java.util.List;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.ObjectKey;

public class KamarHomepageAdapter extends RecyclerView.Adapter<KamarHomepageAdapter.ViewHolder> {
    private Context context;
    private List<KamarResponse.KamarModel> listKamar;

    public KamarHomepageAdapter(Context context, List<KamarResponse.KamarModel> listKamar) {
        this.context = context;
        this.listKamar = listKamar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_kamar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KamarResponse.KamarModel kamar = listKamar.get(position);

        // 1. Tampilkan Data Teks
        holder.tvLabel.setText("Nomor Kamar: " + kamar.getNoKamar());
        holder.tvTipe.setText(kamar.getTipeKamar());
        holder.tvHarga.setText("Rp. " + kamar.getHarga() + "/Bulan");
        holder.tvStatus.setText(kamar.getStatus());

        // 2. Logika Navigasi (Klik)
        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            String tipe = kamar.getTipeKamar();

            // Menggunakan contains agar fleksibel jika teksnya "Kamar Tipe 1" atau "Tipe 1"
            if (tipe != null && tipe.toLowerCase().contains("tipe 1")) {
                intent = new Intent(context, KamarTipe1Activity.class);
            } else {
                intent = new Intent(context, KamarTipe2Activity.class);
            }

            intent.putExtra("ID_KAMAR", kamar.getIdKamar());
            context.startActivity(intent);
        });

        // 3. Load Gambar Dinamis
        // Bagian LOAD GAMBAR di onBindViewHolder
        String urlGambar = "http://10.0.2.2:8000/storage/kamar/" + kamar.getFotoKamar();
        //String urlGambar = "http://192.168.1.15:8000/storage/kamar/" + kamar.getFotoKamar();


        Glide.with(context)
                .load(urlGambar)
                .signature(new ObjectKey(System.currentTimeMillis())) // Menandakan data selalu baru
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.img_kamar_1)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.imgKamar);
    }

    @Override
    public int getItemCount() {
        return listKamar != null ? listKamar.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipe, tvHarga, tvLabel, tvStatus;
        ImageView imgKamar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipe = itemView.findViewById(R.id.tvTipeKamar);
            tvHarga = itemView.findViewById(R.id.tvHargaKamar);
            tvLabel = itemView.findViewById(R.id.tvNoKamar);
            tvStatus = itemView.findViewById(R.id.tvStatusKamar);
            imgKamar = itemView.findViewById(R.id.img_kamar);
        }
    }
}