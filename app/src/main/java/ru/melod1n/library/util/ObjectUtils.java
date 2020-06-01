package ru.melod1n.library.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ObjectUtils {

    @NonNull
    public static <T> T checkNonNull(T object) {
        if (object == null) throw new NullPointerException("");

        return object;
    }

    public static boolean checkValueEquals(Object object, @Nullable Object value) {
        if (object != value) throw new IllegalArgumentException("doesn't equals");

        return true;
    }

    public static boolean checkValueEquals(Object object, @NonNull ArrayList arrayList) {
        boolean equals = false;

        for (Object o : arrayList) {
            if (object == o) {
                equals = true;
                break;
            }
        }

        if (!equals) throw new IllegalArgumentException("doesn't equals");

        return true;
    }

}
