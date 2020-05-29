package com.feedhub.app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.feedhub.app.R;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.net.RequestBuilder;

import java.util.Objects;

public class FragmentSettings extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    public static final String KEY_SERVER_URL = "server_url";
    public static final String KEY_NEWS_KEY = "news_key";
    public static final String KEY_CATEGORY_KEY = "category_key";
    public static final String KEY_TOPICS_KEY = "topics_key";

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);

        Preference serverUrl = Objects.requireNonNull(findPreference(KEY_SERVER_URL));
        setPreferenceValueSummary(KEY_SERVER_URL, null);

        Preference newsKey = Objects.requireNonNull(findPreference(KEY_NEWS_KEY));
        setPreferenceValueSummary(KEY_NEWS_KEY, null);

        Preference categoryKey = Objects.requireNonNull(findPreference(KEY_CATEGORY_KEY));
        setPreferenceValueSummary(KEY_CATEGORY_KEY, null);

        Preference topicsKey = Objects.requireNonNull(findPreference(KEY_TOPICS_KEY));
        setPreferenceValueSummary(KEY_TOPICS_KEY, null);

        serverUrl.setOnPreferenceChangeListener(this);
        newsKey.setOnPreferenceChangeListener(this);
        categoryKey.setOnPreferenceChangeListener(this);
        topicsKey.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case KEY_SERVER_URL:
                setPreferenceValueSummary(preference.getKey(), (String) newValue);

                RequestBuilder.updateBaseUrl((String) newValue);
                requireActivity().recreate();
                break;
            case KEY_NEWS_KEY:
            case KEY_CATEGORY_KEY:
            case KEY_TOPICS_KEY:
                setPreferenceValueSummary(preference.getKey(), (String) newValue);
                return true;
        }
        return false;
    }

    private void setPreferenceValueSummary(@NonNull String key, @Nullable String value) {
        String summary = (value == null ? AppGlobal.preferences.getString(key, "") : value).trim();

        setPreferenceSummary(key, summary);
    }


    private void setPreferenceSummary(@NonNull String preferenceKey, @Nullable String summary) {
        Preference preference = findPreference(preferenceKey);

        if (preference != null) {
            preference.setSummary(summary);
        }
    }
}
