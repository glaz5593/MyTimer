package com.guzon.mytimer;

import android.app.Application;
import android.content.Context;

public class AppBase extends Application {
    private static AppBase mInstance;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context=getApplicationContext();
    }

    public static synchronized AppBase getInstance() {
        return mInstance;
    }
    public static synchronized Context getContext() {
        return mInstance.getApplicationContext();
    }
}
