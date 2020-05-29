package com.feedhub.app.mvp.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.dao.FavoritesDao;
import com.feedhub.app.item.Favorite;

import java.util.ArrayList;

import ru.melod1n.library.mvp.base.MvpException;
import ru.melod1n.library.mvp.base.MvpFields;
import ru.melod1n.library.mvp.base.MvpOnLoadListener;
import ru.melod1n.library.mvp.base.MvpRepository;

public class FavoritesRepository extends MvpRepository<Favorite> {

    private FavoritesDao dao = AppGlobal.database.favoritesDao();

    @Override
    public void loadCachedValues(@NonNull MvpFields fields, @Nullable MvpOnLoadListener<Favorite> listener) {
        startNewThread(() -> {
            try {
                ArrayList<Favorite> cachedValues = new ArrayList<>(dao.getAll());

                post(() -> {
                    if (cachedValues.isEmpty()) {
                        sendError(listener, MvpException.ERROR_EMPTY);
                    } else {
                        sendValuesToPresenter(fields, cachedValues, listener);
                    }
                });
            } catch (Exception e) {
                post(() -> sendError(listener, e));
            }
        });
    }
}
