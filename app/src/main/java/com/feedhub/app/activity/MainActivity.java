package com.feedhub.app.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.feedhub.app.R;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.common.TaskManager;
import com.feedhub.app.current.BaseActivity;
import com.feedhub.app.current.BaseFragment;
import com.feedhub.app.fragment.FragmentFavorites;
import com.feedhub.app.fragment.FragmentHeadlines;
import com.feedhub.app.fragment.FragmentNews;
import com.feedhub.app.fragment.FragmentSettings;
import com.feedhub.app.fragment.FragmentSources;
import com.feedhub.app.fragment.FragmentSubscriptions;
import com.feedhub.app.net.NetUtils;
import com.feedhub.app.net.RequestBuilder;
import com.feedhub.app.util.LocaleUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.melod1n.library.fragment.FragmentSwitcher;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public final FragmentHeadlines fragmentHeadlines = new FragmentHeadlines();
    private final FragmentNews fragmentNews = new FragmentNews();
    private final FragmentSubscriptions fragmentSubscriptions = new FragmentSubscriptions();
    private final FragmentFavorites fragmentFavorites = new FragmentFavorites();
    private final FragmentSources fragmentSources = new FragmentSources();

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView navigationView;

    @SuppressLint("PrivateResource")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        prepareBottomNavView();

        sendRequest();
    }

    public void sendRequest() {
        TaskManager.execute(() -> {
            String link = "https://api.innomaxx.dev/feedhub/relay";

            RequestBuilder.create()
                    .baseUrl(link)
                    .execute(new RequestBuilder.OnResponseListener<JSONObject>() {

                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                String baseUrl = response.optString("baseUrl");

                                boolean isLocked = response.optBoolean("isLocked", true);

                                AppGlobal.preferences.edit().putBoolean(FragmentSettings.KEY_SERVER_URL_BLOCKED, isLocked).apply();

                                JSONObject title = Objects.requireNonNull(response.optJSONObject("title"));

                                String en = title.optString("en", "");
                                String ru = title.optString("ru", "");
                                String uk = title.optString("uk", "");

                                String transformedUrl = NetUtils.transformUrl(baseUrl);

                                Locale currentLocale = LocaleUtils.getCurrentLocale(MainActivity.this);

                                String strLocale = currentLocale.toString();

                                String serverSummary = "";

                                switch (strLocale) {
                                    case "en":
                                        serverSummary = en;
                                        break;
                                    case "ru":
                                        serverSummary = ru;
                                        break;
                                    case "uk":
                                        serverSummary = uk;
                                        break;
                                }

                                AppGlobal.preferences.edit()
                                        .putString(FragmentSettings.KEY_SERVER_URL_SUMMARY, serverSummary)
                                        .putString(FragmentSettings.KEY_SERVER_URL_SUMMARY_EN, en)
                                        .putString(FragmentSettings.KEY_SERVER_URL_SUMMARY_RU, ru)
                                        .putString(FragmentSettings.KEY_SERVER_URL_SUMMARY_UK, uk)
                                        .putString(FragmentSettings.KEY_SERVER_URL, transformedUrl)
                                        .apply();

                                RequestBuilder.updateBaseUrl(transformedUrl);

                                runOnUiThread(() -> {
                                    prepareFragments();
                                    replaceFragment(fragmentNews);
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });
        });
    }

    private void prepareFragments() {
        View container = findViewById(R.id.fragmentContainer);
        container.setVisibility(View.VISIBLE);
        container.setAlpha(0);
        container.setClickable(false);
        container.setFocusable(false);

        int containerId = container.getId();

        FragmentSwitcher.addFragments(
                getSupportFragmentManager(),
                containerId,
                Arrays.asList(fragmentNews, fragmentHeadlines, fragmentSubscriptions, fragmentFavorites, fragmentSources)
        );

        FragmentSwitcher.showFragment(getSupportFragmentManager(), fragmentNews.getClass().getSimpleName(), null, true);

//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(containerId, fragmentGeneral, fragmentGeneral.getClass().getSimpleName())
//                .add(containerId, fragmentHeadlines, fragmentHeadlines.getClass().getSimpleName())
//                .add(containerId, fragmentFollowing, fragmentFollowing.getClass().getSimpleName())
//                .add(containerId, fragmentSaved, fragmentSaved.getClass().getSimpleName())
//                .commit();


        container.animate().alpha(1).setDuration(500).withEndAction(() -> {
            container.setClickable(true);
            container.setFocusable(true);
        }).start();
//        container.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigationGeneral:
                replaceFragment(fragmentNews);
                return true;
            case R.id.navigationHeadlines:
                replaceFragment(fragmentHeadlines);
                return true;
            case R.id.navigationSubscriptions:
                replaceFragment(fragmentSubscriptions);
                return true;
            case R.id.navigationFavorites:
                replaceFragment(fragmentFavorites);
                return true;
            case R.id.navigationSources:
                replaceFragment(fragmentSources);
                return true;
        }
        return false;
    }

    private void prepareBottomNavView() {
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setOnNavigationItemReselectedListener(this::onReselect);
    }

    private void onReselect(@NonNull MenuItem item) {
        BaseFragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigationGeneral:
                fragment = fragmentNews;
                break;
            case R.id.navigationHeadlines:
                fragment = fragmentHeadlines;
                break;
            case R.id.navigationSubscriptions:
                fragment = fragmentSubscriptions;
                break;
            case R.id.navigationFavorites:
                fragment = fragmentFavorites;
                break;
            case R.id.navigationSources:
                fragment = fragmentSources;
                break;
        }

        if (fragment != null && fragment.getRecyclerView() != null) {
            fragment.getRecyclerView().smoothScrollToPosition(0);
        }
    }

    private void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, null);
    }

    public void replaceFragment(@NonNull Fragment fragment, Bundle arguments) {
        FragmentSwitcher.showFragment(getSupportFragmentManager(), fragment.getTag(), arguments, true);
    }

    public BottomNavigationView getNavigationView() {
        return navigationView;
    }
}
