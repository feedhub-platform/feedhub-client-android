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
import com.feedhub.app.common.TaskManager;
import com.feedhub.app.current.BaseFragment;
import com.feedhub.app.item.News;
import com.feedhub.app.item.Topic;
import com.feedhub.app.net.HttpRequest;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentHeadlinesItem extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private String categoryTitle;
    private String categoryId;
    private ArrayList<Topic> topics;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.headlinesItemChip)
    ChipGroup chipGroup;

    private int lastCheckedId;

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

        if (getArguments() != null) {
            categoryTitle = getArguments().getString("title", "");
            categoryId = getArguments().getString("id", "");
            topics = (ArrayList<Topic>) getArguments().getSerializable("topics");
        }
    }

    @Override
    public void onRefresh() {
        insertTestData();
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

        insertTestData();
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

        Chip chip = (Chip) chipGroup.getChildAt(0);

        if (chip != null) chip.setChecked(true);

//        ArrayList<Topic> items = new ArrayList<>();
//
//        TaskManager.execute(() -> {
//            try {
//                String serverUrl =
//                        AppGlobal.preferences.getString(FragmentSettings.KEY_SERVER_URL, "") + "/" +
//                                AppGlobal.preferences.getString(FragmentSettings.KEY_TOPICS_KEY, "") +
//                                "?category=" + categoryId;
//
//                JSONObject root = new JSONObject(HttpRequest.get(serverUrl).asString());
//                JSONObject response = Objects.requireNonNull(root.optJSONObject("response"));
//                JSONArray topics = Objects.requireNonNull(response.optJSONArray("topics").optJSONObject(0).optJSONArray("items"));
//
//                for (int i = 0; i < topics.length(); i++) {
//                    items.add(new Topic(topics.optJSONObject(i)));
//                }
//
//                AppGlobal.handler.post(() -> {
//                    for (int i = 0; i < items.size(); i++) {
//                        Topic topic = items.get(i);
//
//                        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.fragment_headlines_item_chip, chipGroup, false);
//                        chip.setId(i);
//                        chip.setTag(topic.id);
//                        chip.setText(topic.title);
//
//                        chipGroup.addView(chip);
//                    }
//
//                    ((Chip) chipGroup.getChildAt(0)).setChecked(true);
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });

        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == -1) {
                checkedId = lastCheckedId;
                ((Chip) chipGroup.findViewById(checkedId)).setChecked(true);
            } else {
                lastCheckedId = checkedId;
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

    private void insertTestData() {
        int headlinesCount = new Random().nextInt();
        if (headlinesCount == 0) headlinesCount = 1;


        TaskManager.execute(() -> {
            ArrayList<News> items = new ArrayList<>(AppGlobal.database.newsDao().getAll());
            HeadlineAdapter adapter = new HeadlineAdapter(requireContext(), items);
            recyclerView.post(() -> recyclerView.setAdapter(adapter));
        });


//        for (int i = 0; i < new Random().nextInt(20); i++) {
//            News news = new News();
//            news.body = "Some nigga body";
//            news.picture = "https://i.ytimg.com/vi/A-_Vl6fHPFo/hqdefault.jpg";
//            news.title = "Some nigga title";
//
//            items.add(news);
//        }


        if (refreshLayout.isRefreshing()) refreshLayout.setRefreshing(false);
    }
}
