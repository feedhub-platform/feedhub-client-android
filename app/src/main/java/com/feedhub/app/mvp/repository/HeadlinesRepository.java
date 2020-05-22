package com.feedhub.app.mvp.repository;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.dao.HeadlinesDao;
import com.feedhub.app.fragment.FragmentSettings;
import com.feedhub.app.item.Headline;
import com.feedhub.app.item.Topic;
import com.feedhub.app.net.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import ru.melod1n.library.mvp.base.MvpFields;
import ru.melod1n.library.mvp.base.MvpOnLoadListener;
import ru.melod1n.library.mvp.base.MvpRepository;

public class HeadlinesRepository extends MvpRepository<Headline> {

    private HeadlinesDao headlinesDao = AppGlobal.database.headlinesDao();

//    private void loadTopics() {
//        String serverUrl = AppGlobal.preferences.getString(FragmentSettings.KEY_SERVER_URL, "") + "/news/topics?category=" + category;
//
//        if (serverUrl.trim().isEmpty()) return;
//
//        TaskManager.execute(() -> {
//            try {
//                JSONObject root = new JSONObject(HttpRequest.get(serverUrl).asString());
//                JSONObject response = Objects.requireNonNull(root.optJSONObject("response"));
//                JSONArray items = (response.optJSONArray("items"));
//                JSONArray topics = Objects.requireNonNull(response.optJSONArray("topics"));
//
//                final ArrayList<Headline> news = new ArrayList<>();
//
//                for (int i = 0; i < topics.length(); i++) {
//                    news.add(new Headline(topics.optJSONObject(i)));
//                }
//
//                cacheValues(0, news.size(), news);
//                sendValues(listener, news);
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                sendError(listener, e);
//            }
//        });
//    }

    @Override
    public void loadValues(@NonNull MvpFields fields, @Nullable MvpOnLoadListener<Headline> listener) {
//        final List<MvpFields> data = new ArrayList<>();

        String sourceId = fields.get("sourceId");

        RequestBuilder builder = RequestBuilder.create();
//      builder.baseUrl(prefs.getString(FragmentSettings.KEY_SERVER_URL, ""))
        builder.method(prefs.getString(FragmentSettings.KEY_CATEGORY_KEY, ""));

        if (sourceId != null) {
            builder.method("platform/sources/" + sourceId);
            builder.execute(new RequestBuilder.OnResponseListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject root) {
                    Log.d("RESPONSE", root.toString());

                    JSONObject response = Objects.requireNonNull(root.optJSONObject("response"));
                    JSONArray categories = Objects.requireNonNull(response.optJSONArray("categories"));
                    JSONArray categoriesTopics = Objects.requireNonNull(response.optJSONArray("topics"));

                    ArrayMap<String, String> idsTitles = new ArrayMap<>();
                    ArrayList<Headline> headlines = new ArrayList<>();

                    for (int i = 0; i < categories.length(); i++) {
//                        JSONObject category = Objects.requireNonNull(categories.optJSONObject(i));

                        String id = categories.optString(i);
//                        String id = category.optString("id");
                        String title = id.substring(0, 1).toUpperCase() + id.substring(1);
//                        String title = category.optString("title");

                        idsTitles.put(id, title);

                        JSONObject categoryTopic = Objects.requireNonNull(categoriesTopics.optJSONObject(i));
                        JSONArray jTopics = Objects.requireNonNull(categoryTopic.optJSONArray("topics"));

                        ArrayList<Topic> topics = new ArrayList<>();

                        for (int j = 0; j < jTopics.length(); j++) {
                            topics.add(new Topic(jTopics.optJSONObject(j)));
                        }

                        Headline headline = new Headline();
                        headline.id = idsTitles.keyAt(i);
                        headline.title = idsTitles.valueAt(i);
                        headline.topics = topics;

                        headlines.add(headline);
                    }

                    sendValuesToPresenter(fields, headlines, listener);
                }

                @Override
                public void onError(Exception e) {
                    sendError(listener, e);
                }
            });
            return;
        }

        builder.execute(new RequestBuilder.OnResponseListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject root) {
                Log.d("RESPONSE", root.toString());

                JSONObject response = Objects.requireNonNull(root.optJSONObject("response"));
                final JSONArray items = Objects.requireNonNull(response.optJSONArray("categories"));

                final ArrayMap<String, String> idsTitles = new ArrayMap<>();

                for (int i = 0; i < items.length(); i++) {
                    JSONObject category = Objects.requireNonNull(items.optJSONObject(i));

                    String id = category.optString("id");
                    String title = category.optString("title");

                    idsTitles.put(id, title);
                }

                RequestBuilder.create()
//                .baseUrl(prefs.getString(FragmentSettings.KEY_SERVER_URL, ""))
                        .method(prefs.getString(FragmentSettings.KEY_TOPICS_KEY, ""))
                        .put("category", idsTitles.keySet().toArray())
                        .execute(new RequestBuilder.OnResponseListener<JSONObject>() {
                            @Override
                            public void onSuccess(JSONObject root) {
                                JSONObject response = Objects.requireNonNull(root.optJSONObject("response"));
                                JSONArray jTopics = Objects.requireNonNull(response.optJSONArray("topics"));

                                ArrayList<Headline> headlines = new ArrayList<>();

//                                        Object[] ids = idsTitles.keySet().toArray();
//                                        List<String> values = (List<String>) idsTitles.values();

                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject topic = jTopics.optJSONObject(i);

                                    ArrayList<Topic> topics = new ArrayList<>();

                                    if (topic != null) {
                                        String categoryId = topic.optString("categoryId");

                                        JSONArray topicsItems = Objects.requireNonNull(topic.optJSONArray("items"));

                                        for (int j = 0; j < topicsItems.length(); j++) {
                                            topics.add(new Topic(topicsItems.optJSONObject(j)));
                                        }
                                    }
//
//                                            MvpFields field = new MvpFields()
//                                                    .put("id", idsTitles.keyAt(i))
//                                                    .put("title", idsTitles.valueAt(i))
//                                                    .put("topics", topics.size());
//
//                                            data.add(field);

                                    Headline headline = new Headline();
//                                            headline.id = (String) ids[i];
//                                            headline.title = values.get(i);

                                    headline.id = idsTitles.keyAt(i);
                                    headline.title = idsTitles.valueAt(i);
                                    headline.topics = topics;

                                    headlines.add(headline);
                                }

                                sendValuesToPresenter(fields, headlines, listener);
                            }

                            @Override
                            public void onError(Exception e) {
                                sendError(listener, e);
                            }
                        });
            }

            @Override
            public void onError(Exception e) {
                sendError(listener, e);
            }
        });

//        String serverUrl =
//                AppGlobal.preferences.getString(FragmentSettings.KEY_SERVER_URL, "") + "/" +
//                        AppGlobal.preferences.getString(FragmentSettings.KEY_NEWS_KEY, "") + "/category=" + category;
//
//        TaskManager.execute(() -> {
//            try {
//                JSONObject root = new JSONObject(HttpRequest.get(serverUrl).asString());
//
//                int i = 0;
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                sendError(listener, e);
//            }
//        });
    }
}
