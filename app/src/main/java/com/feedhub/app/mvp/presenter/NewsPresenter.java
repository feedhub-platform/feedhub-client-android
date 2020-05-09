package com.feedhub.app.mvp.presenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.common.TaskManager;
import com.feedhub.app.dao.NewsDao;
import com.feedhub.app.item.News;
import com.feedhub.app.mvp.contract.BaseContract;
import com.feedhub.app.mvp.repository.NewsRepository;
import com.feedhub.app.mvp.view.NewsView;
import com.feedhub.app.util.ArrayUtils;

import java.util.ArrayList;

import ru.melod1n.library.mvp.base.MvpFields;
import ru.melod1n.library.mvp.base.Presenter;

public class NewsPresenter extends Presenter<News, NewsView> {

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
