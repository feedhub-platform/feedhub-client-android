package com.feedhub.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.feedhub.app.R;
import com.feedhub.app.adapter.HeadlinesPagerAdapter;
import com.feedhub.app.current.BaseFragment;
import com.feedhub.app.dialog.ProfileBottomSheetDialog;
import com.feedhub.app.item.Headline;
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

public class FragmentHeadlines extends BaseFragment implements HeadlinesView {

    @BindView(R.id.headlinesPager)
    ViewPager2 viewPager;

    @BindView(R.id.headlinesTabs)
    TabLayout tabLayout;

    private boolean needToDestroy;

//    private ArrayMap<String, String> categories = new ArrayMap<>();
//    private ArrayMap<String, ArrayList<Topic>> topics = new ArrayMap<>();

    private List<String> titles = new ArrayList<>();

    private boolean isPrepared;

    private HeadlinesPresenter presenter;

    private HeadlinesPagerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);

        if (args == null) return;

        String sourceId = args.getString("sourceId", "_empty");

        if (sourceId.equals("_empty")) return;

        needToDestroy = true;

        loadCategories(null, sourceId);
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

        prepareToolbar();
        prepareViewPager();

        presenter = new HeadlinesPresenter(this);

        loadCategories(savedInstanceState, null);
    }

    void loadCategories() {
        loadCategories(null, null);
    }

    private void loadCategories(Bundle savedInstanceState, @Nullable String sourceId) {
        if (savedInstanceState == null && isAttached() || sourceId != null) {
            MvpFields fields = new MvpFields()
                    .put(MvpConstants.OFFSET, 0)
                    .put(MvpConstants.COUNT, 30)
                    .put(MvpConstants.FROM_CACHE, false);

            if (sourceId != null) {
                fields.put("sourceId", sourceId);
            }

            presenter.requestLoadValues(fields);
        } else {
            presenter.requestCachedData(new MvpFields()
                    .put(MvpConstants.OFFSET, 0)
                    .put(MvpConstants.COUNT, 30)
                    .put(MvpConstants.FROM_CACHE, true));
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (needToDestroy && hidden) {
            loadCategories(null, null);
        }
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
        return v -> ProfileBottomSheetDialog.show(getParentFragmentManager());
    }

    private void prepareViewPager() {
        viewPager.setUserInputEnabled(false);
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

    private void onConfigureTab(TabLayout.Tab tab, int position) {
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

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void insertValues(@NonNull MvpFields fields, @NonNull ArrayList<Headline> values) {
        adapter = new HeadlinesPagerAdapter(this, values);
        viewPager.setAdapter(adapter);

        prepareTabLayout();
    }

    @Override
    public void clearList() {

    }
}
