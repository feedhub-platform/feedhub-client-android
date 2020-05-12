package com.feedhub.app.mvp.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.common.TaskManager;
import com.feedhub.app.dao.HeadlinesDao;
import com.feedhub.app.fragment.FragmentSettings;
import com.feedhub.app.item.Headline;
import com.feedhub.app.mvp.presenter.HeadlinesPresenter;
import com.feedhub.app.net.HttpRequest;

import org.json.JSONObject;

import java.util.ArrayList;

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
        String category = fields.getString(HeadlinesPresenter.CATEGORY);

        String serverUrl =
                AppGlobal.preferences.getString(FragmentSettings.KEY_SERVER_URL, "") + "/" +
                        AppGlobal.preferences.getString(FragmentSettings.KEY_NEWS_KEY, "") + "/category=" + category;

        TaskManager.execute(() -> {
            try {
                JSONObject root = new JSONObject(HttpRequest.get(serverUrl).asString());

                int i = 0;
            } catch (Exception e) {
                e.printStackTrace();

                sendError(listener, e);
            }
        });
    }

    @Override
    public void loadCachedValues(@NonNull MvpFields fields, @Nullable MvpOnLoadListener<Headline> listener) {

    }

    @Override
    protected void cacheLoadedValues(@NonNull ArrayList<Headline> values) {

    }
}
