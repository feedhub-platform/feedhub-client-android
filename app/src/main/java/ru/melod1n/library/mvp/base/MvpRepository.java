package ru.melod1n.library.mvp.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.common.TaskManager;

import java.util.ArrayList;

public abstract class MvpRepository<T> {

    public abstract void loadValues(@NonNull MvpFields fields, @Nullable OnLoadListener<T> listener);

    public abstract void loadCachedValues(@NonNull MvpFields fields, @Nullable OnLoadListener<T> listener);

    protected void sendError(@Nullable OnLoadListener<T> listener, @NonNull String errorId) {
        if (listener != null) {
            listener.onErrorLoad(new MvpException(errorId));
        }
    }

    protected void sendError(@Nullable OnLoadListener<T> listener, @Nullable Exception e) {
        if (listener != null && e != null) {
            AppGlobal.handler.post(() -> listener.onErrorLoad(e));
        }
    }

    protected void sendValuesToPresenter(@NonNull MvpFields fields, @NonNull ArrayList<T> values, @Nullable OnLoadListener<T> listener) {
        if (listener != null) {
            AppGlobal.handler.post(() -> listener.onSuccessLoad(values));
        }
    }

    protected abstract void cacheLoadedValues(@NonNull ArrayList<T> values);

    protected void startNewThread(Runnable runnable) {
        TaskManager.execute(runnable);
    }

    protected void post(Runnable runnable) {
        AppGlobal.handler.post(runnable);
    }

}