package com.feedhub.app.net;

import androidx.annotation.NonNull;

public class NetUtils {

    private final static String PROTOCOL_HTML = "https://";

    public static String transformUrl(@NonNull String url) {
        if (url.contains(PROTOCOL_HTML)) return url;

        return PROTOCOL_HTML + url;
    }

}
