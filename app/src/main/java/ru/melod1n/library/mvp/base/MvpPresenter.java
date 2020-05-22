package ru.melod1n.library.mvp.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public abstract class MvpPresenter<T, V extends MvpView<T>, R extends MvpRepository<T>> {

    protected String TAG;

    @Nullable
    protected V view;

    @NonNull
    protected R repository;

    @NonNull
    protected ArrayList<T> loadedValues = new ArrayList<>();

    @NonNull
    protected ArrayList<T> cachedValues = new ArrayList<>();

    @NonNull
    protected ArrayList<T> values = new ArrayList<>();


    public MvpPresenter(@NonNull V view) {
        this.view = view;
    }

    protected void initRepository(@NonNull R repository) {
        this.repository = repository;
    }

    public void prepareForLoading() {
        if (view != null) {
            view.hideNoInternetView();
            view.hideNoItemsView();
            view.hideErrorView();

            if (values.isEmpty()) {
                view.showProgressBar();
            }
        }
    }

    public void showList() {
        if (view != null) {
            view.hideProgressBar();
            view.stopRefreshing();
        }
    }

    public void checkListIsEmpty(boolean fromCache) {
        if (((fromCache && cachedValues.isEmpty()) || (!fromCache && loadedValues.isEmpty())) && view != null) {
            view.showNoItemsView();
        }
    }

    public void checkListIsEmpty(ArrayList<T> values) {
        if (view != null && values.isEmpty()) {
            view.showNoItemsView();
        }
    }

    public void requestLoadValues(@NonNull MvpFields fields) {
        fields.put(MvpConstants.FROM_CACHE, false);

        Objects.requireNonNull(repository, "Repository must be init in Presenter's constructor").loadValues(fields, new MvpOnLoadListener<T>() {
            @Override
            public void onSuccessLoad(ArrayList<T> values) {
                onValuesLoaded(fields, values);
            }

            @Override
            public void onErrorLoad(Exception e) {
                onValuesError(e);
            }
        });
    }

    public void requestCachedData(@NonNull MvpFields fields) {
        fields.put(MvpConstants.FROM_CACHE, true);

        Objects.requireNonNull(repository, "Repository must be init in Presenter's constructor").loadCachedValues(fields, new MvpOnLoadListener<T>() {
            @Override
            public void onSuccessLoad(ArrayList<T> values) {
                onValuesLoaded(fields, values);
            }

            @Override
            public void onErrorLoad(Exception e) {
                onValuesError(e);
            }
        });
    }

    protected void onValuesLoaded(@NonNull MvpFields fields, @NonNull ArrayList<T> values) {
        showList();

        boolean fromCache = fields.getBoolean(MvpConstants.FROM_CACHE);

        this.values = values;

        if (fromCache) {
            cachedValues = values;
        } else {
            loadedValues = values;
        }

        insertValues(fields, values);
    }

    protected void onValuesError(Exception e) {
        showList();

        loadedValues.clear();

        if (view != null) view.showErrorView(e);
    }

    protected void insertValues(@NonNull MvpFields fields, @NonNull ArrayList<T> values) {
        if (view != null) {
            view.insertValues(fields, values);
        }
    }

    public void destroy() {
        view = null;
    }
}
