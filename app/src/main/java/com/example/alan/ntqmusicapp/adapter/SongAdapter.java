package com.example.alan.ntqmusicapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alan.ntqmusicapp.R;
import com.example.alan.ntqmusicapp.model.ItemClickListener;
import com.example.alan.ntqmusicapp.room.SongEntity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private ItemClickListener itemClickListener;
    private Context m_context;
    private List<SongEntity> songList;

    public SongAdapter(Context m_context, List<SongEntity> songList) {
        this.m_context = m_context;
        this.songList = songList;
    }

    public void setOnClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.lt_song_item, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        SongEntity song = songList.get(position);

        holder.txt_song_name.setText(song.getSong_name());
        holder.txt_song_singer.setText(song.getSinger());
        Picasso.with(m_context).load(song.getImage_url())
                .placeholder(R.mipmap.icon_song).error(R.mipmap.icon_song).into(holder.img_song_item);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img_song_item;
        private TextView txt_song_name, txt_song_singer;

        public SongViewHolder(View itemView) {
            super(itemView);
            img_song_item = itemView.findViewById(R.id.img_song_item);
            txt_song_name = itemView.findViewById(R.id.txt_song_name);
            txt_song_singer = itemView.findViewById(R.id.txt_song_singer);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.OnClickListener(getLayoutPosition());
        }
    }
}
