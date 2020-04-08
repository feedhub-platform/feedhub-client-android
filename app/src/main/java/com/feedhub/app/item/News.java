package com.feedhub.app.item;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

@Entity
public class News {

    @PrimaryKey
    public int id;

    public String title;
    public String body;
    public String picture;
    public String language;
    public String originTitle;
    public String originUrl;

    public News() {
    }

    public News(String title, String body, String picture) {
        this.title = title;
        this.body = body;
        this.picture = picture;
    }

    public News(JSONObject o) {
//        this.id = o.optInt("id")
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
