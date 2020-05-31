package com.feedhub.app.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.feedhub.app.R;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.library.mvp.base.MvpConstants;
import ru.melod1n.library.mvp.base.MvpFields;

public class FragmentNews extends BaseFragment implements NewsView, SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnItemClickListener {

    private static final int NEWS_COUNT = 10;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
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

    private void showMoreItems(View view, int position) {
        if (adapter == null) return;

        News news = adapter.getItem(position);

        TaskManager.execute(() -> {
            boolean alreadyIn = false;


            List<Favorite> favorites = favoritesDao.getAll();

            for (Favorite favorite : favorites) {
                if (news.id.equals(favorite.id)) {
                    alreadyIn = true;
                    break;
                }
            }

            boolean finalAlreadyIn = alreadyIn;

            runOnUiThread(() -> {
                PopupMenu popupMenu = new PopupMenu(requireContext(), view);
                popupMenu.inflate(R.menu.fragment_news_more_popup);

                if (finalAlreadyIn) {
                    popupMenu.getMenu().findItem(R.id.moreAddToFavorites).setTitle(R.string.remove_from_favorites);
                }

                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.moreAddToFavorites:
                            TaskManager.execute(() -> {
                                try {
                                    Favorite favorite = new Favorite(news);

                                    if (finalAlreadyIn) {
                                        favoritesDao.delete(favorite);
                                    } else {

                                        favoritesDao.insert(favorite);
                                    }


                                    runOnUiThread(() -> Toast.makeText(
                                            requireContext(),
                                            finalAlreadyIn ? R.string.removed_from_favorites : R.string.added_to_favorites,
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

    @Override
    public void onRefresh() {
        loadValues();
    }

    @Override
    public void onItemClick(int position) {
        if (adapter == null) return;

        News news = adapter.getItem(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage(R.string.open_post_confirmation);
        builder.setPositiveButton(R.string.dialog_yes, (dialog, which) -> {

            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .addDefaultShareMenuItem()
                    .setShowTitle(true)
                    .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
                    .build();
            
            customTabsIntent.launchUrl(requireContext(), Uri.parse(news.originUrl));

        });
        builder.setNegativeButton(R.string.dialog_no, null);
        builder.show();
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
            adapter = new NewsAdapter(requireContext(), values);
            adapter.setOnItemClickListener(this);
            adapter.setOnMoreClickListener(this::showMoreItems);

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
