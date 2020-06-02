package com.feedhub.app.item;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Topic implements Serializable {

    public String id;
    public String title;

    public Topic() {

    }

    public Topic(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public Topic(@NonNull JSONObject o) {
        id = o.optString("id");
        title = o.optString("title");
    }

    @NonNull
    public static List<Topic> parse(@NonNull JSONArray jsonArray) {
        List<Topic> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(new Topic(jsonArray.optJSONObject(i)));
        }

        return list;
    }
}
