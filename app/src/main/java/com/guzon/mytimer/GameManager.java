package com.guzon.mytimer;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;

public class GameManager {
    private Game game;
    String gameTag=FlavorManager.getFirebaseTag();

    private static GameManager instance;
    GameManagerListener listener;
    public interface GameManagerListener{void onGameUpdate(Game game);}
    //PreferencesManagerInterface preferences;
    //DatabaseReference database;
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    public void  setListener( GameManagerListener listener){
    this.listener=listener;
}
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    GameManager() {
        database = FirebaseFirestore.getInstance();
        if (game == null) {
            game = new Game();
        }

        database.collection(gameTag).document("game").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("GameManager", "DocumentSnapshot data: " + document.getData());
                        setGameFromFirebase(document.getData().toString());
                    } else {
                        Log.d("GameManager", "No such document");
                    }
                } else {
                    Log.d("GameManager", "get failed with ", task.getException());
                }
            }
        });

        database.collection(gameTag).document("game").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("onEvent", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("onEvent", "Current data: " + snapshot.getData());
                    setGameFromFirebase(snapshot.getData().toString());
                } else {
                    Log.d("onEvent", "Current data: null");
                }
            }
        });
    }

    private void setGameFromFirebase(String data) {
        String j = data;
        j = removeFromLeft(j, "{game=");
        j = removeFromRight(j, 1);
        Game G = Json.toObject(j, Game.class);
        if (G != null) {
            game = G;
            updateUi();
        }
    }

    private void updateUi(){
        if (listener != null) {
            listener.onGameUpdate(game);
        }
    }

    public void  saveGame(){
        String j = Json.toString(game);
        Hashtable<String,String> list= new Hashtable<>();
        list.put("game",j);
        database.collection(gameTag).document("game").set(list).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public Game getActive() {
        return game;
    }

    public void startNewGame() {
        stopGame();

        game.startPlayGame=new Date();
        game.endGame = Utils.addSeconds(game.startPlayGame, FlavorManager.getGameSeconds());
        game.ringRequired = new ArrayList<>();
        game.ringRequired.add(new GameRing(1,R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.time_end, R.raw.time_end));

        for (int i = 2; i <=11; i++) {
            game.ringRequired.add(new GameRing(i, R.raw.buzzer));
        }

        game.ringRequired.add(new GameRing(30, R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.d0_5));
        game.ringRequired.add(new GameRing(60, R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.d1));
        game.ringRequired.add(new GameRing(120, R.raw.buzzer, R.raw.buzzer, R.raw.d2));

        saveGame();
    }

    public void stopGame() {
        GameOldData oldData=new GameOldData();
        oldData.time=getTimeToString(game.getSumSeconds());
        oldData.addedTime=getTimeToString(game.getAddSeconds() + game.getPausesSecounds());
        game.clear();
        game.oldData = oldData;
        saveGame();
    }

    public String getTimeToString(int seconds) {
        if (seconds == 0) {
            return "00:00";
        }

        if(seconds < -60000){
            return "--:--";
        }

        String minus="";
        if(seconds < 0){
            minus="-";
            seconds*=-1;
        }

        int m = seconds / 60;
        int s = seconds % 60;

        String res = minus + padLeft(m+"",'0',2) + ":" + padLeft(s+"",'0',2);
        Log.i("Logg","Seconds:" + seconds+" Time:" + res);
        return res;
    }

    public static String padRight(String str, char chr, int count) {
        return paddingString(str, count, chr, false);
    }

    public static String padLeft(String str, char chr, int count) {
        return paddingString(str, count, chr, true);
    }

    public static String paddingString(String s, int n, char c, boolean paddingLeft) {
        if (s == null) {
            return s;
        }
        int add = n - s.length(); // may overflow int size... should not be a problem in real life
        if (add <= 0) {
            return s;
        }
        StringBuffer str = new StringBuffer(s);
        char[] ch = new char[add];
        Arrays.fill(ch, c);
        if (paddingLeft) {
            str.insert(0, ch);
        } else {
            str.append(ch);
        }
        return str.toString();
    }

    public static String removeFromRight(String text, String s) {
        String res = text;
        while (res.length() >= s.length() &&
                equals(res.substring(res.length() - s.length()), s)) {
            res = res.substring(0, res.length() - s.length());
        }

        return res;
    }

    public static String fromRight(String text, int length) {
        if (isNullOrEmpty(text)) {
            return "";
        }

        if (text.length() < length) {
            return text;
        }

        return text.substring(text.length() - length);
    }

    public static String removeFromRight(String text, int length) {
        if (isNullOrEmpty(text)) {
            return "";
        }

        if (text.length() <= length) {
            return "";
        }

        return text.substring(0, text.length() - length);
    }

    public static String removeFromLeft(String text, String s) {
        String res = text;
        while (res.length() >= s.length() &&
                equals(res.substring(0, s.length()), s)) {
            res = res.substring(s.length());
        }

        return res;
    }

    public static boolean isNullOrEmpty(String str) {
        return (str == null || str.equals(""));
    }
    public static boolean equals(String str1, String str2) {
        String s1 = str1 == null ? "" : str1;
        String s2 = str2 == null ? "" : str2;
        return s2.equals(s1);
    }
    public static String removeFromLeft(String text, int length) {
        if (isNullOrEmpty(text)) {
            return "";
        }

        if (text.length() <= length) {
            return "";
        }

        return text.substring(length, text.length());
    }
}
