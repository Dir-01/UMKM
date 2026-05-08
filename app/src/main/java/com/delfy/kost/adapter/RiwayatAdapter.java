package com.delfy.kost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.delfy.kost.R;
import com.delfy.kost.api.RiwayatResponse; // Gunakan RiwayatResponse yang baru

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {

    // 1. Pastikan nama variabel konsisten (kita pakai 'list')
    private List<RiwayatResponse.RiwayatModel> list;
    private Context context;

    public RiwayatAdapter(List<RiwayatResponse.RiwayatModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Pastikan R.layout.item_riwayat memang ada di folder layout kamu
        View view = LayoutInflater.from(context).inflate(R.layout.item_riwayat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RiwayatResponse.RiwayatModel item = list.get(position);

        holder.tvJudul.setText("Sewa " + item.getTipeKamar());

        // --- PERBAIKAN STATUS DINAMIS ---
        // Pastikan di RiwayatModel.java sudah ada method getStatus()
        String statusServer = item.getStatus();

        if (statusServer != null) {
            holder.tvStatus.setText(statusServer.toUpperCase());

            // Opsional: Beri warna berbeda agar lebih keren
            if (statusServer.equalsIgnoreCase("Berhasil")) {
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            } else {
                holder.tvStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
            }
        } else {
            holder.tvStatus.setText("PENDING");
        }

        holder.tvMetode.setText("Metode: " + item.getMetodePembayaran());

        // Format Rupiah
        Locale localeID = new Locale("id", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.tvTglHarga.setText(formatRupiah.format(item.getTotalHarga()));

        // Tampilkan Foto Bukti
        String urlFoto = "http://10.0.2.2:8000/storage/bukti_bayar/" + item.getBuktiBayar();
        // Ganti IP-nya agar foto bukti bayar muncul
        //String urlFoto = "http://192.168.1.15:8000/storage/bukti_bayar/" + item.getBuktiBayar();
        Glide.with(context)
                .load(urlFoto)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.ivBukti);
    }

    @Override
    public int getItemCount() {
        // 7. Gunakan nama variabel yang benar 'list'
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvTglHarga, tvStatus, tvMetode;
        ImageView ivBukti;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_item_judul);
            tvTglHarga = itemView.findViewById(R.id.tv_item_tanggal_harga);
            tvStatus = itemView.findViewById(R.id.tv_item_status);
            tvMetode = itemView.findViewById(R.id.tv_item_metode);
            ivBukti = itemView.findViewById(R.id.iv_item_bukti);
        }
    }
}