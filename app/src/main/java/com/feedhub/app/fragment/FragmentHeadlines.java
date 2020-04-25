package com.feedhub.app.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager2.widget.ViewPager2;

import com.feedhub.app.R;
import com.feedhub.app.activity.SettingsActivity;
import com.feedhub.app.adapter.HeadlinesPagerAdapter;
import com.feedhub.app.current.BaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentHeadlines extends BaseFragment {

    private final String[] titles = new String[]{
            "General",
            "Health",
            "Sport",
            "Hi-tech",
            "Miscellaneous"
    };

    @BindView(R.id.headlinesPager)
    ViewPager2 viewPager;

    @BindView(R.id.headlinesTabs)
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_headlines, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        prepareToolbar();
        prepareViewPager();
        prepareTabLayout();
    }

    private void prepareToolbar() {
        initToolbar(R.id.toolbar);

        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationClickListener(v -> {
        });
        toolbar.setNavigationIcon(R.drawable.ic_search);
        toolbar.setAvatarClickListener(getAvatarClickListener());
    }

    private View.OnClickListener getAvatarClickListener() {
        return v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

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
        };
    }

    private void openSettingsScreen() {
        startActivity(new Intent(requireContext(), SettingsActivity.class));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void prepareViewPager() {
        List<FragmentHeadlinesItem> fragments = new ArrayList<>();

        for (String title : titles) {
            fragments.add(FragmentHeadlinesItem.newInstance(title));
        }

        viewPager.setUserInputEnabled(false);
        viewPager.setSaveEnabled(false);
        viewPager.setAdapter(new HeadlinesPagerAdapter(this, fragments));
    }

    private void prepareTabLayout() {
        TabLayoutMediator tabMediator = new TabLayoutMediator(tabLayout, viewPager, (this::onConfigureTab));

        tabMediator.attach();
    }

    private void onConfigureTab(TabLayout.Tab tab, int position) {
        tab.setText(titles[position]);
    }
}
