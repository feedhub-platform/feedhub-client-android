package com.feedhub.app.mvp.repository;

import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.fragment.FragmentSettings;
import com.feedhub.app.item.Headline;
import com.feedhub.app.item.Topic;
import com.feedhub.app.net.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ru.melod1n.library.mvp.base.MvpBase;
import ru.melod1n.library.mvp.base.MvpFields;
import ru.melod1n.library.mvp.base.MvpOnLoadListener;
import ru.melod1n.library.mvp.base.MvpRepository;

public class HeadlinesRepository extends MvpRepository<Headline> {

    public void loadTopics(@Nullable MvpOnLoadListener<ArrayMap<String, ArrayList<Topic>>> listener) {
        RequestBuilder.create()
                .method(prefs.getString(FragmentSettings.KEY_TOPICS_KEY, FragmentSettings.KEY_TOPICS_KEY_DV))
                .execute(new RequestBuilder.OnResponseListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject root) {
                        try {
                            JSONObject response = Objects.requireNonNull(root.optJSONObject("response"));
                            JSONArray oTopics = Objects.requireNonNull(response.optJSONArray("topics"));

                            ArrayMap<String, ArrayList<Topic>> data = new ArrayMap<>();

                            for (int i = 0; i < oTopics.length(); i++) {
                                JSONObject jTopic = Objects.requireNonNull(oTopics.optJSONObject(i));

                                String categoryId = jTopic.optString("categoryId");

                                JSONArray oItems = Objects.requireNonNull(jTopic.optJSONArray("items"));

                                List<Topic> topics = Topic.parse(oItems);

                                data.put(categoryId, new ArrayList<>(topics));
                            }

                            sendValues(listener, data);
                        } catch (Exception e) {
                            e.printStackTrace();
                            sendError(e, listener);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        sendError(e, listener);
                    }
                });
    }

    private void sendError(@NonNull Exception e, @Nullable MvpOnLoadListener<ArrayMap<String, ArrayList<Topic>>> listener) {
        e.printStackTrace();

        if (listener != null) {
            MvpBase.handler.post(() -> listener.onErrorLoad(e));
        }
    }

    private void sendValues(@Nullable MvpOnLoadListener<ArrayMap<String, ArrayList<Topic>>> listener, @NonNull ArrayMap<String, ArrayList<Topic>> topics) {
        if (listener != null) {
            MvpBase.handler.post(() -> listener.onSuccessLoad(new ArrayList<>(Collections.singletonList(topics))));
        }
    }

    @Override
    public void loadValues(@NonNull MvpFields fields, @Nullable MvpOnLoadListener<Headline> listener) {
        String sourceId = fields.get("sourceId");
        ArrayMap<String, ArrayList<Topic>> topics = fields.get("topics");

        RequestBuilder builder = RequestBuilder.create();

        if (sourceId != null) {
            builder.method("platform/sources/" + sourceId);
        } else {
            builder.method(prefs.getString(FragmentSettings.KEY_CATEGORY_KEY, FragmentSettings.KEY_CATEGORY_KEY_DV));
        }

        builder.execute(new RequestBuilder.OnResponseListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject root) {
                try {
                    JSONObject response = Objects.requireNonNull(root.optJSONObject("response"));
                    JSONArray categories = Objects.requireNonNull(response.optJSONArray("categories"));

                    ArrayList<Headline> headlines = new ArrayList<>();

                    if (sourceId == null) { //parse just categories
                        assert topics != null;

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject category = categories.optJSONObject(i);

                            String id = category.optString("id");
                            String title = category.optString("title");

                            Headline headline = new Headline();
                            headline.id = id;
                            headline.title = title;

                            for (int j = 0; j < topics.size(); j++) {
                                String key = topics.keyAt(j);

                                if (id.equals(key)) {
                                    headline.topics = topics.valueAt(j);
                                    break;
                                }
                            }

                            if (headline.topics == null) headline.topics = new ArrayList<>();

                            headlines.add(headline);
                        }
                    } else { //parse sources
                        JSONArray jTopics = Objects.requireNonNull(response.optJSONArray("topics"));

                        ArrayMap<String, ArrayList<Topic>> topics = new ArrayMap<>();

                        for (int i = 0; i < jTopics.length(); i++) {
                            JSONObject topic = Objects.requireNonNull(jTopics.optJSONObject(i));

                            String categoryId = topic.optString("categoryId");

                            JSONArray oTopics = topic.optJSONArray("topics");

                            topics.put(categoryId, new ArrayList<>(Topic.parse(oTopics)));
                        }

                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject category = categories.optJSONObject(i);

                            String id = category.optString("id");
                            String title = category.optString("title");

                            Headline headline = new Headline();
                            headline.id = id;
                            headline.title = title;

                            for (int j = 0; j < topics.size(); j++) {
                                String key = topics.keyAt(j);

                                if (id.equals(key)) {
                                    headline.topics = topics.valueAt(j);
                                    break;
                                }
                            }

                            headlines.add(headline);
                        }
                    }

                    sendValues(fields, headlines, listener);
                } catch (Exception e) {
                    sendError(listener, e);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }
}
//                    final JSONArray items = Objects.requireNonNull(response.optJSONArray("categories"));
//
//                    final ArrayMap<String, String> idsTitles = new ArrayMap<>();
//
//                    for (int i = 0; i < items.length(); i++) {
//                        JSONObject category = Objects.requireNonNull(items.optJSONObject(i));
//
//                        String id = category.optString("id");
//                        String title = category.optString("title");
//
//                        idsTitles.put(id, title);
//                    }
//
//                    RequestBuilder.create()
//                            .method(prefs.getString(FragmentSettings.KEY_TOPICS_KEY, ""))
//                            .put("category", idsTitles.keySet().toArray())
//                            .execute(new RequestBuilder.OnResponseListener<JSONObject>() {
//                                @Override
//                                public void onSuccess(JSONObject root) {
//                                    JSONObject response = Objects.requireNonNull(root.optJSONObject("response"));
//                                    JSONArray jTopics = Objects.requireNonNull(response.optJSONArray("topics"));
//
//                                    ArrayList<Headline> headlines = new ArrayList<>();
//
//
//                                    for (int i = 0; i < items.length(); i++) {
//                                        JSONObject topic = jTopics.optJSONObject(i);
//
//                                        ArrayList<Topic> topics = new ArrayList<>();
//
//                                        if (topic != null) {
//                                            String categoryId = topic.optString("categoryId");
//
//                                            JSONArray topicsItems = Objects.requireNonNull(topic.optJSONArray("items"));
//
//                                            for (int j = 0; j < topicsItems.length(); j++) {
//                                                topics.add(new Topic(topicsItems.optJSONObject(j)));
//                                            }
//                                        }
//
//                                        Headline headline = new Headline();
//
//                                        headline.id = idsTitles.keyAt(i);
//                                        headline.title = idsTitles.valueAt(i);
//                                        headline.topics = topics;
//
//                                        headlines.add(headline);
//                                    }
//
//                                    sendValues(fields, headlines, listener);
//                                }
//
//                                @Override
//                                public void onError(Exception e) {
//                                    sendError(listener, e);
//                                }
//                            });
//                } catch (Exception e) {
//                    sendError(listener, e);
//                }
//
//                try {
//                    JSONObject response = Objects.requireNonNull(root.optJSONObject("response"));
////                        JSONArray categories = Objects.requireNonNull(response.optJSONArray("categories"));
////                        JSONArray topics = Objects.requireNonNull(response.optJSONArray("topics"));
//
//                    ArrayList<Headline> headlines = new ArrayList<>();
//
////                        for (int i = 0; i < categories.length(); i++) {
////                            JSONObject category = categories.optJSONObject(i);
////
////                            String id = category.optString("id");
////                            String title = category.optString("title");
////
////                            ArrayList<Topic> mTopics = new ArrayList<>();
////
////                            JSONObject topic = topics.optJSONObject(i);
////                            if (topic != null) {
////                                JSONArray oTopics = Objects.requireNonNull(topic.optJSONArray("topics"));
////
////                                for (int j = 0; j < oTopics.length(); j++) {
////                                    mTopics.add(new Topic(oTopics.optJSONObject(j)));
////                                }
////                            }
////
////                            Headline headline = new Headline();
////                            headline.id = id;
////                            headline.title = title;
////                            headline.topics = mTopics;
////
////                            headlines.add(headline);
////                        }
//
//                    sendValues(fields, headlines, listener);
