package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MusicListActivity extends AppCompatActivity {
    ArrayList<MusicListData> musicList = new ArrayList<>();
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        recyclerView = findViewById(R.id.music_list_recycle_view);

        if(checkPermission() == false) {
            requestPermission();
            return;
        }

        String[] musicProjection = {
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION
        };

        String musicSelection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, musicProjection, musicSelection,null, null);
        while(cursor.moveToNext()) {
            MusicListData musicListData = new MusicListData(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            if(new File(musicListData.getPath()).exists())
                musicList.add(musicListData);
        }

        Collections.sort(musicList, Comparator.comparing(MusicListData::getName));

        if(!musicList.isEmpty()) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicListAdapter(musicList, getApplicationContext()));
        }

    }

    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission( MusicListActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MusicListActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
            Toast.makeText(MusicListActivity.this, "Need read external storage permission. Please allow it from settings.", Toast.LENGTH_LONG).show();
        ActivityCompat.requestPermissions(MusicListActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
    }
}