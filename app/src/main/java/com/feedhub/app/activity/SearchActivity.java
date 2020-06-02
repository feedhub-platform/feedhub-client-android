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
import com.feedhub.app.fragment.FragmentNews;
import com.feedhub.app.item.News;
import com.feedhub.app.net.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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
        adapter = new NewsAdapter(this, items);
        adapter.setOnMoreClickListener((view, position) -> FragmentNews.showMoreItems(adapter, position, SearchActivity.this, view));
        adapter.setOnItemClickListener(position -> FragmentNews.openNewsPost(adapter, position, SearchActivity.this));
        recyclerView.setAdapter(adapter);
    }

    private void search(@NonNull String query) {
        RequestBuilder.create()
                .method("news/search")
                .put("query", query.toLowerCase())
                .put("limit", 30)
                .execute(new RequestBuilder.OnResponseListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject root) {
                        try {
                            JSONObject response = Objects.requireNonNull(root.optJSONObject("response"));
                            JSONArray results = Objects.requireNonNull(response.optJSONArray("results"));

                            ArrayList<News> news = new ArrayList<>(News.parse(results));
                            runOnUiThread(() -> {
                                adapter.changeItems(news);
                                adapter.notifyDataSetChanged();
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
