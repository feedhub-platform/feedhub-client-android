package com.feedhub.app.mvp.presenter;

import androidx.annotation.NonNull;

import com.feedhub.app.item.News;
import com.feedhub.app.mvp.repository.NewsRepository;
import com.feedhub.app.mvp.view.NewsView;

import java.util.ArrayList;

import ru.melod1n.library.mvp.base.MvpFields;
import ru.melod1n.library.mvp.base.MvpPresenter;

public class NewsPresenter extends MvpPresenter<News, NewsView> {

    public NewsPresenter(@NonNull NewsView view) {
        super(view);

        initRepository(new NewsRepository());
    }

    @Override
    protected void insertValues(@NonNull MvpFields fields, @NonNull ArrayList<News> values) {
        if (view != null) {
            view.insertValues(fields, values);
        }
    }
}
