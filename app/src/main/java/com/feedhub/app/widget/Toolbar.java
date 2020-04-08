package com.feedhub.app.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.R;
import com.google.android.material.appbar.MaterialToolbar;

public class Toolbar extends MaterialToolbar {

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
        ((TextView) findViewById(R.id.toolbarTitle)).setTextColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        init();
    }
}
