package com.feedhub.app.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.feedhub.app.R;
import com.feedhub.app.adapter.NewsAdapter;
import com.feedhub.app.current.BaseFragment;
import com.feedhub.app.item.News;
import com.feedhub.app.mvp.contract.BaseContract;
import com.feedhub.app.mvp.presenter.NewsPresenter;
import com.feedhub.app.util.AndroidUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentGeneral extends BaseFragment implements BaseContract.View<News>, SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnItemClickListener {

    private static final int NEWS_COUNT = 10;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Nullable
    private NewsAdapter adapter;

    @NonNull
    private NewsPresenter presenter;

    public FragmentGeneral() {
        presenter = new NewsPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_general, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        prepareRefreshLayout();
        prepareRecyclerView();

        loadCachedValues();

        if (AndroidUtils.hasConnection()) {
            loadValues();
        }
    }

    private void loadCachedValues() {
        presenter.requestCachedValues(0, NEWS_COUNT);
    }

    private void loadValues() {
        if (AndroidUtils.hasConnection()) {
            if (adapter != null && !adapter.isEmpty()) {
                presenter.prepareForLoading();
            } else {
                showRefreshLayout(true);
            }

            presenter.requestValues(0, NEWS_COUNT);
        } else {
            showNoInternetView(true);
            showRefreshLayout(false);
        }
    }

    private void prepareRefreshLayout() {
        refreshLayout.setOnRefreshListener(this);
    }

    private void prepareRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void onRefresh() {
        loadValues();
    }

    @Override
    public void onItemClick(int position) {
        if (adapter == null) return;

        News news = adapter.getItem(position);

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .addDefaultShareMenuItem()
                .setToolbarColor(ContextCompat.getColor(requireContext(), R.color.primary))
                .setShowTitle(true)
                .build();

        customTabsIntent.launchUrl(requireContext(), Uri.parse(news.originUrl));
    }

    @Override
    public void showNoItemsView(boolean visible) {

    }

    @Override
    public void showNoInternetView(boolean visible) {

    }

    @Override
    public void showErrorView(@Nullable Exception e) {

    }

    @Override
    public void showRefreshLayout(boolean visible) {
        refreshLayout.setRefreshing(visible);
    }

    @Override
    public void showProgressBar(boolean visible) {

    }

    @Override
    public void insertValues(int offset, int count, ArrayList<News> values, boolean isCache) {
        if (adapter == null) {
            adapter = new NewsAdapter(requireContext(), values);
            adapter.setOnItemClickListener(this);

            recyclerView.setAdapter(adapter);
            return;
        }

        if (offset > 0) {
            adapter.addAll(values);
            return;
        }

        adapter.changeItems(values);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearList() {
        if (adapter != null) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
    }
}
