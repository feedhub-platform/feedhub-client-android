package com.feedhub.app.item;

import java.io.Serializable;

public class BaseNewsItem implements Serializable {

    public String title;
    public String body;
    public String picture;
    public String language;
    public String originTitle;
    public String originUrl;

    public BaseNewsItem() {
    }
}
