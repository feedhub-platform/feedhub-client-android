package com.feedhub.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.feedhub.app.R;
import com.feedhub.app.adapter.HeadlinesPagerAdapter;
import com.feedhub.app.current.BaseFragment;
import com.feedhub.app.dialog.ProfileBottomSheetDialog;
import com.feedhub.app.item.Headline;
import com.feedhub.app.item.Topic;
import com.feedhub.app.mvp.presenter.HeadlinesPresenter;
import com.feedhub.app.mvp.view.HeadlinesView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.library.mvp.base.MvpConstants;
import ru.melod1n.library.mvp.base.MvpFields;
import ru.melod1n.library.mvp.base.MvpOnLoadListener;

public class FragmentHeadlines extends BaseFragment implements HeadlinesView {

    @BindView(R.id.headlinesPager)
    ViewPager2 viewPager;

    @BindView(R.id.headlinesTabs)
    TabLayout tabLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    String currentCategory;
    int currentTopicId = -1;

    private List<String> titles = new ArrayList<>();
    private HeadlinesPresenter presenter;
    private HeadlinesPagerAdapter adapter;
    private String sourceId;
    private String sourceTitle;

    @NonNull
    public static FragmentHeadlines newInstance(@NonNull Bundle arguments) {
        FragmentHeadlines fragmentHeadlines = new FragmentHeadlines();
        fragmentHeadlines.setArguments(arguments);
        return fragmentHeadlines;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            sourceId = getArguments().getString("sourceId", "_empty");
            sourceTitle = getArguments().getString("sourceTitle", "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_headlines, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setRecyclerView(null);

        prepareToolbar();
        prepareViewPager();

        presenter = new HeadlinesPresenter(this);

        loadCategories();
    }

    void loadCategories() {
        MvpFields fields = new MvpFields()
                .put(MvpConstants.OFFSET, 0)
                .put(MvpConstants.COUNT, 30)
                .put(MvpConstants.FROM_CACHE, false);

        if (sourceId != null && !"_empty".equals(sourceId)) {
            fields.put("sourceId", sourceId);
            presenter.requestLoadValues(fields);
        } else {
            presenter.repository.loadTopics(new MvpOnLoadListener<ArrayMap<String, ArrayList<Topic>>>() {
                @Override
                public void onSuccessLoad(ArrayList<ArrayMap<String, ArrayList<Topic>>> values) {
                    fields.put("topics", values.get(0));

                    presenter.requestLoadValues(fields);
                }

                @Override
                public void onErrorLoad(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void prepareToolbar() {
        initToolbar(R.id.toolbar);

        boolean useForSource = sourceId != null && !"_empty".equals(sourceId);

        toolbar.setTitle(useForSource ? sourceTitle : getString(R.string.app_name));
        toolbar.setNavigationClickListener(v -> {
            if (useForSource) {
                requireActivity().onBackPressed();
            }
        });
        toolbar.setNavigationIcon(useForSource ? R.drawable.ic_arrow_back : R.drawable.ic_search);
        toolbar.setAvatarClickListener(getAvatarClickListener());
    }

    private View.OnClickListener getAvatarClickListener() {
        return v -> ProfileBottomSheetDialog.show(getParentFragmentManager());
    }

    private void prepareViewPager() {
        viewPager.setSaveEnabled(false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void prepareTabLayout() {
        for (Headline headline : adapter.items) {
            titles.add(headline.title);
        }

        TabLayoutMediator tabMediator = new TabLayoutMediator(tabLayout, viewPager, (this::onConfigureTab));

        tabMediator.attach();
    }

    private void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(titles.get(position));
    }

    @Override
    public void prepareNoInternetView() {
    }

    @Override
    public void prepareNoItemsView() {
    }

    @Override
    public void prepareErrorView() {
    }

    @Override
    public void showNoInternetView() {
    }

    @Override
    public void hideNoInternetView() {
    }

    @Override
    public void showNoItemsView() {
    }

    @Override
    public void hideNoItemsView() {
    }

    @Override
    public void showErrorView(Exception e) {
    }

    @Override
    public void hideErrorView() {
    }

    @Override
    public void startRefreshing() {
    }

    @Override
    public void stopRefreshing() {
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void insertValues(@NonNull MvpFields fields, @NonNull ArrayList<Headline> values) {
        if (!isAttached()) return;

        ArrayMap<String, ArrayList<Topic>> data = new ArrayMap<>();

        if (sourceTitle != null) {
            for (Headline headline : values) {
                data.put(headline.id, headline.topics);
            }
        }

        adapter =
                sourceId == null ?
                        new HeadlinesPagerAdapter(this, values) :
                        new HeadlinesPagerAdapter(this, values, sourceId, data);

        viewPager.setAdapter(adapter);

        prepareTabLayout();

        if (currentCategory != null) {
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);

                if (tab == null || tab.getText() == null) continue;

                if (currentCategory.contentEquals(tab.getText())) {
                    tabLayout.selectTab(tab);
                }
            }
        }
    }

    @Override
    public void clearList() {
    }
}
