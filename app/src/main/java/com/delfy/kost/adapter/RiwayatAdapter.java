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
        // 2. Gunakan RiwayatModel, bukan TransaksiItem
        RiwayatResponse.RiwayatModel item = list.get(position);

        holder.tvJudul.setText("Sewa " + item.getTipeKamar());

        // 3. Ambil Status (karena di RiwayatModel kita belum buat getStatus, pastikan di RiwayatResponse sudah ada)
        // Jika belum ada getStatus di RiwayatModel, sesuaikan dengan variabel di Laravel
        holder.tvStatus.setText("MENUNGGU"); // Default atau sesuaikan

        holder.tvMetode.setText("Metode: " + item.getMetodePembayaran());

        // 4. Format Rupiah
        Locale localeID = new Locale("id", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String hargaStr = formatRupiah.format(item.getTotalHarga());

        // 5. Tampilkan Tanggal dan Harga
        // Karena di RiwayatModel adanya getIdTransaksi, jika ingin tanggal harus tambah getTanggalMasuk di RiwayatModel
        holder.tvTglHarga.setText(hargaStr);

        // 6. Tampilkan Foto Bukti
        // SESUAIKAN IP: Gunakan 10.0.2.2 jika pakai Emulator Android Studio
        String urlFoto = "http://10.0.2.2:8000/storage/bukti_bayar/" + item.getBuktiBayar();

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