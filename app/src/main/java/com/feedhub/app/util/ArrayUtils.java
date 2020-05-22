package com.feedhub.app.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {

    @NonNull
    public static <T> ArrayList<T> prepareList(@NonNull ArrayList<T> list, int offset, int count) {
        if (list.isEmpty()) return list;

        ArrayList<T> newList = new ArrayList<>();

        if (count > list.size()) count = list.size();

        if (offset == 0) {
            for (int i = 0; i < count; i++) {
                newList.add(list.get(i));
            }
        } else {
            for (int i = offset - 1; i < count; i++) {
                newList.add(list.get(i));
            }
        }

        return newList;
    }

    @SafeVarargs
    public static <T> String toString(T... array) {
        if (array == null || array.length == 0) {
            return null;
        }

        StringBuilder builder = new StringBuilder(array.length * 12);
        builder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            builder.append(',');
            builder.append(array[i]);
        }
        return builder.toString();
    }

    public static String toString(List arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(arrayList.get(0));
        for (int i = 1; i < arrayList.size(); i++) {
            builder.append(',');
            builder.append(arrayList.get(i));
        }
        return builder.toString();
    }
}
