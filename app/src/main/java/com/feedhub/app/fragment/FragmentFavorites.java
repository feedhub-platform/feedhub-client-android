package com.feedhub.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.feedhub.app.R;
import com.feedhub.app.adapter.FavoritesAdapter;
import com.feedhub.app.adapter.NewsAdapter;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.common.TaskManager;
import com.feedhub.app.current.BaseFragment;
import com.feedhub.app.dialog.ProfileBottomSheetDialog;
import com.feedhub.app.item.Favorite;
import com.feedhub.app.mvp.presenter.FavoritesPresenter;
import com.feedhub.app.mvp.view.FavoritesView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.library.mvp.base.MvpConstants;
import ru.melod1n.library.mvp.base.MvpFields;

public class FragmentFavorites extends BaseFragment implements FavoritesView, SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnItemClickListener {

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Nullable
    private FavoritesAdapter adapter;

    @NonNull
    private FavoritesPresenter presenter;

    public FragmentFavorites() {
        presenter = new FavoritesPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setRecyclerView(recyclerView);

        prepareToolbar();
        prepareRefreshLayout();
        prepareRecyclerView();

        loadData();
    }

    private void prepareRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
    }

    private void prepareRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.accent);
        refreshLayout.setOnRefreshListener(this);
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

    private void loadData() {
        presenter.requestCachedData(
                new MvpFields()
                        .put(MvpConstants.OFFSET, 0)
                        .put(MvpConstants.FROM_CACHE, true)
        );
    }

    private void removeFavorite(View view, int position) {
        if (adapter == null) return;

        Favorite favorite = adapter.getItem(position);

        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.inflate(R.menu.fragment_favorites_more_popup);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.moreDeleteFromFavorites:
                    TaskManager.execute(() -> {
                        try {
                            AppGlobal.database.favoritesDao().deleteById(favorite.id);
                            runOnUiThread(() -> {
                                Toast.makeText(requireContext(), R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();

                                if (adapter != null) {
                                    adapter.remove(position);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    return true;
            }

            return false;
        });

        popupMenu.show();


    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            loadData();
        }
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
    public void showErrorView(Exception e) {

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
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void insertValues(@NonNull MvpFields fields, @NonNull ArrayList<Favorite> values) {
        if (adapter == null) {
            adapter = new FavoritesAdapter(requireContext(), values);
            adapter.setOnItemClickListener(this);
            adapter.setOnMoreClickListener(this::removeFavorite);

            recyclerView.setAdapter(adapter);
            return;
        }

        int offset = fields.getNonNull(MvpConstants.OFFSET);

        if (offset > 0) {
            adapter.addAll(values);
            return;
        }

        if (values.isEmpty()) {
            adapter.clear();
        } else {
            adapter.changeItems(values);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearList() {
        if (adapter != null) {
            adapter.clear();
        }
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onItemClick(int position) {

    }
}
