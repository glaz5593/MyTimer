package com.guzon.mytimer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Date;

public class MainActivity extends Activity implements GameManager.GameManagerListener {
    Game game;
    boolean isActive;
    Thread thread;
    TextView tv_time_added,tv_time_old;
    FontFitTextView tv_time;
    View btn_play,btn_stop, btn_pause, btn_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        tv_time = findViewById(R.id.tv_time);
        tv_time_added = findViewById(R.id.tv_time_added);
        tv_time_old = findViewById(R.id.tv_time_old);

        btn_play = findViewById(R.id.btn_play);
        btn_stop = findViewById(R.id.btn_stop);
        btn_pause = findViewById(R.id.btn_pause);
        btn_new = findViewById(R.id.btn_new);

        Typeface myTypeface = Typeface.createFromAsset(this.getAssets(),
                "dismay.otf");
        tv_time.setTypeface(myTypeface);
        tv_time_added.setTypeface(myTypeface);
        tv_time_old.setTypeface(myTypeface);
    }

    public void onArrowRaiseClick(View view) {
        int sec=Integer.parseInt(view.getTag().toString());
        addToTime(sec);

        switch (sec){
            case -20:{
                playRaw(R.raw.minus_20);
                break;
            }
            case 20:{
                playRaw(R.raw.plus_20);
                break;
            }
            case -60:{
                playRaw(R.raw.minus_60);
                break;
            }
            case 60:{
                playRaw(R.raw.plus_60);
                break;
            }
        }
        GameManager.getInstance().saveGame();
    }


    private void addToTime(int addSeconds) {
        if (game == null) {
            return;
        }

        game.addSeconds.add(addSeconds);
        updateUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        game = GameManager.getInstance().getActive();
        updateUi();
        runningTimeThread();
        GameManager.getInstance().setListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    private void runningTimeThread() {
        if (thread != null) {
            return;
        }

        if(game==null){
            return;
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isActive && game!=null && game.active()) {
                    tv_time.post(new Runnable() {
                        @Override
                        public void run() {
                            updateTimeUi();
                        }
                    });

                    GameRing ring=game.getRequiredRing();
                    if(ring!=null){
                        playGameRing(ring);
                    }

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                tv_time.post(new Runnable() {
                    @Override
                    public void run() {
                        updateTimeUi();
                    }
                });

                thread=null;
            }
        });
        thread.start();
    }

    private void updateUi() {
        updateTimeUi();

        tv_time_old.setVisibility(View.GONE);

        if (game == null) {
            btn_play.setVisibility(View.GONE);
            btn_stop.setVisibility(View.GONE);
            btn_pause.setVisibility(View.INVISIBLE);
            btn_new.setVisibility(View.VISIBLE);
            tv_time_added.setText("");
            return;
        }

        boolean hasActivePause = game.getActivePause() != null;
        int sec = game.getPausesSecounds() + game.getAddSeconds();
        tv_time_added.setText(sec ==0 ? "" : GameManager.getInstance().getTimeToString(sec));

        if (!game.active()) {
            btn_play.setVisibility(View.GONE);
            btn_stop.setVisibility(View.GONE);
            btn_pause.setVisibility(View.INVISIBLE);
            btn_new.setVisibility(View.VISIBLE);

            if(game.hasOldData()){
                tv_time_old.setVisibility(View.VISIBLE);
                tv_time_old.setText(game.oldData.time);
                tv_time_added.setText(game.oldData.addedTime);
            }
            return;
        }

        btn_new.setVisibility(View.GONE);
        btn_stop.setVisibility(View.VISIBLE);

        btn_play.setVisibility(hasActivePause ? View.VISIBLE : View.GONE);
        btn_pause.setVisibility(!hasActivePause ? View.VISIBLE : View.GONE);
    }

    private void updateTimeUi() {
        if (game == null) {
            tv_time.setText("--:--");
            return;
        }

        if (game.getActivePause() != null) {
            return;
        }

        int sec=game.getSeconds();
        if(sec < -60000){
            GameManager.getInstance().stopGame();
        }

        tv_time.setText(GameManager.getInstance().getTimeToString(sec));
    }

    public void onNewGameClick(View view) {
        GameManager.getInstance().startNewGame();
        runningTimeThread();
        updateUi();
        playRaw(R.raw.new_game);
    }

    public void onStopClick(View view) {
        GameManager.getInstance().stopGame();
        updateUi();
        playRaw(R.raw.stop_game);
    }

    public void onPlayClick(View view) {
        game.getActivePause().end=new Date();
        GameManager.getInstance().saveGame();
        updateUi();
        playRaw(R.raw.next);
    }

    public void onPauseClick(View view) {
        game.pause();
        GameManager.getInstance().saveGame();
        updateUi();
        playRaw(R.raw.pause);
    }


    private void playRaw(int resId) {
        playRaw( resId, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }
        });
    }
    private void playGameRing(final GameRing ring) {
        playRaw( ring.resIdList[ring.ringIndex], new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if ((ring.ringIndex + 1) < ring.resIdList.length) {
                    ring.ringIndex++;
                    playGameRing(ring);
                }
            }
        });
    }

    public  void playRaw(int resId, MediaPlayer.OnCompletionListener listener) {
        //playRaw(getApplicationContext(), resId,listener);

        WavPlayer w=new WavPlayer(getApplicationContext(), resId,listener);
        w.run();
    }


    public static void playRaw(Context context, int resId, MediaPlayer.OnCompletionListener listener) {
        MediaPlayer mp = MediaPlayer.create(context,resId);
        Log.i("WavPlayer", "run");

        try {
            // mp.setAudioStreamType(AudioType.NOTIFICATION.audioStreamName);
            mp.start();
            if(listener!=null) {
                mp.setOnCompletionListener(listener);
            }
        } catch (Exception e) {
            Log.i("error",e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onGameUpdate(Game game) {
        if(isActive) {
            this.game = game;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateUi();
                    runningTimeThread();
                }
            });
        }
    }
}
