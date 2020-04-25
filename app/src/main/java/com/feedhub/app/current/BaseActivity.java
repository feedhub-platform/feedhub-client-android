package com.feedhub.app.current;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.feedhub.app.R;
import com.feedhub.app.activity.SettingsActivity;

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
