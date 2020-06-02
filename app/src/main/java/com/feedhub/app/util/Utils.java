package com.feedhub.app.util;

import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.annotation.StringDef;

import java.util.Locale;

public class Utils {

    public static final String LOCALE_RU = "ru";
    public static final String LOCALE_EN = "en";

    /**
     * use in onResume in activity
     * @param strLocale string value of locale (us, ua, etc)
     * @param context any context
     */
    public static void changeLocale(@NonNull Context context, @Locales String strLocale) {
        Locale locale = new Locale(strLocale);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        config.locale = locale;

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    @StringDef({LOCALE_EN, LOCALE_RU})
    public @interface Locales {
    }


}
