package com.guzon.mytimer;

import java.util.Date;

public class GameRing {
    public GameRing() {
        this.resIdList =new int[0];
    }

    public GameRing(int seconds, int... resIdList) {
        this.seconds = seconds;
        this.resIdList = resIdList;
    }
    public boolean played;
    public int ringIndex;
    public int seconds;
    public int[] resIdList;
}
