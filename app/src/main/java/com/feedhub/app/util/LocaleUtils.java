package com.feedhub.app.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class LocaleUtils {

    private static Locale mLocale;

    public static void setLocale(Locale locale) {
        mLocale = locale;
        if (mLocale != null) {
            Locale.setDefault(mLocale);
        }
    }

    public static void updateConfiguration(ContextThemeWrapper wrapper) {
        if (mLocale != null) {
            Configuration configuration = new Configuration();
            configuration.setLocale(mLocale);
            wrapper.applyOverrideConfiguration(configuration);
        }
    }

    public static void updateConfiguration(Application application, Configuration configuration){
        if(mLocale != null){
            Configuration config = new Configuration(configuration);
            config.locale = mLocale;
            Resources res = application.getBaseContext().getResources();
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
    }

    public static void updateConfiguration(Context context, String language, String country) {
        Locale locale = new Locale(language, country);
        setLocale(locale);
        if (mLocale != null) {
            Resources res = context.getResources();
            Configuration configuration = res.getConfiguration();
            configuration.locale = mLocale;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
    }

    public static String getPrefLangCode(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("lang_code", "en");
    }

    public static void setPrefLangCode(Context context, String mPrefLangCode) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("lang_code", mPrefLangCode);
        editor.apply();
    }

    public static String getPrefCountryCode(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("country_code", "US");
    }

    public static void setPrefCountryCode(Context context, String mPrefCountryCode) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("country_code", mPrefCountryCode);
        editor.apply();
    }
}