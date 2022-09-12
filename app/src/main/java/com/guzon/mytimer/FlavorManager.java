package com.guzon.mytimer;

import java.util.ArrayList;

public class FlavorManager {
    public static int getGameSeconds() {
        switch (BuildConfig.FLAVOR) {
            case "Goldberg":
                return 605;
            case "Chen":
                return 902;
        }

        return 0;
    }

    public static String getMainBottomText() {
        switch (BuildConfig.FLAVOR) {
            case "Goldberg":
                return "בית ספר לאה גולדברג Ⓒ פבל";
            case "Chen":
                return "כדורסל מוצ''ש Ⓒ משה";
        }

        return "";
    }

    public static int getBackground() {
        switch (BuildConfig.FLAVOR) {
            case "Goldberg":
                return R.drawable.background_goldberg;
            case "Chen":
                return R.drawable.background;
        }

        return 0;
    }

    public static String getFirebaseTag() {
        switch (BuildConfig.FLAVOR) {
            case "Goldberg":
                return "LeaGoldberg";
            case "Chen":
                return "gameM";
        }

        return "";
    }



    public static ArrayList<GameRing> getRingList() {
          ArrayList<GameRing> ring_list = new ArrayList<>();

        switch (BuildConfig.FLAVOR) {
            case "Goldberg": {
                ring_list = new ArrayList<>();
                ring_list.add(new GameRing(1, R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.time_end, R.raw.time_end));

                for (int i = 2; i <= 11; i++) {
                    ring_list.add(new GameRing(i, R.raw.buzzer));
                }

                ring_list.add(new GameRing(30, R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.d0_5));
                ring_list.add(new GameRing(60, R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.d1));
                ring_list.add(new GameRing(120, R.raw.buzzer, R.raw.buzzer, R.raw.d2));
                break;
            }
            case "Chen": {
                ring_list.add(new GameRing(1,R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.time_end, R.raw.time_end));

                for (int i = 2; i <=11; i++) {
                    ring_list.add(new GameRing(i, R.raw.buzzer));
                }

                ring_list.add(new GameRing(30, R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.d0_5));
                ring_list.add(new GameRing(60, R.raw.buzzer, R.raw.buzzer, R.raw.buzzer, R.raw.d1));
                ring_list.add(new GameRing(120, R.raw.buzzer, R.raw.buzzer, R.raw.d2));
                ring_list.add(new GameRing(180, R.raw.buzzer, R.raw.buzzer, R.raw.d3));
                ring_list.add(new GameRing(300, R.raw.buzzer, R.raw.d5));
                ring_list.add(new GameRing(600, R.raw.buzzer, R.raw.d10));

                break;
            }
        }

        return ring_list;
    }
}
