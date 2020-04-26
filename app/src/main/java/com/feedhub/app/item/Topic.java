package com.feedhub.app.item;

import org.json.JSONObject;

public class Topic {

    public String id;
    public String title;

    public Topic() {

    }

    public Topic(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public Topic(JSONObject o) {
        id = o.optString("id");
        title = o.optString("title");
    }
}
