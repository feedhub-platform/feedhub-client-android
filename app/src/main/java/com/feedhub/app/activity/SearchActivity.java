package com.feedhub.app.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.feedhub.app.R;
import com.feedhub.app.adapter.NewsAdapter;
import com.feedhub.app.fragment.FragmentNews;
import com.feedhub.app.item.News;

import java.util.ArrayList;

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
                return false;
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
}
