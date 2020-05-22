package com.feedhub.app.item;

import android.graphics.drawable.Drawable;

public class MainMenuItem {

    public String id;
    public String title;
    public Drawable icon;

    public MainMenuItem(String id, String title, Drawable icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public MainMenuItem() {}
}
