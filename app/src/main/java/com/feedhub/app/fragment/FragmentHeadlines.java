package com.feedhub.app.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.feedhub.app.R;
import com.feedhub.app.adapter.HeadlinesPagerAdapter;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.common.TaskManager;
import com.feedhub.app.current.BaseFragment;
import com.feedhub.app.dialog.ProfileBottomSheetDialog;
import com.feedhub.app.item.Headline;
import com.feedhub.app.item.Topic;
import com.feedhub.app.mvp.view.HeadlinesView;
import com.feedhub.app.net.HttpRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.library.mvp.base.MvpFields;

public class FragmentHeadlines extends BaseFragment implements HeadlinesView {

    private ArrayMap<String, String> categories = new ArrayMap<>();

    private ArrayMap<String, ArrayList<Topic>> topics = new ArrayMap<>();

    @BindView(R.id.headlinesPager)
    ViewPager2 viewPager;

    @BindView(R.id.headlinesTabs)
    TabLayout tabLayout;


    private boolean isPrepared;

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

        if (savedInstanceState == null && isAttached()) {
            loadCategories();
        }
    }

    void loadCategories() {
        String url =
                AppGlobal.preferences.getString(FragmentSettings.KEY_SERVER_URL, "") + "/" +
                        AppGlobal.preferences.getString(FragmentSettings.KEY_CATEGORY_KEY, "");

        StringBuilder topicsUrl =
                new StringBuilder(AppGlobal.preferences.getString(FragmentSettings.KEY_SERVER_URL, "") + "/" +
                        AppGlobal.preferences.getString(FragmentSettings.KEY_TOPICS_KEY, "") + "?category=");

        TaskManager.execute(() -> {
            try {
                JSONObject root = new JSONObject(HttpRequest.get(url).asString());
                JSONObject response = Objects.requireNonNull(root.optJSONObject("response"));
                JSONArray items = Objects.requireNonNull(response.optJSONArray("categories"));

                for (int i = 0; i < items.length(); i++) {
                    JSONObject category = Objects.requireNonNull(items.optJSONObject(i));

                    String id = category.optString("id");
                    String title = category.optString("title");

                    topicsUrl.append(id).append(i == items.length() ? "" : ",");

                    categories.put(id, title);
                }

                JSONObject root2 = new JSONObject(HttpRequest.get(topicsUrl.toString()).asString());
                JSONObject response2 = Objects.requireNonNull(root2.optJSONObject("response"));
                JSONArray topics = Objects.requireNonNull(response2.optJSONArray("topics"));

                for (int i = 0; i < topics.length(); i++) {
                    JSONObject topic = topics.optJSONObject(i);

                    String categoryId = topic.optString("categoryId");

                    JSONArray topics2 = topic.optJSONArray("items");

                    ArrayList<Topic> arrayList = new ArrayList<>();

                    for (int j = 0; j < topics2.length(); j++) {
                        arrayList.add(new Topic(topics2.optJSONObject(j)));
                    }

                    FragmentHeadlines.this.topics.put(categoryId, arrayList);
                }

                if (isAttached()) {
                    if (!isPrepared) {
                        isPrepared = true;

                        AppGlobal.handler.post(() -> {
                            prepareViewPager();
                            prepareTabLayout();
                        });
                    } else {
                        if (viewPager.getAdapter() != null) {
                            viewPager.post(() -> viewPager.getAdapter().notifyDataSetChanged());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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

    @SuppressLint("ClickableViewAccessibility")
    private void prepareViewPager() {
        ArrayMap<ArrayMap<String, String>, ArrayList<Topic>> items = new ArrayMap<>();

        for (int i = 0; i < categories.size(); i++) {
            ArrayMap<String, String> data = new ArrayMap<>();

            String id = categories.keyAt(i);
            String title = categories.valueAt(i);

            data.put(id, title);

            items.put(data, i > topics.size() - 1 ? new ArrayList<>() : topics.valueAt(i));
        }

        viewPager.setUserInputEnabled(false);
        viewPager.setSaveEnabled(false);

        viewPager.setAdapter(new HeadlinesPagerAdapter(this, items));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    private void prepareTabLayout() {
        TabLayoutMediator tabMediator = new TabLayoutMediator(tabLayout, viewPager, (this::onConfigureTab));

        tabMediator.attach();
    }

    private void onConfigureTab(TabLayout.Tab tab, int position) {
        tab.setText(categories.valueAt(position));
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

    }

    @Override
    public void clearList() {

    }
}
