package com.feedhub.app.item;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Entity
public class News extends BaseNewsItem {

    @NonNull
    @PrimaryKey
    public String id = "";

    public News() {}

    @Ignore
    public News(JSONObject o) {
        id = o.optString("id");
        title = o.optString("title");
        body = o.optString("description");
        picture = o.optString("frontImageUrl");
        language = o.optString("language");

        JSONObject origin = o.optJSONObject("origin");
        if (origin != null) {
            originTitle = origin.optString("title");
            originUrl = origin.optString("url");
        }
    }

    @NonNull
    public static List<News> parse(@NonNull JSONArray jsonArray) {
        List<News> list = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(new News(jsonArray.optJSONObject(i)));
        }

        return list;
    }

}
