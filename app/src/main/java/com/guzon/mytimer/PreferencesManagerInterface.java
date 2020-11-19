package com.guzon.mytimer;

/**
 * Created by mosheg on 13/07/2016.
 */
public interface PreferencesManagerInterface {
    public String get(String key, String defaultValue);
    public void put(String key, String value);
    public boolean contains(String key);
    public int getInt(String key);
    public void clear();
}
