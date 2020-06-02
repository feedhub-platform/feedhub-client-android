package com.feedhub.app.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.feedhub.app.R;
import com.feedhub.app.current.BaseActivity;
import com.google.android.material.appbar.MaterialToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.aboutVersion)
    TextView version;

    @BindView(R.id.toolbar)
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (info == null) return;

        String versionName = info.versionName;

        version.setText(versionName);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }
}
