package com.feedhub.app.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.feedhub.app.R;
import com.feedhub.app.current.BaseActivity;
import com.feedhub.app.fragment.FragmentFollowing;
import com.feedhub.app.fragment.FragmentGeneral;
import com.feedhub.app.fragment.FragmentHeadlines;
import com.feedhub.app.fragment.FragmentSaved;
import com.feedhub.app.fragment.FragmentSources;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;

import butterknife.BindView;
import ru.melod1n.library.fragment.FragmentSwitcher;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public final FragmentHeadlines fragmentHeadlines = new FragmentHeadlines();
    private final FragmentGeneral fragmentGeneral = new FragmentGeneral();
    private final FragmentFollowing fragmentFollowing = new FragmentFollowing();
    private final FragmentSaved fragmentSaved = new FragmentSaved();
    private final FragmentSources fragmentSources = new FragmentSources();

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView navigationView;

    public MainActivity() {
        super(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareBottomNavView();

        prepareFragments();

        replaceFragment(fragmentGeneral);
    }

    private void prepareFragments() {
        View container = findViewById(R.id.fragmentContainer);
        container.setVisibility(View.VISIBLE);
        container.setAlpha(0);
        container.setClickable(false);
        container.setFocusable(false);

        int containerId = container.getId();

        FragmentSwitcher.addFragments(
                getSupportFragmentManager(),
                containerId,
                Arrays.asList(fragmentGeneral, fragmentHeadlines, fragmentFollowing, fragmentSaved, fragmentSources)
        );

        FragmentSwitcher.showFragment(getSupportFragmentManager(), fragmentGeneral.getClass().getSimpleName(), null, true);

//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(containerId, fragmentGeneral, fragmentGeneral.getClass().getSimpleName())
//                .add(containerId, fragmentHeadlines, fragmentHeadlines.getClass().getSimpleName())
//                .add(containerId, fragmentFollowing, fragmentFollowing.getClass().getSimpleName())
//                .add(containerId, fragmentSaved, fragmentSaved.getClass().getSimpleName())
//                .commit();


        container.animate().alpha(1).setDuration(1250).withEndAction(() -> {
            container.setClickable(true);
            container.setFocusable(true);
        }).start();
//        container.setVisibility(View.VISIBLE);
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
            case R.id.navigationSources:
                replaceFragment(fragmentSources);
                return true;
        }
        return false;
    }

    private void prepareBottomNavView() {
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setOnNavigationItemReselectedListener(this::onReselect);
    }

    private void onReselect(@NonNull MenuItem item) {
        //empty
    }

    private void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, null);
    }

    public void replaceFragment(@NonNull Fragment fragment, Bundle arguments) {
        FragmentSwitcher.showFragment(getSupportFragmentManager(), fragment.getTag(), arguments, true);
    }

    public BottomNavigationView getNavigationView() {
        return navigationView;
    }
}
