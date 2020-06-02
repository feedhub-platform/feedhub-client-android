package com.feedhub.app.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feedhub.app.R;
import com.feedhub.app.current.BaseActivity;
import com.feedhub.app.fragment.FragmentSettings;
import com.feedhub.app.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        toolbar.setTitle(R.string.navigation_settings);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnBackClickListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FragmentSettings(), FragmentSettings.class.getSimpleName()).commit();
    }

    @NonNull
    public Toolbar getToolbar() {
        return toolbar;
    }
}
