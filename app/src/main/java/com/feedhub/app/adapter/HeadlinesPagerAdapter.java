package com.feedhub.app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.feedhub.app.current.BaseFragment;
import com.feedhub.app.fragment.FragmentHeadlinesItem;
import com.feedhub.app.item.Headline;

import java.util.List;

public class HeadlinesPagerAdapter extends FragmentStateAdapter {

//    private ArrayMap<ArrayMap<String, String>, ArrayList<Topic>> items;

    public List<Headline> items;

    public HeadlinesPagerAdapter(BaseFragment fragment, List<Headline> items) {
        super(fragment);

        this.items = items;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Headline headline = items.get(position);

        return FragmentHeadlinesItem.newInstance(
                headline.id, headline.title, headline.topics
        );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
