package com.feedhub.app.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.feedhub.app.R;
import com.feedhub.app.activity.MainActivity;
import com.feedhub.app.adapter.NewsAdapter;
import com.feedhub.app.common.AppGlobal;
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

    private LinearLayoutManager layoutManager;

    @Nullable
    private NewsAdapter adapter;

    @NonNull
    private NewsPresenter presenter;

    private Toolbar toolbar;
    private Window window;

    private boolean isToolbarAnimating;

    public FragmentGeneral() {
        presenter = new NewsPresenter(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = ((MainActivity) requireActivity()).toolbar;
        window = requireActivity().getWindow();
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int firstPosition = layoutManager.findFirstVisibleItemPosition();

                if (firstPosition > 0) {
                    if (toolbar.getAlpha() == 0.85 || isToolbarAnimating) return;

                    isToolbarAnimating = true;

                    toolbar.animate().alpha(0.85f).setDuration(150).withEndAction(() -> isToolbarAnimating = false).start();
                } else {
                    if (toolbar.getAlpha() == 1 || isToolbarAnimating) return;

                    isToolbarAnimating = true;

                    toolbar.animate().alpha(1f).setDuration(150).withEndAction(() -> isToolbarAnimating = false).start();
                }
            }
        });
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
        refreshLayout.setColorSchemeColors(AppGlobal.colorAccent);
        refreshLayout.setProgressViewOffset(true, AndroidUtils.px(36), AndroidUtils.px(46));
        refreshLayout.setProgressViewEndTarget(true, AndroidUtils.px(66));
        refreshLayout.setOnRefreshListener(this);
    }

    private void prepareRecyclerView() {
        layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
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
        if (e != null) {
            Toast.makeText(requireContext(), getString(R.string.cause_exception, e.toString()), Toast.LENGTH_SHORT).show();
        }
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
        if (getContext() == null) return;

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

        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(adapter);
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
