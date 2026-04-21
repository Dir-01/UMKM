package com.delfy.kost.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.delfy.kost.R;
import com.delfy.kost.UpdateStatusActivity;
import com.delfy.kost.UpdateStatusBerhasilActivity;
import com.delfy.kost.model.Komplain;
import java.util.List;

public class KomplainAdapter extends RecyclerView.Adapter<KomplainAdapter.KomplainViewHolder> {

    private Context context;
    private List<Komplain> komplainList;

    public KomplainAdapter(Context context, List<Komplain> komplainList) {
        this.context = context;
        this.komplainList = komplainList;
    }

    @NonNull
    @Override
    public KomplainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_komplain, parent, false);
        return new KomplainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KomplainViewHolder holder, int position) {
        Komplain komplain = komplainList.get(position);
        holder.tvNama.setText(komplain.getNamaPenghuni());
        holder.tvNoKamar.setText(komplain.getNoKamar());
        holder.tvKeluhan.setText(komplain.getKeluhan());
        holder.tvStatus.setText(komplain.getStatus());

        holder.btnUpdateStatus.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateStatusActivity.class);
            intent.putExtra("komplain_id", komplain.getId());
            intent.putExtra("nama", komplain.getNamaPenghuni());
            intent.putExtra("no_kamar", komplain.getNoKamar());
            intent.putExtra("keluhan", komplain.getKeluhan());
            intent.putExtra("status", komplain.getStatus());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return komplainList != null ? komplainList.size() : 0;
    }

    static class KomplainViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvNoKamar, tvKeluhan, tvStatus;
        Button btnUpdateStatus;

        KomplainViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNoKamar = itemView.findViewById(R.id.tvNoKamar);
            tvKeluhan = itemView.findViewById(R.id.tvKeluhan);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnUpdateStatus = itemView.findViewById(R.id.btnUpdateStatus);
        }
    }
}
