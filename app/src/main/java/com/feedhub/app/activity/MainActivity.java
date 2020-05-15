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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;

import butterknife.BindView;
import ru.melod1n.library.fragment.FragmentSwitcher;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private final FragmentGeneral fragmentGeneral = new FragmentGeneral();
    private final FragmentHeadlines fragmentHeadlines = new FragmentHeadlines();
    private final FragmentFollowing fragmentFollowing = new FragmentFollowing();
    private final FragmentSaved fragmentSaved = new FragmentSaved();

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
        container.setVisibility(View.INVISIBLE);

        int containerId = container.getId();

        FragmentSwitcher.addFragments(
                getSupportFragmentManager(),
                containerId,
                Arrays.asList(fragmentGeneral, fragmentHeadlines, fragmentFollowing, fragmentSaved)
        );

        FragmentSwitcher.showFragment(getSupportFragmentManager(), fragmentGeneral.getClass().getSimpleName(), true);

//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(containerId, fragmentGeneral, fragmentGeneral.getClass().getSimpleName())
//                .add(containerId, fragmentHeadlines, fragmentHeadlines.getClass().getSimpleName())
//                .add(containerId, fragmentFollowing, fragmentFollowing.getClass().getSimpleName())
//                .add(containerId, fragmentSaved, fragmentSaved.getClass().getSimpleName())
//                .commit();


        container.setVisibility(View.VISIBLE);
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

    private void prepareBottomNavView() {
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setOnNavigationItemReselectedListener(this::onReselect);
    }

    private void onReselect(@NonNull MenuItem item) {
        //empty
    }

    private void replaceFragment(Fragment fragment) {
        FragmentSwitcher.showFragment(getSupportFragmentManager(), fragment.getTag(), true);
    }
}
