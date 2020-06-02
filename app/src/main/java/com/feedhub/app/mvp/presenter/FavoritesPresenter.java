package com.feedhub.app.mvp.presenter;

import androidx.annotation.NonNull;

import com.feedhub.app.item.Favorite;
import com.feedhub.app.mvp.repository.FavoritesRepository;
import com.feedhub.app.mvp.view.FavoritesView;

import ru.melod1n.library.mvp.base.MvpPresenter;

public class FavoritesPresenter extends MvpPresenter<Favorite, FavoritesView, FavoritesRepository> {

    public FavoritesPresenter(@NonNull FavoritesView view) {
        super(view);

        initRepository(new FavoritesRepository());
    }

}
