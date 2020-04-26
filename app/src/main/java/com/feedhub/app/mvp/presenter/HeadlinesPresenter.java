package com.feedhub.app.mvp.presenter;

import androidx.annotation.NonNull;

import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.dao.HeadlinesDao;
import com.feedhub.app.dao.NewsDao;
import com.feedhub.app.item.Headline;
import com.feedhub.app.item.News;
import com.feedhub.app.mvp.contract.BaseContract;
import com.feedhub.app.mvp.repository.NewsRepository;

import java.util.ArrayList;

public class HeadlinesPresenter implements BaseContract.Presenter<Headline> {

    @NonNull
    private BaseContract.View<News> view;

    @NonNull
    private NewsRepository repository;

    private HeadlinesDao newsDao = AppGlobal.database.headlinesDao();

    public HeadlinesPresenter(@NonNull BaseContract.View<News> view) {
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

    }

    @Override
    public void requestCachedValues(int offset, int count) {

    }

    @Override
    public void onValuesLoaded(int offset, int count, ArrayList<Headline> values, boolean isCache) {

    }

    @Override
    public void onValuesError(Exception e) {

    }
}
