package com.feedhub.app.mvp.presenter;

import androidx.annotation.NonNull;

import com.feedhub.app.item.Source;
import com.feedhub.app.mvp.repository.SourceRepository;
import com.feedhub.app.mvp.view.SourcesView;

import ru.melod1n.library.mvp.base.MvpPresenter;

public class SourcesPresenter extends MvpPresenter<Source, SourcesView, SourceRepository> {

    public SourcesPresenter(@NonNull SourcesView view) {
        super(view);

        initRepository(new SourceRepository());
    }
}
