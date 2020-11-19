package com.guzon.mytimer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class GameManager {
    private Game game;
    private static GameManager instance;
    PreferencesManagerInterface preferences;
    DatabaseReference database;

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    GameManager() {
        database = FirebaseDatabase.getInstance().getReference();
        preferences = new PreferencesManager(AppBase.getContext(), "t1");
        String j = preferences.get("game", "");
        game = Json.toObject(j, Game.class);
        if (game == null) {
            game = new Game();
        }

        database.child("game").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String j = snapshot.getValue().toString();
                game = Json.toObject(j, Game.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void  saveGame(){
        String j = Json.toString(game);
        preferences.put("game",j);
        database.child("game").setValue(j);
    }

    public Game getActive() {
        return game;
    }

    public void startNewGame() {
        stopGame();

        game.startGame = Utils.addSeconds(new Date(), 901);
        game.startPlayGame=new Date();
        game.ringRequired = new ArrayList<>();
        game.ringRequired.add(new GameRing(1,R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.time_end, R.raw.time_end));

        for (int i = 2; i <=11; i++) {
            game.ringRequired.add(new GameRing(i, R.raw.buzzer));
        }

        game.ringRequired.add(new GameRing(30, R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.d0_5));
        game.ringRequired.add(new GameRing(60, R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.d1));
        game.ringRequired.add(new GameRing(120, R.raw.buzzer, R.raw.buzzer, R.raw.d2));
        game.ringRequired.add(new GameRing(180, R.raw.buzzer, R.raw.buzzer, R.raw.d3));
        game.ringRequired.add(new GameRing(300, R.raw.buzzer, R.raw.d5));
        game.ringRequired.add(new GameRing(600, R.raw.buzzer, R.raw.d10));

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

        String minus="";
        if(seconds < 0){
            minus="-";
            seconds*=-1;
        }

        int m = seconds / 60;
        int s = seconds % 60;

        return minus + padLeft(m+"",'0',2) + ":" + padLeft(s+"",'0',2);
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

}
