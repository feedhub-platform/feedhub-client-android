package com.feedhub.app.current;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class BaseAdapter<T, VH extends BaseHolder>
        extends RecyclerView.Adapter<VH> {

    public OnItemClickListener onItemClickListener;
    public OnItemLongClickListener onItemLongClickListener;
    protected Context context;
    protected ArrayList<T> values;
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

    public void changeItems(ArrayList<T> values) {
        this.values = values;
    }

    protected View view(@LayoutRes int resId, @NonNull ViewGroup viewGroup) {
        return inflater.inflate(resId, viewGroup, false);
    }

    protected T getItem(int position) {
        return values.get(position);
    }

    protected void remove(int index) {
        values.remove(index);
    }

    protected void remove(T item) {
        values.remove(item);
    }

    protected void add(T item) {
        values.add(item);
    }

    protected void add(int index, T item) {
        values.add(index, item);
    }

    protected void set(int index, T item) {
        values.set(index, item);
    }

    protected void clear() {
        values.clear();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

}
