package com.feedhub.app.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.feedhub.app.R;
import com.feedhub.app.adapter.NewsAdapter;
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

public class FragmentHeadlinesItem extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, NewsAdapter.OnItemClickListener {

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.headlinesItemChip)
    ChipGroup chipGroup;

    private String sourceId;
    private String categoryTitle;
    private String categoryId;
    private ArrayList<Topic> topics;

    private int lastCheckedId = -1;

    private String selectedTopic = "";

    private NewsAdapter adapter;


    public static FragmentHeadlinesItem newInstance(String categoryId, String categoryTitle, ArrayList<Topic> topics) {
        FragmentHeadlinesItem fragment = new FragmentHeadlinesItem();

        Bundle bundle = new Bundle();
        bundle.putString("categoryTitle", categoryTitle);
        bundle.putString("categoryId", categoryId);
        bundle.putSerializable("topics", topics);

        fragment.setArguments(bundle);

        return fragment;
    }

    public static FragmentHeadlinesItem newInstance(String categoryId, String categoryTitle, String sourceId, ArrayMap<String, ArrayList<Topic>> topics) {
        FragmentHeadlinesItem fragment = new FragmentHeadlinesItem();

        Bundle bundle = new Bundle();
        bundle.putString("categoryTitle", categoryTitle);
        bundle.putString("categoryId", categoryId);
        bundle.putString("sourceId", sourceId);

        for (int i = 0; i < topics.size(); i++) {
            String key = topics.keyAt(i);
            if (categoryId.equals(key)) {
                bundle.putSerializable("topics", topics.valueAt(i));
                break;
            }
        }

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        FragmentHeadlines fragment = (FragmentHeadlines) requireParentFragment();
        lastCheckedId = fragment.currentTopicId;

        if (getArguments() != null) {
            categoryTitle = getArguments().getString("categoryTitle", "");
            categoryId = getArguments().getString("categoryId", "");
            sourceId = getArguments().getString("sourceId", "");
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
        setRecyclerView(recyclerView);

        prepareRefreshLayout();
        prepareRecyclerView();
        prepareChipGroup();

        parentFragment().showProgressBar();

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
                ((Chip) chipGroup.findViewById(lastCheckedId)).setChecked(false);
                lastCheckedId = -1;
                selectedTopic = "";
            } else {
                lastCheckedId = checkedId;
                selectedTopic = topics.get(checkedId).id;
            }

            loadData();
        });

        if (lastCheckedId != -1) {
            ((Chip) chipGroup.findViewById(lastCheckedId)).setChecked(true);
            selectedTopic = topics.get(lastCheckedId).id;
        }
    }

    private void loadData() {
        RequestBuilder builder = RequestBuilder.create()
                .method("news")
                .put("category", categoryId);

        if (!sourceId.isEmpty()) {
            builder.put("source", sourceId);
        }

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
                parentFragment().hideProgressBar();
            }
        });
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
            adapter = new NewsAdapter(requireContext(), new ArrayList<>(news));
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(this);
            adapter.setOnMoreClickListener(this::onMoreClick);

            parentFragment().hideProgressBar();
            return;
        }

        adapter.changeItems(new ArrayList<>(news));
        adapter.notifyDataSetChanged();
    }

    private FragmentHeadlines parentFragment() {
        return (FragmentHeadlines) getParentFragment();
    }

    private void refreshData() {
        FragmentHeadlines fragment = (FragmentHeadlines) requireParentFragment();
        fragment.currentCategory = categoryTitle;
        fragment.currentTopicId = lastCheckedId;

        fragment.loadCategories();

        loadData();
    }

    private void onMoreClick(View view, int position) {
        FragmentNews.showMoreItems(adapter, position, requireActivity(), view);
    }

    @Override
    public void onItemClick(int position) {
        FragmentNews.openNewsPost(adapter, position, requireActivity());
    }

}
