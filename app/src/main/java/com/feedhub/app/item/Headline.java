package com.feedhub.app.item;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Headline {

    @NonNull
    @PrimaryKey
    public String id = "";

    public String title = "";

    @Ignore
    public ArrayList<Topic> topics = new ArrayList<>();

    public Headline() {}
}
