package com.feedhub.app.mvp.contract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.common.AppGlobal;

import java.util.ArrayList;

public class BaseContract {

    public interface View<T> {
        void showNoItemsView(boolean visible);

        void showNoInternetView(boolean visible);

        void showErrorView(@Nullable Exception e);

        void showRefreshLayout(boolean visible);

        void showProgressBar(boolean visible);

        void insertValues(int offset, int count, ArrayList<T> values, boolean isCache);

        void clearList();
    }

    public interface Presenter<T> {
        void post(Runnable r);

        void prepareForLoading();

        void showList();

        void requestValues(int offset, int count);

        void requestCachedValues(int offset, int count);

        void onValuesLoaded(int offset, int count, ArrayList<T> values, boolean isCache);

        void onValuesError(Exception e);
    }

    public abstract static class Repository<T> {

        public abstract void loadValues(int offset, int count, @Nullable OnValuesLoadListener<T> listener);

        @NonNull
        public abstract ArrayList<T> loadCachedValues(int offset, int count);

        public abstract void cacheValues(int offset, int count, @NonNull ArrayList<T> values);

        protected void sendError(OnValuesLoadListener<T> listener, Exception e) {
            if (listener == null) return;

            AppGlobal.handler.post(() -> listener.onErrorLoad(e));
        }

        protected void sendValues(OnValuesLoadListener<T> listener, ArrayList<T> values) {
            if (listener == null) return;

            AppGlobal.handler.post(() -> listener.onLoadValues(values));
        }
    }

    public interface OnValuesLoadListener<T> {
        void onLoadValues(ArrayList<T> values);

        void onErrorLoad(Exception e);
    }

}
