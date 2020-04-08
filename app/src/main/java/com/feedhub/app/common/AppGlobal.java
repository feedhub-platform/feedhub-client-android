package com.feedhub.app.common;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.room.Room;

public class AppGlobal extends Application {

    public static volatile AppDatabase database;
    public static volatile SharedPreferences preferences;
    public static volatile Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        database = Room.databaseBuilder(this, AppDatabase.class, "database").build();

        handler = new Handler(getMainLooper());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}
