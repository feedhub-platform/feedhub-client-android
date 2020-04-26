package com.feedhub.app.mvp.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.common.AppDatabase;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.common.TaskManager;
import com.feedhub.app.dao.HeadlinesDao;
import com.feedhub.app.fragment.FragmentSettings;
import com.feedhub.app.item.Headline;
import com.feedhub.app.item.News;
import com.feedhub.app.mvp.contract.BaseContract;
import com.feedhub.app.net.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class HeadlinesRepository extends BaseContract.Repository<Headline> {

    private HeadlinesDao headlinesDao = AppGlobal.database.headlinesDao();

    private String category;

    public void loadValues(String category, int offset, int count, @Nullable BaseContract.OnValuesLoadListener<Headline> listener) {
        this.category = category;
        loadValues(offset, count, listener);
    }

    @Override
    public void loadValues(int offset, int count, @Nullable BaseContract.OnValuesLoadListener<Headline> listener) {
//        loadTopics();
    }

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

    @NonNull
    @Override
    public ArrayList<Headline> loadCachedValues(int offset, int count) {
        return null;
    }

    @Override
    public void cacheValues(int offset, int count, @NonNull ArrayList<Headline> values) {

    }
}
