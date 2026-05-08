package com.delfy.kost.adapter;

import com.delfy.kost.R;
import com.delfy.kost.DetailPesananActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.delfy.kost.api.RiwayatResponse;
import java.util.List;

public class PesananAdapter extends RecyclerView.Adapter<PesananAdapter.ViewHolder> {
    private List<RiwayatResponse.RiwayatModel> list;
    private Context context;

    public PesananAdapter(List<RiwayatResponse.RiwayatModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pesanan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RiwayatResponse.RiwayatModel data = list.get(position);
        holder.tvTipe.setText(data.getTipeKamar());
        holder.tvDurasi.setText("Durasi sewa : " + data.getDurasiSewa() + " Bulan");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailPesananActivity.class);
            intent.putExtra("ID_TRANSAKSI", data.getIdTransaksi());
            intent.putExtra("TIPE", data.getTipeKamar());
            intent.putExtra("DURASI", String.valueOf(data.getDurasiSewa()));
            intent.putExtra("METODE", data.getMetodePembayaran());
            intent.putExtra("TOTAL", String.valueOf(data.getTotalHarga()));
            intent.putExtra("BUKTI", data.getBuktiBayar());
            if (data.getPenyewa() != null) {
                intent.putExtra("NAMA", data.getPenyewa().getNama());
                intent.putExtra("NO_HP", data.getPenyewa().getNoHp());
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipe, tvDurasi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipe = itemView.findViewById(R.id.tv_tipe_kamar_item);
            tvDurasi = itemView.findViewById(R.id.tv_durasi_item);
        }
    }
}