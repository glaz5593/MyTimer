package com.guzon.mytimer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by mosheg on 24/05/2017.
 */

public class WavPlayer implements Runnable {
    public static ArrayList<WavPlayer> wavPlayers;
    MediaPlayer mplayer;
    int soundId;
    Context context;
    boolean isCompletion;
    MediaPlayer.OnCompletionListener listener;

    static void addWavPlayer(WavPlayer wp) {
        if (wavPlayers == null) {
            wavPlayers = new ArrayList<>();
        }

        wavPlayers.add(wp);
        clearWavPlayers();
    }

    private static void clearWavPlayers() {
        for (WavPlayer wp : wavPlayers) {
            if (!wp.mplayer.isPlaying()) {
                wp.mplayer.stop();
            }
        }

        wavPlayers.clear();
    }

    public WavPlayer(Context context, int soundId,MediaPlayer.OnCompletionListener listener) {
        this.context = context;
        this.soundId = soundId;
        this.listener = listener;
    }

    public void run() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                play();
            }
        });
    }

    private void play() {
        try {
            Log.i("WavPlayer", "run");
            mplayer = MediaPlayer.create(context, soundId);
            //mplayer.setAudioStreamType(AudioType.NOTIFICATION.audioStreamName);

            Log.i("WavPlayer", "start");
            mplayer.start();

            mplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.i("WavPlayer", "onCompletion");
                    isCompletion = true;
                    mp.release();

                    listener.onCompletion(mp);
                }
            });
            mplayer.setLooping(false);
        } catch (Exception ex) {

            Log.e("WavPlayer", "Exception:", ex);
        }
    }
}
