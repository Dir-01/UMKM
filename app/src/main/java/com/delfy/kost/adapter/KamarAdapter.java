package com.delfy.kost.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.delfy.kost.R;
import com.delfy.kost.api.KamarResponse;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

// PERHATIKAN BARIS INI: Cukup gunakan <KamarAdapter.KamarViewHolder>
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

        holder.tvNoKamar.setText("Kamar " + kamar.getNoKamar());
        holder.tvStatusKamar.setText(kamar.getStatus());

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        holder.tvHargaKamar.setText(formatRupiah.format(kamar.getHarga()).replace("Rp", "Rp "));

        if (kamar.getStatus().equalsIgnoreCase("kosong")) {
            holder.tvStatusKamar.setBackgroundResource(R.drawable.bg_badge_grey);
        } else if (kamar.getStatus().equalsIgnoreCase("terisi")) {
            holder.tvStatusKamar.setBackgroundResource(R.drawable.bg_badge_green);
        } else {
            holder.tvStatusKamar.setBackgroundResource(R.drawable.bg_badge_yellow);
        }
    }

    @Override
    public int getItemCount() {
        return listKamar.size();
    }

    public class KamarViewHolder extends RecyclerView.ViewHolder {
        TextView tvNoKamar, tvStatusKamar, tvHargaKamar;

        public KamarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoKamar = itemView.findViewById(R.id.tvNoKamar);
            tvStatusKamar = itemView.findViewById(R.id.tvStatusKamar);
            tvHargaKamar = itemView.findViewById(R.id.tvHargaKamar);
        }
    }
}