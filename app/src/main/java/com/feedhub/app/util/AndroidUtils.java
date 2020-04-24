package com.feedhub.app.util;

import android.net.ConnectivityManager;

import com.feedhub.app.common.AppGlobal;

public class AndroidUtils {

    public static boolean hasConnection() {
        ConnectivityManager cm = AppGlobal.connectivityManager;

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static int px(float dp) {
        return (int) (dp * AppGlobal.resources.getDisplayMetrics().density);
    }

    public static int dp(float px) {
        return (int) (px / AppGlobal.resources.getDisplayMetrics().density);
    }

}
