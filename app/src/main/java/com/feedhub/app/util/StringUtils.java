package com.feedhub.app.util;

import androidx.annotation.NonNull;

public class StringUtils {

    public static String cutString(@NonNull String string, int maxLength, boolean withDots) {
        if (string.isEmpty()) return string;

        if (string.length() > maxLength) {
            string = string.substring(0, maxLength - 1) + (withDots ? "..." : "");
        }

        return string;
    }

}
