package ru.melod1n.library.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.feedhub.app.current.BaseAdapter;

public abstract class EndlessScrollListener<A extends BaseAdapter> extends RecyclerView.OnScrollListener {

    @NonNull
    private A adapter;

    public EndlessScrollListener(@NonNull A adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        if (!adapter.isLoading) {
            if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.getValues().size() - 1) {
                adapter.isLoading = true;
                onLoadMore();
            }
        }
    }

    public void onLoadMore() {
    }

}
