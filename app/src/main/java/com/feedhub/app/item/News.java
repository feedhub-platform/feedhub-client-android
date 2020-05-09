package com.feedhub.app.item;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

@Entity
public class News {

    @NonNull
    @PrimaryKey
    public String id = "";

    public String title;
    public String body;
    public String picture;
    public String language;
    public String originTitle;
    public String originUrl;

    public News() {
    }

    @Ignore
    public News(String title, String body, String picture) {
        this.title = title;
        this.body = body;
        this.picture = picture;
    }

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
}
