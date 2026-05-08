package com.delfy.kost.adapter;

import android.content.Context;
import android.content.Intent; // Tambahkan ini
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.delfy.kost.R;
import com.delfy.kost.DetailPenghuniActivity; // Pastikan Activity ini sudah dibuat
import com.delfy.kost.api.RiwayatResponse;
import java.util.List;

public class PenghuniAdapter extends RecyclerView.Adapter<PenghuniAdapter.PenghuniViewHolder> {

    private Context context;
    private List<RiwayatResponse.RiwayatModel> penghuniList;

    public PenghuniAdapter(Context context, List<RiwayatResponse.RiwayatModel> penghuniList) {
        this.context = context;
        this.penghuniList = penghuniList;
    }

    @NonNull
    @Override
    public PenghuniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_penghuni, parent, false);
        return new PenghuniViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PenghuniViewHolder holder, int position) {
        RiwayatResponse.RiwayatModel data = penghuniList.get(position);

        if (data.getPenyewa() != null) {
            holder.tvNamaPenghuni.setText("Nama Penghuni : " + data.getPenyewa().getNama());
            holder.tvKontak.setText("Kontak : " + data.getPenyewa().getNoHp());
        }

        if (data.getKamar() != null) {
            holder.tvNoKamar.setText("No. Kamar : " + data.getKamar().getNoKamar());
        }

        holder.tvKontrak.setText("Kontrak : " + data.getDurasiSewa() + " Bulan");

        // --- FITUR KLIK UNTUK DETAIL KTP ---
        holder.itemView.setOnClickListener(v -> {
            if (data.getPenyewa() != null) {
                Intent intent = new Intent(context, DetailPenghuniActivity.class);
                // Kirim ID Penyewa agar di halaman detail bisa panggil API show($id)
                intent.putExtra("ID_PENYEWA", data.getIdPenyewa());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return penghuniList != null ? penghuniList.size() : 0;
    }

    public void updateList(List<RiwayatResponse.RiwayatModel> newList) {
        this.penghuniList = newList;
        notifyDataSetChanged();
    }

    static class PenghuniViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaPenghuni, tvNoKamar, tvKontrak, tvKontak;

        PenghuniViewHolder(View itemView) {
            super(itemView);
            tvNamaPenghuni = itemView.findViewById(R.id.tvNamaPenghuni);
            tvNoKamar = itemView.findViewById(R.id.tvNoKamar);
            tvKontrak = itemView.findViewById(R.id.tvKontrak);
            tvKontak = itemView.findViewById(R.id.tvKontak);
        }
    }
}