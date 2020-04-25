package com.feedhub.app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.feedhub.app.current.BaseFragment;
import com.feedhub.app.fragment.FragmentHeadlinesItem;

import java.util.List;

public class HeadlinesPagerAdapter extends FragmentStateAdapter {

    @NonNull
    private List<FragmentHeadlinesItem> fragments;

    public HeadlinesPagerAdapter(BaseFragment fragment, @NonNull List<FragmentHeadlinesItem> fragments) {
        super(fragment);

        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
