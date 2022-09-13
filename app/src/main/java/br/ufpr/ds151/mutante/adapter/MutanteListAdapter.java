package br.ufpr.ds151.mutante.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.ufpr.ds151.mutante.R;
import br.ufpr.ds151.mutante.helper.ImageConverter;
import br.ufpr.ds151.mutante.model.MutanteDTO;

public class MutanteListAdapter extends RecyclerView.Adapter<MutanteListAdapter.MyViewHolder> {

    private List<MutanteDTO> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        ImageView foto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textViewMutante);
            foto = itemView.findViewById(R.id.imageViewMutante);
        }
    }

    public MutanteListAdapter(List<MutanteDTO> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mutanteItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.mutante_cell, parent, false);
        return new MyViewHolder(mutanteItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MutanteDTO mutanteDTO = list.get(position);
        holder.nome.setText(mutanteDTO.getNome());
        holder.foto.setImageBitmap(ImageConverter.base64ToBitmap(mutanteDTO.getFoto()));
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

}
