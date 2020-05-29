package com.feedhub.app.item;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite")
public class Favorite {

    @NonNull
    @PrimaryKey()
    public String id = "";

    public String title;
    public String body;
    public String picture;
    public String language;
    public String originTitle;
    public String originUrl;

    public Favorite() {
    }

    @Ignore
    public Favorite(News news) {
        this.id = id;
        this.body = news.body;
        this.language = news.language;
        this.originTitle = news.originTitle;
        this.originUrl = news.originUrl;
        this.picture = news.picture;
        this.title = news.title;
    }

    @Ignore
    public Favorite(String title, String body, String picture) {
        this.title = title;
        this.body = body;
        this.picture = picture;
    }
}
