package com.feedhub.app.item;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

@Entity
public class Headline {

    @NonNull
    @PrimaryKey
    public String id = "";

    public Headline() {}
}
