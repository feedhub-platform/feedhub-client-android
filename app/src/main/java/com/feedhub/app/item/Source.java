package com.feedhub.app.item;

import org.json.JSONObject;

public class Source {

    public String id;
    public String title;
    public String description;
    public String frontPictureUrl;

    public Source() {}

    public Source(JSONObject o) {
        id = o.optString("id");
        title = o.optString("title");
        description = o.optString("description");
        frontPictureUrl = o.optString("frontPictureUrl");
    }

}
