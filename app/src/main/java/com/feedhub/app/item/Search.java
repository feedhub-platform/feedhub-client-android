package com.feedhub.app.item;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "searches")
public class Search extends BaseNewsItem {

    @PrimaryKey
    @NonNull
    public String query;

    public Search(@NonNull String query) {
        this.query = query;
    }

}
