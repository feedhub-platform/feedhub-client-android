package com.feedhub.app.mvp.presenter;

import androidx.annotation.NonNull;

import com.feedhub.app.item.News;
import com.feedhub.app.mvp.repository.NewsRepository;
import com.feedhub.app.mvp.view.NewsView;

import ru.melod1n.library.mvp.base.MvpPresenter;

public class NewsPresenter extends MvpPresenter<News, NewsView, NewsRepository> {

    public NewsPresenter(@NonNull NewsView view) {
        super(view);

        initRepository(new NewsRepository());
    }
}
