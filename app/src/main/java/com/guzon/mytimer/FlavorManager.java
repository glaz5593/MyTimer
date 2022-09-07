package com.guzon.mytimer;

public class FlavorManager {
    public static int getGameSeconds() {
        switch (BuildConfig.FLAVOR){
            case "Goldberg":
                return 605;
            case "Chen":
                return 902;
        }

        return 0;
    }

    public static String getMainBottomText() {
        switch (BuildConfig.FLAVOR){
            case "Goldberg":
                return "בית ספר לאה גולדברג Ⓒ פבל";
            case "Chen":
                return "כדורסל מוצ''ש Ⓒ משה";
        }

        return "";
    }

    public static int getBackground() {
        switch (BuildConfig.FLAVOR){
            case "Goldberg":
                return R.drawable.background_goldberg;
            case "Chen":
                return R.drawable.background;
        }

        return 0;
    }

    public static String getFirebaseTag() {
        switch (BuildConfig.FLAVOR){
            case "Goldberg":
                return "LeaGoldberg";
            case "Chen":
                return "gameM";
        }

        return "";
    }
}
