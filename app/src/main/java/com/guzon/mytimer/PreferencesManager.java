package com.guzon.mytimer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by mosheg on 11/11/2014.
 */
public class PreferencesManager implements PreferencesManagerInterface{
    private String prefKey;
    private Context context;

    public PreferencesManager(Context context, String prefKey) {
        this.context = context;
        this.prefKey = prefKey;
    }

    public SharedPreferences sharedPreferences = null;
    public SharedPreferences.Editor editor = null;

    private SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(prefKey, 0);

        return sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        if (editor == null) {
            editor = getSharedPreferences().edit();
        }
        return editor;
    }
    public String get(String key, String defaultValue) {
        String res = getSharedPreferences().getString(key, defaultValue);
        Log.i("PreferencesManager"," get "+ key+":"+res);
        return res;
    }

    public boolean contains(String key) {
        return getSharedPreferences().contains(key);
    }

    public void put(String key, String value) {
        Log.i("PreferencesManager"," put "+ key+":"+value);
        getEditor().putString(key, value);
        getEditor().commit();
    }

    public int getInt(String key) {
        return tryParseInteger(get(key, ""),0);
    }
    public static Integer tryParseInteger(String value, Integer defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception Ex) {
        }
        return defaultValue;
    }

    @Override
    public void clear() {
        Log.i("PreferencesManager" ,"clear()");
        getEditor().clear();
        getEditor().commit();
    }
}
