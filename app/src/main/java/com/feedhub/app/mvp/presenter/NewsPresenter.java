package com.feedhub.app.mvp.presenter;

import androidx.annotation.NonNull;

import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.common.TaskManager;
import com.feedhub.app.dao.NewsDao;
import com.feedhub.app.item.News;
import com.feedhub.app.mvp.contract.BaseContract;
import com.feedhub.app.mvp.repository.NewsRepository;
import com.feedhub.app.util.ArrayUtils;

import java.util.ArrayList;

public class NewsPresenter implements BaseContract.Presenter<News> {

    @NonNull
    private BaseContract.View<News> view;

    @NonNull
    private NewsRepository repository;

    private NewsDao newsDao = AppGlobal.database.newsDao();

//    private ArrayList<News> loadedValues = new ArrayList<>();
//    private ArrayList<News> cachedValues = new ArrayList<>();

    public NewsPresenter(@NonNull BaseContract.View<News> view) {
        this.view = view;

        repository = new NewsRepository();
    }

    @Override
    public void post(Runnable r) {
        AppGlobal.handler.post(r);
    }

    @Override
    public void prepareForLoading() {
        view.showNoInternetView(false);
        view.showNoItemsView(false);
        view.showProgressBar(false);
        view.showErrorView(null);
    }

    @Override
    public void showList() {
        view.showProgressBar(false);
        view.showRefreshLayout(false);
    }

    @Override
    public void requestValues(int offset, int count) {
        repository.loadValues(offset, count, new BaseContract.OnValuesLoadListener<News>() {
            @Override
            public void onLoadValues(ArrayList<News> values) {
                post(() -> onValuesLoaded(offset, count, values, false));
            }

            @Override
            public void onErrorLoad(Exception e) {
                post(() -> onValuesError(e));
            }
        });
    }

    @Override
    public void requestCachedValues(int offset, int count) {
        TaskManager.execute(() -> {
            ArrayList<News> cachedValues = new ArrayList<>(newsDao.getAll());
            ArrayUtils.prepareList(cachedValues, offset, count);

            post(() -> onValuesLoaded(offset, count, cachedValues, true));
        });
    }

    @Override
    public void onValuesLoaded(int offset, int count, ArrayList<News> values, boolean isCache) {
        showList();

        post(() -> view.insertValues(offset, count, values, isCache));
    }

    @Override
    public void onValuesError(Exception e) {
        showList();

        post(() -> view.showErrorView(e));
    }
}
