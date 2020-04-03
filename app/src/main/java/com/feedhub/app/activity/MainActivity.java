package com.feedhub.app.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.feedhub.app.R;
import com.feedhub.app.adapter.NewsAdapter;
import com.feedhub.app.common.AppDatabase;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.dao.NewsDao;
import com.feedhub.app.item.News;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnItemClickListener {

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

        AtomicReference<ArrayList<News>> values = new AtomicReference<>(new ArrayList<>());

        new Thread(() -> {
            values.set(new ArrayList<>(newsDao.getAll()));

            Random random = new Random();

            if (values.get().isEmpty() || fromRefresh) {
                if (!values.get().isEmpty()) {
                    newsDao.clear();
                    values.get().clear();
                }

                for (int i = 0; i < random.nextInt(100); i++) {
                    News news = new News("Title " + (i + 1), "Body " + (i + 1), null);
                    news.id = i;

                    values.get().add(news);
                    newsDao.insert(news);
                }
            }

            runOnUiThread(() -> {
                createAdapter(values.get());

                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
            });
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
