package com.feedhub.app.item;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favorites")
public class Favorite extends News {

//    @NonNull
//    @PrimaryKey
//    public String id = "";
//
//    public String title;
//    public String body;
//    public String picture;
//    public String language;
//    public String originTitle;
//    public String originUrl;

    public Favorite() {
    }

    @Ignore
    public Favorite(News news) {
        this.id = news.id;
        this.body = news.body;
        this.language = news.language;
        this.originTitle = news.originTitle;
        this.originUrl = news.originUrl;
        this.picture = news.picture;
        this.title = news.title;
    }
}
