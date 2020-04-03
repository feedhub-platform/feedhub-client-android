package com.feedhub.app.item;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class News {

    @PrimaryKey
    public int id;

    public String title;
    public String body;
    public String picture;

    public News() {
    }

    public News(String title, String body, String picture) {
        this.title = title;
        this.body = body;
        this.picture = picture;
    }
}
