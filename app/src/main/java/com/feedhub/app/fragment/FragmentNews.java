package com.feedhub.app.fragment;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.feedhub.app.R;
import com.feedhub.app.activity.SearchActivity;
import com.feedhub.app.activity.TaskActivity;
import com.feedhub.app.adapter.NewsAdapter;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.common.TaskManager;
import com.feedhub.app.current.BaseFragment;
import com.feedhub.app.dao.FavoritesDao;
import com.feedhub.app.dialog.ProfileBottomSheetDialog;
import com.feedhub.app.item.Favorite;
import com.feedhub.app.item.News;
import com.feedhub.app.mvp.presenter.NewsPresenter;
import com.feedhub.app.mvp.view.NewsView;
import com.feedhub.app.util.AndroidUtils;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.library.mvp.base.MvpConstants;
import ru.melod1n.library.mvp.base.MvpFields;
import ru.melod1n.library.recyclerview.EndlessScrollListener;

public class FragmentNews extends BaseFragment implements NewsView, SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnItemClickListener {

    public static final int REQUEST_ADD_TO_FAVORITES = 1;
    private static final int NEWS_COUNT = 30;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Nullable
    private NewsAdapter adapter;

    @NonNull
    private NewsPresenter presenter;

    private FavoritesDao favoritesDao = AppGlobal.database.favoritesDao();

    public FragmentNews() {
        presenter = new NewsPresenter(this);
    }

    public static void openNewsPost(@NonNull NewsAdapter adapter, int position, @NonNull Activity activity) {
        FavoritesDao favoritesDao = AppGlobal.database.favoritesDao();

        News news = (News) adapter.getItem(position);

        TaskManager.execute(() -> {
            boolean contains = false;

            List<Favorite> favorites = favoritesDao.getAll();

            for (Favorite favorite : favorites) {
                if (news.id.equals(favorite.id)) {
                    contains = true;
                    break;
                }
            }

            boolean finalContains = contains;

            activity.runOnUiThread(() -> {
                String label = "FeedHub: ";

                label += activity.getString(finalContains ? R.string.remove_from_favorites : R.string.chrome_tabs_add_to_favorites);

                Intent intent = new Intent(activity, TaskActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("news", news);
                intent.putExtra("add", !finalContains);

                PendingIntent pendingIntent = PendingIntent.getActivity(
                        activity,
                        REQUEST_ADD_TO_FAVORITES,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                );

                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                        .addDefaultShareMenuItem()
                        .setShowTitle(true)
                        .addMenuItem(label, pendingIntent)
                        .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
                        .build();

                customTabsIntent.launchUrl(activity, Uri.parse(news.originUrl));
            });
        });
    }

    public static void showMoreItems(@NonNull NewsAdapter adapter, int position, @NonNull Activity activity, @NonNull View view) {
        FavoritesDao favoritesDao = AppGlobal.database.favoritesDao();

        News news = (News) adapter.getItem(position);

        TaskManager.execute(() -> {
            boolean contains = false;

            List<Favorite> favorites = favoritesDao.getAll();

            for (Favorite favorite : favorites) {
                if (news.id.equals(favorite.id)) {
                    contains = true;
                    break;
                }
            }

            boolean finalContains = contains;

            activity.runOnUiThread(() -> {
                PopupMenu popupMenu = new PopupMenu(activity, view);
                popupMenu.inflate(R.menu.fragment_news_more_popup);

                if (finalContains) {
                    popupMenu.getMenu().findItem(R.id.moreAddToFavorites).setTitle(R.string.remove_from_favorites);
                }

                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.moreAddToFavorites:
                            TaskManager.execute(() -> {
                                try {
                                    Favorite favorite = new Favorite(news);

                                    if (finalContains) {
                                        favoritesDao.delete(favorite);
                                    } else {

                                        favoritesDao.insert(favorite);
                                    }

                                    activity.runOnUiThread(() -> Toast.makeText(
                                            activity,
                                            finalContains ? R.string.removed_from_favorites : R.string.added_to_favorites,
                                            Toast.LENGTH_SHORT
                                    ).show());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                            return true;
                    }

                    return false;
                });

                popupMenu.show();
            });
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setRecyclerView(recyclerView);

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
            if (adapter == null) return;

            Intent intent = new Intent(requireContext(), SearchActivity.class);
            intent.putExtra("items", adapter.getValues());

            startActivity(intent);
        });
        toolbar.setNavigationIcon(R.drawable.ic_search);
        toolbar.setTitle(R.string.app_name);
        toolbar.setAvatarClickListener(getAvatarClickListener());
    }

    @NonNull
    @Contract(pure = true)
    private View.OnClickListener getAvatarClickListener() {
        return v -> ProfileBottomSheetDialog.show(getParentFragmentManager());
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

    private void initScrollListener(@NonNull NewsAdapter adapter) {
        recyclerView.addOnScrollListener(new EndlessScrollListener<NewsAdapter>(adapter) {

            @Override
            public void onLoadMore() {
                adapter.add(null);

                presenter.requestLoadValues(new MvpFields()
                        .put(MvpConstants.FROM_CACHE, false)
                        .put(MvpConstants.COUNT, 30)
                        .put(MvpConstants.OFFSET, adapter.getItemCount() - 1));
            }
        });
    }

    private void showMoreItems(View view, int position) {
        if (adapter == null) return;

        FragmentNews.showMoreItems(adapter, position, requireActivity(), view);
    }

    @Override
    public void onRefresh() {
        loadValues();
    }

    @Override
    public void onItemClick(int position) {
        if (adapter == null) return;

        FragmentNews.openNewsPost(adapter, position, requireActivity());
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
            if (getContext() != null)
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
            adapter = new NewsAdapter(requireContext(), new ArrayList<>(values));
            adapter.setOnItemClickListener(this);
            adapter.setOnMoreClickListener(this::showMoreItems);

            recyclerView.setAdapter(adapter);
            initScrollListener(adapter);
            return;
        }

        if (adapter.isLoading) {
            adapter.remove(null);
            adapter.isLoading = false;
        }

        if (offset > 0) {
            adapter.addAll(new ArrayList<>(values));
            adapter.notifyDataSetChanged();
            return;
        }

        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(adapter);
        }

        adapter.changeItems(new ArrayList<>(values));
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
