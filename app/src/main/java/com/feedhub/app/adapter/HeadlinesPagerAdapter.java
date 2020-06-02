package com.feedhub.app.adapter;

import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.feedhub.app.current.BaseFragment;
import com.feedhub.app.fragment.FragmentHeadlinesItem;
import com.feedhub.app.item.Headline;
import com.feedhub.app.item.Topic;

import java.util.ArrayList;
import java.util.List;

public class HeadlinesPagerAdapter extends FragmentStateAdapter {

    public List<Headline> items;

    private String sourceId;
    private ArrayMap<String, ArrayList<Topic>> topics;

    public HeadlinesPagerAdapter(@NonNull BaseFragment fragment, List<Headline> items, String sourceId, ArrayMap<String, ArrayList<Topic>> topics) {
        super(fragment);

        this.items = items;
        this.sourceId = sourceId;
        this.topics = topics;
    }

    public HeadlinesPagerAdapter(@NonNull BaseFragment fragment, List<Headline> items) {
        this(fragment, items, null, null);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Headline headline = items.get(position);

        return sourceId == null ?
                FragmentHeadlinesItem.newInstance(
                        headline.id, headline.title, headline.topics
                ) :
                FragmentHeadlinesItem.newInstance(
                        headline.id, headline.title, sourceId, topics
                );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
