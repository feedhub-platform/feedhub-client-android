package com.feedhub.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.feedhub.app.R;
import com.feedhub.app.current.BaseActivity;
import com.feedhub.app.fragment.FragmentFollowing;
import com.feedhub.app.fragment.FragmentGeneral;
import com.feedhub.app.fragment.FragmentHeadlines;
import com.feedhub.app.fragment.FragmentSaved;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final FragmentGeneral fragmentGeneral = new FragmentGeneral();
    private final FragmentHeadlines fragmentHeadlines = new FragmentHeadlines();
    private final FragmentFollowing fragmentFollowing = new FragmentFollowing();
    private final FragmentSaved fragmentSaved = new FragmentSaved();

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView navigationView;

    @BindView(R.id.toolbarSearch)
    LinearLayout toolbarSearch;

    @BindView(R.id.toolbarAvatar)
    RoundedImageView toolbarAvatar;

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

        toolbarSearch.setOnClickListener(v -> {

        });

        toolbarAvatar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            String[] items = new String[]{
                    getString(R.string.navigation_settings)
            };

            builder.setItems(items, (dialog, which) -> {
                switch (which) {
                    case 0:
                        openSettingsScreen();
                        break;
                }
            });

            builder.create().show();
        });
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

    private void openSettingsScreen() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

}
