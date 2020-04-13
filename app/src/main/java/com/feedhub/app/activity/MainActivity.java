package com.feedhub.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.feedhub.app.R;
import com.feedhub.app.current.BaseActivity;
import com.feedhub.app.fragment.FragmentFollowing;
import com.feedhub.app.fragment.FragmentGeneral;
import com.feedhub.app.fragment.FragmentHeadlines;
import com.feedhub.app.fragment.FragmentSaved;
import com.feedhub.app.fragment.FragmentSettings;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final FragmentGeneral fragmentGeneral = new FragmentGeneral();
    private final FragmentHeadlines fragmentHeadlines = new FragmentHeadlines();
    private final FragmentFollowing fragmentFollowing = new FragmentFollowing();
    private final FragmentSaved fragmentSaved = new FragmentSaved();
    private final FragmentSettings fragmentSettings = new FragmentSettings();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView navigationView;

    public MainActivity() {
        super(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareToolbar();
        prepareBottomNavView();

        replaceFragment(fragmentGeneral);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigationGeneral:
                replaceFragment(fragmentGeneral);
                return true;
            case R.id.navigationHeadlines:
                replaceFragment(fragmentHeadlines);
                return true;
            case R.id.navigationFollowing:
                replaceFragment(fragmentFollowing);
                return true;
            case R.id.navigationSaved:
                replaceFragment(fragmentSaved);
                return true;
            case R.id.navigationSettings:
                replaceFragment(fragmentSettings);
                return true;
        }
        return false;
    }

    private void prepareToolbar() {
        setSupportActionBar(toolbar);
    }

    private void prepareBottomNavView() {
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setOnNavigationItemReselectedListener(this::onReselect);
    }

    private void onReselect(@NonNull MenuItem item) {
        //empty
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.getClass().getSimpleName()).commit();
    }

}
