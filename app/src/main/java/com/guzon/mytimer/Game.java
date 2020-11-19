package com.guzon.mytimer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class Game {
    public Game() {
        clear();
    }

    public Date startGame;
    public Date startPlayGame;
    public ArrayList<Integer> addSeconds;
    public ArrayList<GamePause> pauses;
    public ArrayList<GameRing> ringRequired;

    public boolean active() {
        return startGame != null;
    }

    public void clear() {
        pauses = new ArrayList<>();
        addSeconds = new ArrayList<>();
        ringRequired = new ArrayList<>();
        startGame = null;
    }

    public int getSumSeconds() {
        if (startPlayGame == null) {
            return 0;
        }

        TimeSpan ts = new TimeSpan(new Date(), startPlayGame);
        int pauseSeconds = getPausesSecounds();

        return (int) Math.abs(ts.totalSeconds + pauseSeconds);
    }

    public int getSeconds() {
        if (startGame == null) {
            return 0;
        }
        TimeSpan ts = new TimeSpan(new Date(), startGame);
        int pauseSeconds = getPausesSecounds();
        int addSec = getAddSeconds();
        return (int) ts.totalSeconds + pauseSeconds + addSec;
    }

    public int getAddSeconds() {
        if (startGame == null) {
            return 0;
        }

        int res = 0;

        for (Integer i : addSeconds) {
            res += i;
        }

        return res;
    }

    public int getPausesSecounds() {
        if (startGame == null) {
            return 0;
        }
        int res = 0;

        for (GamePause p : pauses) {
            Date end = p.end == null ? new Date() : p.end;
            TimeSpan ts = new TimeSpan(p.start, end);
            res += ts.totalSeconds;
        }

        return res;
    }

    public GamePause getActivePause() {
        if (startGame == null) {
            return null;
        }

        if (pauses.size() == 0) {
            return null;
        }

        return pauses.get(pauses.size() - 1).end == null ? pauses.get(pauses.size() - 1) : null;
    }

    public void pause() {
        pauses.add(new GamePause());
    }

    public GameRing getRequiredRing() {
        if (startGame == null) {
            return null;
        }

        if (ringRequired.size() == 0) {
            return null;
        }

        int sec = getSeconds();
        for (GameRing r : ringRequired) {
            if (!r.played && r.seconds == sec) {
                r.played=true;
                return r;
            }
        }

        return null;
    }

GameOldData oldData;
    public boolean hasOldData() {
        return oldData!= null;
    }


}
