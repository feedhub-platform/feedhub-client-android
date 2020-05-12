package com.feedhub.app.mvp.presenter;

import androidx.annotation.NonNull;

import com.feedhub.app.item.Headline;
import com.feedhub.app.mvp.repository.HeadlinesRepository;
import com.feedhub.app.mvp.view.HeadlinesView;

import java.util.ArrayList;

import ru.melod1n.library.mvp.base.MvpFields;
import ru.melod1n.library.mvp.base.MvpPresenter;

public class HeadlinesPresenter extends MvpPresenter<Headline, HeadlinesView> {

    public static final String CATEGORY = "category";

    public HeadlinesPresenter(@NonNull HeadlinesView view) {
        super(view);

        initRepository(new HeadlinesRepository());
    }

    @Override
    protected void insertValues(@NonNull MvpFields fields, @NonNull ArrayList<Headline> values) {

    }
}
