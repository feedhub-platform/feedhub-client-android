package com.feedhub.app.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.feedhub.app.R;
import com.feedhub.app.adapter.NewsAdapter;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.common.TaskManager;
import com.feedhub.app.dao.SearchesDao;
import com.feedhub.app.fragment.FragmentNews;
import com.feedhub.app.item.News;
import com.feedhub.app.item.Search;
import com.feedhub.app.net.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.searchView)
    SearchView searchView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private ArrayList<News> items;
    private NewsAdapter adapter;

    private SearchesDao searchesDao = AppGlobal.database.searchesDao();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initExtraData();
        prepareRefreshLayout();
        prepareSearchView();

        createAdapter();
    }

    private void initExtraData() {
        items = (ArrayList<News>) getIntent().getSerializableExtra("items");
    }

    private void prepareSearchView() {
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter == null) return false;

                adapter.filter(newText.toLowerCase());

                if (adapter.isEmpty()) {
                    showSearchHistory();
                }
                return true;
            }
        });
    }

    private void prepareRefreshLayout() {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        );
    }

    private void createAdapter() {
        adapter = new NewsAdapter(this, new ArrayList<>(items));
        adapter.setOnMoreClickListener((view, position) -> FragmentNews.showMoreItems(adapter, position, SearchActivity.this, view));
        adapter.setOnItemClickListener(position -> FragmentNews.openNewsPost(adapter, position, SearchActivity.this));
        recyclerView.setAdapter(adapter);
    }

    private void search(@NonNull String query) {
        TaskManager.execute(() -> {
            try {
                searchesDao.insert(new Search(query));

                List<Search> searches = searchesDao.getAll();

                if (searches.isEmpty()) return;

                if (searches.size() > 5) {
                    searches = searches.subList(searches.size() - 5, searches.size());
                    searchesDao.clear();
                    searchesDao.insert(searches);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        RequestBuilder.create()
                .method("news/search")
                .put("query", query.toLowerCase())
                .put("limit", 30)
                .execute(new RequestBuilder.OnResponseListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject root) {
                        TaskManager.execute(() -> {
                            try {
                                JSONObject response = Objects.requireNonNull(root.optJSONObject("response"));
                                JSONArray results = Objects.requireNonNull(response.optJSONArray("results"));

                                ArrayList<News> news = new ArrayList<News>(News.parse(results));

                                runOnUiThread(() -> updateList(news));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void updateList(@NonNull ArrayList<News> values) {
        if (values.isEmpty()) {
            showSearchHistory();
            return;
        }

        adapter.changeItems(new ArrayList<>(values));
        adapter.notifyDataSetChanged();
    }

    private void showSearchHistory() {
        TaskManager.execute(() -> {
            try {
                List<Search> searches = searchesDao.getAll();
                Collections.reverse(searches);

                runOnUiThread(() -> {
                    adapter.clear();
                    adapter.getValues().addAll(searches);
                    adapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
