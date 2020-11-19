package com.guzon.mytimer;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDatabaseManager implements PreferencesManagerInterface {
    DatabaseReference database;

    public  FirebaseDatabaseManager(){
        database= FirebaseDatabase.getInstance().getReference();
    }
    @Override
    public String get(String key, String defaultValue) {
        return database.orderByChild(key).toString();
    }

    @Override
    public void put(String key, String value) {
        DatabaseReference child=database.child(key);
        child.setValue(value);
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public int getInt(String key) {
        return 0;
    }

    @Override
    public void clear() {

    }
}
