package com.feedhub.app.net;

import androidx.annotation.NonNull;

public class NetUtils {

    private final static String PROTOCOL_HTML = "https://";

    @NonNull
    public static String transformUrl(@NonNull String url) {
        if (url.isEmpty()) return url;

        StringBuilder builder = new StringBuilder(url.contains(PROTOCOL_HTML) ? "" : PROTOCOL_HTML);
        builder.append(url);

        String lastChar = url.substring(url.length() - 1);

        if (!lastChar.equals("/")) builder.append("/");

        return builder.toString();
    }

}
