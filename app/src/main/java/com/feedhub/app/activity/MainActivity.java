package com.feedhub.app.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.feedhub.app.R;
import com.feedhub.app.current.BaseActivity;
import com.feedhub.app.fragment.FragmentFollowing;
import com.feedhub.app.fragment.FragmentGeneral;
import com.feedhub.app.fragment.FragmentHeadlines;
import com.feedhub.app.fragment.FragmentSaved;
import com.feedhub.app.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final FragmentGeneral fragmentGeneral = new FragmentGeneral();
    private final FragmentHeadlines fragmentHeadlines = new FragmentHeadlines();
    private final FragmentFollowing fragmentFollowing = new FragmentFollowing();
    private final FragmentSaved fragmentSaved = new FragmentSaved();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        prepareToolbar();
        prepareBottomNavView();

        replaceFragment(fragmentGeneral);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
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

        //чтобы текст не уезжал куда-то влево, костыль
        Drawable someDrawable = getDrawable(R.drawable.ic_logo_placeholder);

        if (getSupportActionBar() != null)
            getSupportActionBar().setLogo(someDrawable);
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
