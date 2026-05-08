package com.delfy.kost.adapter;

import android.content.Context;
import android.content.Intent; // <--- INI TAMBAHANNYA
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.delfy.kost.R;
import com.delfy.kost.UpdateStatusActivity; // <--- PASTIKAN INI JUGA ADA
import com.delfy.kost.api.KomplainModel;
import java.util.List;

public class KomplainAdapter extends RecyclerView.Adapter<KomplainAdapter.KomplainViewHolder> {

    private Context context;
    private List<KomplainModel> komplainList;
    private OnUpdateStatusListener listener;

    public interface OnUpdateStatusListener {
        void onUpdateClick(KomplainModel komplain);
    }

    public KomplainAdapter(Context context, List<KomplainModel> komplainList, OnUpdateStatusListener listener) {
        this.context = context;
        this.komplainList = komplainList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public KomplainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Gunakan layout item_komplain_masuk yang kamu buat
        View view = LayoutInflater.from(context).inflate(R.layout.item_komplain_masuk, parent, false);
        return new KomplainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KomplainViewHolder holder, int position) {
        KomplainModel komplain = komplainList.get(position);

        String nama = (komplain.getPenyewa() != null) ? komplain.getPenyewa().getNama() : "Anonim";

        holder.tvDetail.setText(
                "Nama Penghuni : " + nama + "\n" +
                        "No. Kamar : " + komplain.getNoKamar() + "\n" +
                        "Keluhan : " + komplain.getSubjek() + "\n" +
                        "Status : " + komplain.getStatus()
        );

        // Logika Warna Status (Opsional)
        if (komplain.getStatus().equalsIgnoreCase("Menunggu")) {
            holder.tvDetail.append("");
        }

        // --- INI DIA LANGKAH 1 YANG KITA CARI ---
        // Saat tombol update ditekan, kita bungkus datanya lalu pindah halaman
        holder.btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateStatusActivity.class);

            // Paksa ID menjadi String agar tidak null saat diterima
            intent.putExtra("id_komplain", String.valueOf(komplain.getIdKomplain()));
            intent.putExtra("nama_penghuni", nama);
            intent.putExtra("no_kamar", komplain.getNoKamar());
            intent.putExtra("deskripsi", komplain.getSubjek());
            intent.putExtra("status", komplain.getStatus());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return komplainList != null ? komplainList.size() : 0;
    }

    public void updateData(List<KomplainModel> newList) {
        this.komplainList = newList;
        notifyDataSetChanged();
    }

    static class KomplainViewHolder extends RecyclerView.ViewHolder {
        TextView tvDetail;
        LinearLayout btnUpdate;

        KomplainViewHolder(View itemView) {
            super(itemView);
            // Sesuaikan ID dengan yang ada di item_komplain_masuk.xml
            tvDetail = itemView.findViewById(R.id.tv_detail_komplain);
            btnUpdate = itemView.findViewById(R.id.btn_update_status_1);
        }
    }
}