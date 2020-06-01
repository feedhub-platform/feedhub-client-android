package com.feedhub.app.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.room.Room;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.feedhub.app.R;
import com.feedhub.app.fragment.FragmentSettings;
import com.feedhub.app.util.Utils;

import java.util.Locale;

import ru.melod1n.library.mvp.base.MvpBase;

public class AppGlobal extends Application {

    public static volatile String strLocale;
    public static volatile AppDatabase database;
    public static volatile SharedPreferences preferences;
    public static volatile Handler handler;
    public static volatile Resources resources;
    public static volatile ConnectivityManager connectivityManager;
    public static volatile int colorAccent;

    public static AppGlobal getInstance() {
        return new AppGlobal();
    }

    public static void changeLocale(Context baseContext) {
        strLocale = AppGlobal.preferences.getString(FragmentSettings.KEY_LANGUAGE, Utils.LOCALE_EN);

        Utils.changeLocale(
                baseContext,
                strLocale
        );
    }

    public static void updateLocale(Context context, @Utils.Locales String strLocale) {
        AppGlobal.strLocale = strLocale;
        AppGlobal.resources = getLocalizedResources(context, new Locale(strLocale));
    }

    @NonNull
    private static Resources getLocalizedResources(@NonNull Context context, @NonNull Locale desiredLocale) {
        Configuration configuration = context.getResources().getConfiguration();

        configuration = new Configuration(configuration);
        configuration.setLocale(desiredLocale);

        Context localizedContext = context.createConfigurationContext(configuration);
        return localizedContext.getResources();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        changeLocale(getBaseContext());

//        resources = getResources();

        resources = getLocalizedResources(this, new Locale(strLocale));

        database = Room
                .databaseBuilder(this, AppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();

        handler = new Handler(getMainLooper());

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        colorAccent = resources.getColor(R.color.accent, null);

        MvpBase.init(handler);
    }
}
