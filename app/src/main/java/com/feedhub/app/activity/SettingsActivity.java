package com.feedhub.app.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.feedhub.app.R;
import com.feedhub.app.fragment.FragmentSettings;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FragmentSettings(), FragmentSettings.class.getSimpleName()).commit();
    }
}
