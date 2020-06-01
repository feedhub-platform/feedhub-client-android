package com.feedhub.app.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.TaskStackBuilder;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.feedhub.app.R;
import com.feedhub.app.activity.MainActivity;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.net.RequestBuilder;
import com.feedhub.app.util.Utils;

import java.util.Objects;

public class FragmentSettings extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    public static final String KEY_SERVER_URL = "server_url";
    public static final String KEY_NEWS_KEY = "news_key";
    public static final String KEY_CATEGORY_KEY = "category_key";
    public static final String KEY_TOPICS_KEY = "topics_key";
    public static final String KEY_LANGUAGE = "language";

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);

        Preference serverUrl = Objects.requireNonNull(findPreference(KEY_SERVER_URL));
        setPreferenceValueSummary(KEY_SERVER_URL, null);

        Preference newsKey = Objects.requireNonNull(findPreference(KEY_NEWS_KEY));
        setPreferenceValueSummary(KEY_NEWS_KEY, null);
        newsKey.setEnabled(false);

        Preference categoryKey = Objects.requireNonNull(findPreference(KEY_CATEGORY_KEY));
        setPreferenceValueSummary(KEY_CATEGORY_KEY, null);
        categoryKey.setEnabled(false);

        Preference topicsKey = Objects.requireNonNull(findPreference(KEY_TOPICS_KEY));
        setPreferenceValueSummary(KEY_TOPICS_KEY, null);
        topicsKey.setEnabled(false);

        Preference language = Objects.requireNonNull(findPreference(KEY_LANGUAGE));
        setPreferenceValueSummary(KEY_LANGUAGE, null);

        serverUrl.setOnPreferenceChangeListener(this);
        newsKey.setOnPreferenceChangeListener(this);
        categoryKey.setOnPreferenceChangeListener(this);
        topicsKey.setOnPreferenceChangeListener(this);
        language.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    @SuppressLint("PrivateResource")
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(KEY_SERVER_URL)) {
            RequestBuilder.updateBaseUrl((String) newValue);
        }

        switch (preference.getKey()) {
            case KEY_SERVER_URL:
            case KEY_NEWS_KEY:
            case KEY_CATEGORY_KEY:
            case KEY_TOPICS_KEY:
                setPreferenceValueSummary(preference.getKey(), (String) newValue);
                restartActivities();
                return true;
            case KEY_LANGUAGE:
                Utils.changeLocale(
                        requireActivity().getBaseContext(),
                        (String) newValue
                );

                AppGlobal.updateLocale(requireActivity().getBaseContext(), (String) newValue);
//                LocaleHelper.setLocale(requireContext(), (String) newValue);
                restartActivities();
                return true;
        }
        return false;
    }

    @SuppressLint("PrivateResource")
    private void restartActivities() {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(requireContext())
                .addNextIntent(new Intent(requireContext(), MainActivity.class))
                .addNextIntent(requireActivity().getIntent());

        requireActivity().finishAffinity();

        stackBuilder.startActivities();

        requireActivity().overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
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