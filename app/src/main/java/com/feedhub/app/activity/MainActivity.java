package com.feedhub.app.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.feedhub.app.R;
import com.feedhub.app.adapter.NewsAdapter;
import com.feedhub.app.common.AppDatabase;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.current.BaseActivity;
import com.feedhub.app.dao.NewsDao;
import com.feedhub.app.item.News;
import com.feedhub.app.net.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnItemClickListener {

    private static final String FILE_URL = "https://melod1n.do.am/data.json";

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        prepareToolbar();
        prepareRefreshLayout();
        prepareRecyclerView();

        createTestList(false);
    }

    private void prepareToolbar() {
        setSupportActionBar(toolbar);
    }

    private void prepareRefreshLayout() {
        refreshLayout.setOnRefreshListener(this);
    }

    private void prepareRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
    }

    private void createTestList(boolean fromRefresh) {
        AppDatabase database = AppGlobal.database;
        NewsDao newsDao = database.newsDao();

//        AtomicReference<ArrayList<News>> values = new AtomicReference<>(new ArrayList<>());

        final Random random = new Random();

        new Thread(() -> {
            try {
                JSONObject root = new JSONObject(HttpRequest.get("https://d62ebdf3.eu.ngrok.io/news").asString());

                JSONObject response = root.optJSONObject("response");
                JSONArray items = response.optJSONArray("items");

//                JSONArray pictures = root.optJSONArray("pictures");
//                JSONArray titles = root.optJSONArray("titles");
//                JSONArray bodies = root.optJSONArray("bodies");

                final ArrayList<News> news = new ArrayList<>();

                for (int i = 0; i < items.length(); i++) {
                    news.add(new News(items.optJSONObject(i)));
//                    News n = new News();
//                    n.id = i;
//                    n.picture = pictures.optString(random.nextInt(4));
//                    n.title = titles.optString(random.nextInt(4));
//                    n.body = bodies.optString(random.nextInt(4));

//                    news.add(n);
                }

                runOnUiThread(() -> {
                    createAdapter(news);

                    refreshLayout.setRefreshing(false);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
//            values.set(new ArrayList<>(newsDao.getAll()));

//            if (values.get().isEmpty() || fromRefresh) {
//                if (!values.get().isEmpty()) {
//                    newsDao.clear();
//                    values.get().clear();
//                }
//
//                for (int i = 0; i < random.nextInt(100); i++) {
//                    News news = new News("Title " + (i + 1), "Body " + (i + 1), null);
//                    news.id = i;
//
//                    values.get().add(news);
//                    newsDao.insert(news);
//                }
//            }
//
//            runOnUiThread(() -> {
//                createAdapter(values.get());
//
//                if (refreshLayout.isRefreshing())
//                    refreshLayout.setRefreshing(false);
//            });
        }).start();
    }

    private boolean contains(ArrayList<News> array, News news) {
        for (News n : array) {
            if (n.id == news.id) return true;
        }

        return false;
    }

    private void createAdapter(ArrayList<News> values) {
        if (adapter == null) {
            adapter = new NewsAdapter(this, values);
            adapter.setOnItemClickListener(this);

            recyclerView.setAdapter(adapter);
            return;
        }

        adapter.changeItems(values);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        createTestList(true);
    }

    @Override
    public void onItemClick(int position) {

    }
}
