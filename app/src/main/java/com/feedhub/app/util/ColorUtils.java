package com.feedhub.app.util;

import android.graphics.Color;

public class ColorUtils {

    public static int alphaColor(int color) {
        return alphaColor(color, 0.85f);
    }

    public static int alphaColor(int color, float alphaFactor) {
        int alpha = Color.alpha(color);

        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        return Color.argb((int) (alpha * alphaFactor), red, green, blue);
    }

}
