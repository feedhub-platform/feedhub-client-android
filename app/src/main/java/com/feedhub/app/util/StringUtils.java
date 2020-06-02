package com.feedhub.app.util;

import android.content.Context;

import androidx.annotation.NonNull;

import com.feedhub.app.R;

public class StringUtils {

    public static String cutString(@NonNull String string, int maxLength, boolean withDots) {
        if (string.isEmpty()) return string;

        if (string.length() > maxLength) {
            string = string.substring(0, maxLength - 1) + (withDots ? "..." : "");
        }

        return string;
    }

    @NonNull
    public static String getLanguageByCode(@NonNull Context context, @NonNull String code) {
        int resId;

        switch (code) {
            case "ru":
                resId = R.string.russian;
                break;
            case "en":
                resId = R.string.english;
                break;
            case "uk":
                resId = R.string.ukrainian;
                break;
            default:
                return "";
        }

        return context.getString(resId);
    }

}
