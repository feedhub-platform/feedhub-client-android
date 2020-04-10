package com.feedhub.app.util;

import androidx.annotation.NonNull;

import java.util.ArrayList;

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

}
