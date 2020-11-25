package com.guzon.mytimer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Game implements Serializable {

    public Game() {
        clear();
    }

    public Date endGame;
    public Date startPlayGame;
    public ArrayList<Integer> addSeconds;
    public ArrayList<GamePause> pauses;
    public ArrayList<GameRing> ringRequired;
    public GameOldData oldData;
    public boolean active() {
        return endGame != null;
    }

    public void clear() {
        pauses = new ArrayList<>();
        addSeconds = new ArrayList<>();
        ringRequired = new ArrayList<>();
        endGame = null;
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
        if (endGame == null) {
            return 0;
        }
        TimeSpan ts = new TimeSpan(new Date(), endGame);
        int pauseSeconds = getPausesSecounds();
        int addSec = getAddSeconds();
        return (int) ts.totalSeconds + pauseSeconds + addSec;
    }

    public int getAddSeconds() {
        if (endGame == null) {
            return 0;
        }

        int res = 0;

        for (Integer i : addSeconds) {
            res += i;
        }

        return res;
    }

    public int getPausesSecounds() {
        if (endGame == null) {
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
        if (endGame == null) {
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
        if (endGame == null) {
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

    public boolean hasOldData() {
        return oldData!= null;
    }


}
