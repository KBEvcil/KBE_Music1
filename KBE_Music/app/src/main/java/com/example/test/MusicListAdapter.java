package com.example.test;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    ArrayList<MusicListData> listData;
    Context context;

    public MusicListAdapter(ArrayList<MusicListData> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View musicListItem = layoutInflater.inflate(R.layout.music_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(musicListItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MusicListData musicListData = listData.get(position);
        holder.textName.setText(listData.get(position).getName());
        //holder.textDuration.setText(listData.get(position).getDuration());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayerSingleton.getInstance().reset();
                MediaPlayerSingleton.currentIndex = holder.getAdapterPosition();
                Intent switchMediaPlayer = new Intent(context, MPActivity.class);
                switchMediaPlayer.putExtra("LIST", listData);
                switchMediaPlayer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(switchMediaPlayer);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName;
        public TextView textDuration;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textName = itemView.findViewById(R.id.music_name);
        }
    }
}
