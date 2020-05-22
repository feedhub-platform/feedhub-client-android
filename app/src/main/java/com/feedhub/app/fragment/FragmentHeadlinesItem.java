package com.feedhub.app.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.feedhub.app.R;
import com.feedhub.app.adapter.HeadlineAdapter;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.current.BaseFragment;
import com.feedhub.app.item.News;
import com.feedhub.app.item.Topic;
import com.feedhub.app.net.RequestBuilder;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentHeadlinesItem extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.headlinesItemChip)
    ChipGroup chipGroup;

    private String categoryTitle;
    private String categoryId;
    private ArrayList<Topic> topics;
    private int lastCheckedId;

    private String selectedTopic = "";

    private HeadlineAdapter adapter;

    public static FragmentHeadlinesItem newInstance(String id, String title, ArrayList<Topic> topics) {
        FragmentHeadlinesItem fragment = new FragmentHeadlinesItem();

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("id", id);
        bundle.putSerializable("topics", topics);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            categoryTitle = getArguments().getString("title", "");
            categoryId = getArguments().getString("id", "");
            topics = (ArrayList<Topic>) getArguments().getSerializable("topics");
        }
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_headlines_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        prepareRefreshLayout();
        prepareRecyclerView();
        prepareChipGroup();

        loadData();
    }

    @SuppressLint("SetTextI18n")
    private void prepareChipGroup() {
        if (chipGroup.getChildCount() > 0) chipGroup.removeAllViews();

        for (int i = 0; i < topics.size(); i++) {
            Topic topic = topics.get(i);

            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.fragment_headlines_item_chip, chipGroup, false);
            chip.setId(i);
            chip.setTag(topic.id);
            chip.setText(topic.title);

            chipGroup.addView(chip);
        }

//        Chip chip = (Chip) chipGroup.getChildAt(0);
//
//        if (chip != null) {
//            chip.setChecked(true);
//
//            selectedTopic = topics.get(0).id;
//
//            loadData();
//        }


        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == -1) {
                checkedId = lastCheckedId;
                ((Chip) chipGroup.findViewById(checkedId)).setChecked(true);
            } else {
                lastCheckedId = checkedId;
            }

            selectedTopic = topics.get(checkedId).id;
            loadData();
        });
    }

    private void loadData() {
        RequestBuilder builder = RequestBuilder.create()
                .method(AppGlobal.preferences.getString(FragmentSettings.KEY_NEWS_KEY, ""))
                .put("category", categoryId);

        if (!selectedTopic.isEmpty())
            builder.put("topic", selectedTopic);

        builder.execute(new RequestBuilder.OnResponseListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                JSONObject jResponse = Objects.requireNonNull(response.optJSONObject("response"));
                JSONArray items = Objects.requireNonNull(jResponse.optJSONArray("items"));
                ArrayList<News> news = new ArrayList<>();

                for (int i = 0; i < items.length(); i++) {
                    news.add(new News(items.optJSONObject(i)));
                }
                AppGlobal.handler.post(() -> {
                    createAdapter(news);
                    if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
                });
            }

            @Override
            public void onError(Exception e) {
            }
        });
//        TaskManager.execute(() -> {
//            try {
//                String serverUrl = AppGlobal.preferences.getString(FragmentSettings.KEY_SERVER_URL, "") + "/" +
//                        AppGlobal.preferences.getString(FragmentSettings.KEY_NEWS_KEY, "") + "?category=" + categoryId + "&topic=" + selectedTopic;
//
//                JSONObject root = new JSONObject(HttpRequest.get(serverUrl).asString());
//                JSONObject response = Objects.requireNonNull(root.optJSONObject("response"));
//                JSONArray items = Objects.requireNonNull(response.optJSONArray("items"));
//
//                ArrayList<News> news = new ArrayList<>();
//
//                for (int i = 0; i < items.length(); i++) {
//                    news.add(new News(items.optJSONObject(i)));
//                }
//
//                AppGlobal.handler.post(() -> {
//                    createAdapter(news);
//
//                    if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
    }

    private void prepareRefreshLayout() {
        refreshLayout.setColorSchemeResources(R.color.accent);
        refreshLayout.setOnRefreshListener(this);
    }

    private void prepareRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
    }

    private void createAdapter(ArrayList<News> news) {
        if (adapter == null) {
            adapter = new HeadlineAdapter(requireContext(), news);
            recyclerView.setAdapter(adapter);
            return;
        }

        adapter.changeItems(news);
        adapter.notifyDataSetChanged();
    }

    private void refreshData() {
        FragmentHeadlines fragment = (FragmentHeadlines) requireParentFragment();
        fragment.loadCategories();

        loadData();
    }
}
