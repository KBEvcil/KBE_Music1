package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MPActivity extends AppCompatActivity {

    private Button btnPlayPause, btnSkipNext, btnSkipPrev;
    private TextView txtTitle, txtCurrentPos, txtDuration;
    private SeekBar seekBarMusic;

    private MusicListData currentMusic;
    private ArrayList<MusicListData> musics;
    private MediaPlayer playingMusic = MediaPlayerSingleton.getInstance();

    private Handler curPosHandler;

    BroadcastReceiver br = new SensorAppNoticeReceiver();

    Runnable durationUpdater = new Runnable() {
        @Override
        public void run() {
            try {
                if(playingMusic.getCurrentPosition() >= playingMusic.getDuration() - 50)
                    skipNext();
                updateProgBar();
            } finally {
                curPosHandler.postDelayed(durationUpdater, 100);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpactivity);

        curPosHandler = new Handler();
        musics = (ArrayList<MusicListData>) getIntent().getSerializableExtra("LIST");


        btnPlayPause = findViewById(R.id.btn_play);
        btnSkipPrev = findViewById(R.id.btn_prev);
        btnSkipNext = findViewById(R.id.btn_next);
        txtCurrentPos = findViewById(R.id.text_current_pos);
        txtDuration = findViewById(R.id.text_duration);
        txtTitle = findViewById(R.id.text_title);

        seekBarMusic = findViewById(R.id.seek_bar_music);

        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if(playingMusic != null && b) {
                    playingMusic.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setCurrentMusic();

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter("com.example.sensor.sendBroadcast");
        registerReceiver(br,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(br);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        curPosHandler.removeCallbacks(durationUpdater);
        playingMusic.stop();
    }

    public void setCurrentMusic() {
        currentMusic = musics.get(MediaPlayerSingleton.currentIndex);
        Log.println(Log.DEBUG,"SU ANKI SARKININ OZELLIKLERI: ",currentMusic.toString());
        txtTitle.setText(currentMusic.getName());

        btnPlayPause.setOnClickListener(v-> playPause());
        btnSkipNext.setOnClickListener(v-> skipNext());
        btnSkipPrev.setOnClickListener(v-> skipPrev());



        playMusic();
    }

    public void playMusic() {
        playingMusic.reset();
        try {
            playingMusic.setDataSource(currentMusic.getPath());
            playingMusic.prepare();
            setDurationText(txtDuration, playingMusic.getDuration());
            setDurationText(txtCurrentPos, playingMusic.getCurrentPosition());
            seekBarMusic.setProgress(0);
            seekBarMusic.setMax(playingMusic.getDuration());
            resumeMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playPause() {
        if(playingMusic.isPlaying()) {
            pauseMusic();
        } else {
            resumeMusic();
        }
    }

    public void resumeMusic() {
        playingMusic.start();
        btnPlayPause.setBackground(getDrawable(R.drawable.pause));
        durationUpdater.run();
    }

    public void pauseMusic() {
        playingMusic.pause();
        btnPlayPause.setBackground(getDrawable(R.drawable.play));
        curPosHandler.removeCallbacks(durationUpdater);
    }

    public void skipNext() {
        if(MediaPlayerSingleton.currentIndex == musics.size() - 1)
            return;
        MediaPlayerSingleton.currentIndex++;
        curPosHandler.removeCallbacks(durationUpdater);
        playingMusic.reset();
        setCurrentMusic();

    }

    public void skipPrev() {
        if(MediaPlayerSingleton.currentIndex == 0 || playingMusic.getCurrentPosition() > 5000) {
            playingMusic.seekTo(0);
            return;
        }
        MediaPlayerSingleton.currentIndex--;
        playingMusic.reset();
        setCurrentMusic();
    }

    public void updateProgBar() {
        seekBarMusic.setProgress(playingMusic.getCurrentPosition());
        setDurationText(txtCurrentPos, playingMusic.getCurrentPosition());
    }

    public void setDurationText(TextView textView, int ms) {
        int min, sec;
        min = ms/60000;
        sec = (ms%60000) / 1000;
        textView.setText(String.format("%02d:%02d",min,sec));
    }
}