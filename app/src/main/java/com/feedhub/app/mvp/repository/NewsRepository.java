package com.feedhub.app.mvp.repository;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.common.TaskManager;
import com.feedhub.app.dao.NewsDao;
import com.feedhub.app.fragment.FragmentSettings;
import com.feedhub.app.item.News;
import com.feedhub.app.net.HttpRequest;
import com.feedhub.app.util.ArrayUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.melod1n.library.mvp.base.MvpConstants;
import ru.melod1n.library.mvp.base.MvpException;
import ru.melod1n.library.mvp.base.MvpFields;
import ru.melod1n.library.mvp.base.OnLoadListener;
import ru.melod1n.library.mvp.base.MvpRepository;

public class NewsRepository extends MvpRepository<News> {

    private NewsDao newsDao = AppGlobal.database.newsDao();

    @Override
    public void loadValues(@NonNull MvpFields fields, @Nullable OnLoadListener<News> listener) {
        String serverUrl =
                AppGlobal.preferences.getString(FragmentSettings.KEY_SERVER_URL, "") + "/" +
                        AppGlobal.preferences.getString(FragmentSettings.KEY_NEWS_KEY, "");

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

                cacheLoadedValues(news);
                sendValuesToPresenter(fields, news, listener);
            } catch (Exception e) {
                e.printStackTrace();

                sendError(listener, e);
            }
        });
    }

    @Override
    public void loadCachedValues(@NonNull MvpFields fields, @Nullable OnLoadListener<News> listener) {
        int offset = fields.getInt(MvpConstants.OFFSET);
        int count = fields.getInt(MvpConstants.COUNT);

        startNewThread(() -> {
            try {
                ArrayList<News> cachedValues = new ArrayList<>(newsDao.getAll());
                ArrayUtils.prepareList(cachedValues, offset, count);

                post(() -> {
                    if (cachedValues.isEmpty()) {
                        sendError(listener, MvpException.ERROR_EMPTY);
                    } else {
                        sendValuesToPresenter(fields, cachedValues, listener);
                    }
                });
            } catch (Exception e) {
                post(() -> sendError(listener, e));
            }
        });
    }

    @Override
    protected void cacheLoadedValues(@NonNull ArrayList<News> values) {
        startNewThread(() -> {
            List<News> cachedValues = newsDao.getAll();

            if (cachedValues.isEmpty()) {
                for (News news : values) {
                    newsDao.insert(news);
                }
            } else {
                for (News news : values) {
                    newsDao.insert(news);
                }
            }
        });
    }
}
