package com.feedhub.app.current;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseHolder extends RecyclerView.ViewHolder {

    public BaseHolder(@NonNull View v) {
        super(v);
    }

    public abstract void bind(int position);
}
