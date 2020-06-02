package com.feedhub.app.mvp.presenter;

import androidx.annotation.NonNull;

import com.feedhub.app.item.Headline;
import com.feedhub.app.mvp.repository.HeadlinesRepository;
import com.feedhub.app.mvp.view.HeadlinesView;

import ru.melod1n.library.mvp.base.MvpPresenter;

public class HeadlinesPresenter extends MvpPresenter<Headline, HeadlinesView, HeadlinesRepository> {

    public HeadlinesPresenter(@NonNull HeadlinesView view) {
        super(view);

        initRepository(new HeadlinesRepository());
    }
}
