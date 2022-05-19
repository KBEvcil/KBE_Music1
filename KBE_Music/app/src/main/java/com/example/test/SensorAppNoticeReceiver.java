package com.example.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

public class SensorAppNoticeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra("state", 0);
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(state == 0) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + AudioManager.ADJUST_RAISE * 5,AudioManager.FLAG_VIBRATE);
        } else if(state == 1) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_MUTE,AudioManager.FLAG_VIBRATE);
        } else if(state == 2) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + AudioManager.ADJUST_LOWER * 5,AudioManager.FLAG_VIBRATE);
        }
        Toast.makeText(context, "Broadcast received.(State " + state + ")",Toast.LENGTH_LONG).show();
    }
}
