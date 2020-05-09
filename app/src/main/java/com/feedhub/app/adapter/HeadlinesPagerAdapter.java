package com.feedhub.app.adapter;

import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.feedhub.app.current.BaseFragment;
import com.feedhub.app.fragment.FragmentHeadlines;
import com.feedhub.app.fragment.FragmentHeadlinesItem;
import com.feedhub.app.item.Topic;

import java.util.ArrayList;
import java.util.List;

public class HeadlinesPagerAdapter extends FragmentStateAdapter {

    private ArrayMap<ArrayMap<String, String>, ArrayList<Topic>> items;

    public HeadlinesPagerAdapter(BaseFragment fragment, @NonNull ArrayMap<ArrayMap<String, String>, ArrayList<Topic>> items) {
        super(fragment);

        this.items = items;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String id = items.keyAt(position).keyAt(0);
        String title = items.keyAt(position).valueAt(0);

        ArrayList<Topic> topics = items.valueAt(position);

        return FragmentHeadlinesItem.newInstance(id, title, topics);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
