package com.feedhub.app.current;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected boolean useButterKnife = false;

    public BaseActivity() {
    }

    public BaseActivity(boolean useButterKnife) {
        this.useButterKnife = useButterKnife;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        if (useButterKnife) {
            ButterKnife.bind(this);
        }
    }
}
