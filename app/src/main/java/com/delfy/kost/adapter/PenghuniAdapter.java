package com.delfy.kost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.delfy.kost.R;
import com.delfy.kost.model.Penghuni;
import java.util.List;

public class PenghuniAdapter extends RecyclerView.Adapter<PenghuniAdapter.PenghuniViewHolder> {

    private Context context;
    private List<Penghuni> penghuniList;

    public PenghuniAdapter(Context context, List<Penghuni> penghuniList) {
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
        Penghuni penghuni = penghuniList.get(position);
        holder.tvNamaPenghuni.setText(penghuni.getNamaPenghuni());
        holder.tvNoKamar.setText(penghuni.getNoKamar());
        holder.tvKontrak.setText(penghuni.getKontrak());
        holder.tvKontak.setText(penghuni.getKontak());
    }

    @Override
    public int getItemCount() {
        return penghuniList != null ? penghuniList.size() : 0;
    }

    public void updateList(List<Penghuni> newList) {
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