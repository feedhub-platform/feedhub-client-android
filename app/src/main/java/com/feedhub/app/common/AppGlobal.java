package com.feedhub.app.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Handler;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.room.Room;

public class AppGlobal extends Application {

    public static volatile AppDatabase database;
    public static volatile SharedPreferences preferences;
    public static volatile Handler handler;

    public static volatile ConnectivityManager connectivityManager;

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room
                .databaseBuilder(this, AppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        handler = new Handler(getMainLooper());

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}
