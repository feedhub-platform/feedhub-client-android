package com.feedhub.app.current;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.feedhub.app.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseAdapter<T, VH extends BaseHolder>
        extends RecyclerView.Adapter<VH> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_LOADING = 1;

    public boolean isLoading;

    public OnItemClickListener onItemClickListener;
    public OnItemLongClickListener onItemLongClickListener;
    public int currentPosition = -1;
    protected Context context;
    protected ArrayList<T> values;
    protected ArrayList<T> cleanValues;
    protected LayoutInflater inflater;

    public BaseAdapter(@NonNull Context context, @NonNull ArrayList<T> values) {
        this.context = context;
        this.values = values;

        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        initListeners(holder, position);
        holder.bind(position);
    }

    private void initListeners(@NonNull VH holder, int position) {
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(position);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onItemLongClickListener != null)
                onItemLongClickListener.onItemLongClick(position);

            return onItemClickListener != null;
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public ArrayList<T> getValues() {
        return values;
    }

    public void changeItems(ArrayList<T> values) {
        this.values = values;
    }

    protected View view(@LayoutRes int resId, @NonNull ViewGroup viewGroup) {
        return inflater.inflate(resId, viewGroup, false);
    }

    public T getItem(int position) {
        return values.get(position);
    }

    public void remove(int index) {
        values.remove(index);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public void remove(T item) {
        values.remove(item);
    }

    public void add(T item) {
        values.add(item);
    }

    public void add(int index, T item) {
        values.add(index, item);
    }

    public void set(int index, T item) {
        values.set(index, item);
    }

    public void clear() {
        values.clear();
    }

    public void addAll(ArrayList<T> values) {
        this.values.addAll(values);
    }

    public void addAll(int index, ArrayList<T> values) {
        this.values.addAll(index, values);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void filter(String query) {
        String lowerQuery = query.toLowerCase();

        if (cleanValues == null) {
            cleanValues = new ArrayList<>(values);
        }

        values.clear();

        if (query.isEmpty()) {
            values.addAll(cleanValues);
        } else {
            for (T value : cleanValues) {
                if (onQueryItem(value, lowerQuery)) {
                    values.add(value);
                }
            }
        }

        notifyDataSetChanged();
    }

    public boolean onQueryItem(@NonNull T item, @NonNull String lowerQuery) {
        return false;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    protected class LoadingHolder extends BaseHolder {

        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        public LoadingHolder(@NonNull View v) {
            super(v);

            ButterKnife.bind(this, v);
        }

        @Override
        public void bind(int position) {
        }
    }

}
