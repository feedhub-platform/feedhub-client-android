package com.feedhub.app.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.R;

public class Toolbar extends androidx.appcompat.widget.Toolbar {

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        //...
    }

    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getString(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        ((TextView) findViewById(R.id.toolbarTitle)).setText(title);
    }

    @Override
    public void setTitleTextColor(int color) {
        setTitleTextColor(ColorStateList.valueOf(color));
    }

    @Override
    public void setTitleTextColor(@NonNull ColorStateList color) {
        ((TextView) findViewById(R.id.toolbarTitle)).setTextColor(color);
    }
}
