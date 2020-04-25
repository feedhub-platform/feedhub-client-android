package com.feedhub.app.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
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

    public void setNavigationIcon(@Nullable Drawable icon) {
        ((ImageButton) findViewById(R.id.toolbarNavigationIcon)).setImageDrawable(icon);
    }

    public void setNavigationIcon(@DrawableRes int resId) {
        ((ImageButton) findViewById(R.id.toolbarNavigationIcon)).setImageResource(resId);
    }

    public void setNavigationIconTintList(ColorStateList tintList) {
        Drawable icon = ((ImageButton) findViewById(R.id.toolbarNavigationIcon)).getDrawable();

        if (icon != null) {
            icon.setTintList(tintList);
        }
    }

    public void setNavigationIconTint(@ColorInt int tintColor) {
        Drawable icon = ((ImageButton) findViewById(R.id.toolbarNavigationIcon)).getDrawable();

        if (icon != null) {
            icon.setTint(tintColor);
        }
    }

    public void setNavigationClickListener(OnClickListener listener) {
        findViewById(R.id.toolbarNavigation).setOnClickListener(listener);
    }

    public void setNavigationOnBackClickListener(@NonNull Activity activity) {
        findViewById(R.id.toolbarNavigation).setOnClickListener(v -> activity.onBackPressed());
    }

    public void setNavigationVisibility(boolean visible) {
        findViewById(R.id.toolbarNavigation).setVisibility(visible ? VISIBLE : GONE);
    }

    public void setAvatarIcon(@Nullable Drawable icon) {
        ((ImageView) findViewById(R.id.toolbarAvatar)).setImageDrawable(icon);
    }

    public void setAvatarClickListener(OnClickListener listener) {
        findViewById(R.id.toolbarAvatar).setOnClickListener(listener);
    }

    public void setAvatarVisibility(boolean visible) {
        findViewById(R.id.toolbarAvatar).setVisibility(visible ? VISIBLE : GONE);
    }
}