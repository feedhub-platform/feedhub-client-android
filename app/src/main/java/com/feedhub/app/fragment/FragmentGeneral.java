package com.feedhub.app.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.feedhub.app.R;
import com.feedhub.app.activity.MainActivity;
import com.feedhub.app.activity.SettingsActivity;
import com.feedhub.app.adapter.NewsAdapter;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.current.BaseFragment;
import com.feedhub.app.item.News;
import com.feedhub.app.mvp.contract.BaseContract;
import com.feedhub.app.mvp.presenter.NewsPresenter;
import com.feedhub.app.mvp.view.NewsView;
import com.feedhub.app.util.AndroidUtils;
import com.feedhub.app.util.ColorUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.library.mvp.base.MvpConstants;
import ru.melod1n.library.mvp.base.MvpException;
import ru.melod1n.library.mvp.base.MvpFields;

public class FragmentGeneral extends BaseFragment implements NewsView, SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnItemClickListener {

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
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        prepareToolbar();
        prepareRefreshLayout();
        prepareRecyclerView();

        loadCachedValues();

        if (AndroidUtils.hasConnection()) {
            loadValues();
        }
    }

    private void prepareToolbar() {
        initToolbar(R.id.toolbar);

        toolbar.setNavigationClickListener(v -> {
        });
        toolbar.setNavigationIcon(R.drawable.ic_search);
        toolbar.setTitle(R.string.app_name);
        toolbar.setAvatarClickListener(getAvatarClickListener());
    }

    private View.OnClickListener getAvatarClickListener() {
        return v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

            String[] items = new String[]{
                    getString(R.string.navigation_settings)
            };

            builder.setItems(items, (dialog, which) -> {
                switch (which) {
                    case 0:
                        openSettingsScreen();
                        break;
                }
            });

            builder.create().show();
        };
    }

    private void openSettingsScreen() {
        startActivity(new Intent(requireContext(), SettingsActivity.class));
    }

    private void loadCachedValues() {
        presenter.requestCachedData(
                new MvpFields()
                        .put(MvpConstants.OFFSET, 0)
                        .put(MvpConstants.COUNT, NEWS_COUNT)
                        .put(MvpConstants.FROM_CACHE, true)
        );
    }

    private void loadValues() {
        if (AndroidUtils.hasConnection()) {
            if (adapter != null && !adapter.isEmpty()) {
                presenter.prepareForLoading();
            } else {
                startRefreshing();
            }

            presenter.requestLoadValues(
                    new MvpFields()
                            .put(MvpConstants.OFFSET, 0)
                            .put(MvpConstants.COUNT, NEWS_COUNT)
                            .put(MvpConstants.FROM_CACHE, false)
            );
        } else {
            showNoInternetView();
            stopRefreshing();
        }
    }

    private void prepareRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.accent);
        refreshLayout.setOnRefreshListener(this);
    }

    private void prepareRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));
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
    public void prepareNoInternetView() {

    }

    @Override
    public void prepareNoItemsView() {

    }

    @Override
    public void prepareErrorView() {

    }

    @Override
    public void showNoInternetView() {

    }

    @Override
    public void hideNoInternetView() {

    }

    @Override
    public void showNoItemsView() {

    }

    @Override
    public void hideNoItemsView() {

    }


    @Override
    public void showErrorView(@Nullable Exception e) {
        if (e != null) {
            if (e instanceof MvpException) {
                if (((MvpException) e).errorId.equals(MvpException.ERROR_EMPTY)) return;
            }

            Toast.makeText(requireContext(), getString(R.string.cause_exception, e.toString()), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void hideErrorView() {

    }

    @Override
    public void startRefreshing() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void stopRefreshing() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void insertValues(@NonNull MvpFields fields, @NonNull ArrayList<News> values) {
        if (getContext() == null) return;

        int offset = fields.getInt(MvpConstants.OFFSET);

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
