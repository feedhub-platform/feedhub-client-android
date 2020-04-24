package com.feedhub.app.mvp.repository;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.common.TaskManager;
import com.feedhub.app.dao.NewsDao;
import com.feedhub.app.fragment.FragmentSettings;
import com.feedhub.app.item.News;
import com.feedhub.app.mvp.contract.BaseContract;
import com.feedhub.app.net.HttpRequest;
import com.feedhub.app.util.ArrayUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewsRepository extends BaseContract.Repository<News> {

    private NewsDao newsDao = AppGlobal.database.newsDao();

    @Override
    public void loadValues(int offset, int count, @Nullable BaseContract.OnValuesLoadListener<News> listener) {
        String serverUrl = AppGlobal.preferences.getString(FragmentSettings.KEY_SERVER_URL, "");

        if (serverUrl.trim().isEmpty()) return;

        TaskManager.execute(() -> {
            try {
                JSONObject root = new JSONObject(HttpRequest.get(serverUrl).asString());
                JSONObject response = Objects.requireNonNull(root.optJSONObject("response"));
                JSONArray items = Objects.requireNonNull(response.optJSONArray("items"));

                final ArrayList<News> news = new ArrayList<>();

                for (int i = 0; i < items.length(); i++) {
                    news.add(new News(items.optJSONObject(i)));
                }

                cacheValues(offset, count, news);
                sendValues(listener, news);
            } catch (Exception e) {
                e.printStackTrace();

                sendError(listener, e);
            }
        });
    }

    @NonNull
    @Override
    public ArrayList<News> loadCachedValues(int offset, int count) {
        ArrayList<News> cachedValues = new ArrayList<>(newsDao.getAll());
        ArrayUtils.prepareList(cachedValues, offset, count);

        return cachedValues;
    }

    @Override
    public void cacheValues(int offset, int count, @NonNull ArrayList<News> values) {
        TaskManager.execute(() -> {
            List<News> cachedValues = newsDao.getAll();

            if (cachedValues.isEmpty()) {
                for (News news : values) {
                    newsDao.insert(news);
                }
            } else {
                if (offset == 0) {
                    newsDao.clear();
                }

                for (News news : values) {
                    newsDao.insert(news);
                }
            }
        });
    }
}
