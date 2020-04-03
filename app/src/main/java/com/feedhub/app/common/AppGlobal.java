package com.feedhub.app.common;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.room.Room;

public class AppGlobal extends Application {

    public static volatile AppDatabase database;
    public static volatile SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        database = Room.databaseBuilder(this, AppDatabase.class, "database").build();

        int i = 0;
    }
}
