package com.feedhub.app.net;

import androidx.annotation.NonNull;

public class NetUtils {

    private final static String PROTOCOL_HTML = "https://";

    public static String transformUrl(@NonNull String url) {
        if (url.contains(PROTOCOL_HTML)) return url;

        StringBuilder builder = new StringBuilder(PROTOCOL_HTML);
        builder.append(url);

        String lastChar = url.substring(url.length() - 2);

        if (!lastChar.equals("/")) builder.append("/");

        return builder.toString();
    }

}
