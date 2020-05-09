package com.feedhub.app.mvp.presenter;

import androidx.annotation.NonNull;

import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.dao.HeadlinesDao;
import com.feedhub.app.dao.NewsDao;
import com.feedhub.app.item.Headline;
import com.feedhub.app.item.News;
import com.feedhub.app.mvp.contract.BaseContract;
import com.feedhub.app.mvp.repository.HeadlinesRepository;
import com.feedhub.app.mvp.repository.NewsRepository;
import com.feedhub.app.mvp.view.HeadlinesView;

import java.util.ArrayList;

import ru.melod1n.library.mvp.base.MvpFields;
import ru.melod1n.library.mvp.base.Presenter;

public class HeadlinesPresenter extends Presenter<Headline, HeadlinesView> {

    public static final String CATEGORY = "category";

    public HeadlinesPresenter(@NonNull HeadlinesView view) {
        super(view);

        initRepository(new HeadlinesRepository());
    }

    @Override
    protected void insertValues(@NonNull MvpFields fields, @NonNull ArrayList<Headline> values) {

    }
}
