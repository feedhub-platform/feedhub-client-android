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
import com.feedhub.app.activity.SettingsActivity;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.net.RequestBuilder;
import com.feedhub.app.util.LocaleUtils;
import com.feedhub.app.util.StringUtils;
import com.feedhub.app.util.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class FragmentSettings extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    public static final String KEY_SERVER_URL = "server_url";
    public static final String KEY_NEWS_KEY = "news_key";
    public static final String KEY_CATEGORY_KEY = "category_key";
    public static final String KEY_TOPICS_KEY = "topics_key";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_NEWS_LANGUAGE = "news_language";

    public static final String KEY_SERVER_URL_BLOCKED = "server_url_blocked";
    public static final String KEY_SERVER_URL_SUMMARY = "server_summary";

    public static final String KEY_SERVER_URL_SUMMARY_EN = "server_summary_en";
    public static final String KEY_SERVER_URL_SUMMARY_RU = "server_summary_ru";
    public static final String KEY_SERVER_URL_SUMMARY_UK = "server_summary_uk";

    public static final String KEY_NEWS_KEY_DV = "news";
    public static final String KEY_CATEGORY_KEY_DV = "platform/categories";
    public static final String KEY_TOPICS_KEY_DV = "platform/topics";

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);

        Preference serverUrl = Objects.requireNonNull(findPreference(KEY_SERVER_URL));

        boolean isLocked = AppGlobal.preferences.getBoolean(KEY_SERVER_URL_BLOCKED, true);
        serverUrl.setEnabled(!isLocked);

        if (isLocked) {
            setPreferenceValueSummary(KEY_SERVER_URL,
                    AppGlobal.preferences.getString(KEY_SERVER_URL_SUMMARY, "")
            );
        } else {
            setPreferenceValueSummary(KEY_SERVER_URL, (String) null);
        }

        Preference newsKey = Objects.requireNonNull(findPreference(KEY_NEWS_KEY));
        setPreferenceValueSummary(KEY_NEWS_KEY, (String) null);
        newsKey.setVisible(false);

        Preference categoryKey = Objects.requireNonNull(findPreference(KEY_CATEGORY_KEY));
        setPreferenceValueSummary(KEY_CATEGORY_KEY, (String) null);
        categoryKey.setVisible(false);

        Preference topicsKey = Objects.requireNonNull(findPreference(KEY_TOPICS_KEY));
        setPreferenceValueSummary(KEY_TOPICS_KEY, (String) null);
        topicsKey.setVisible(false);

        Preference language = Objects.requireNonNull(findPreference(KEY_LANGUAGE));
        setPreferenceValueSummary(KEY_LANGUAGE, (String) null);

        Preference newsLanguage = Objects.requireNonNull(findPreference(KEY_NEWS_LANGUAGE));
        setPreferenceValueSummary(KEY_NEWS_LANGUAGE, (Set<String>) null);

        serverUrl.setOnPreferenceChangeListener(this);
        newsKey.setOnPreferenceChangeListener(this);
        categoryKey.setOnPreferenceChangeListener(this);
        topicsKey.setOnPreferenceChangeListener(this);
        language.setOnPreferenceChangeListener(this);
        newsLanguage.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    @SuppressLint("PrivateResource")
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(KEY_SERVER_URL)) {
            String url = (String) newValue;

            if (url.isEmpty()) return false;

            String s = url.substring(url.length() - 1);

            if (!s.equals("/")) {
                url += "/";

                AppGlobal.preferences.edit()
                        .putString(preference.getKey(), url)
                        .apply();
            }

            setPreferenceValueSummary(preference.getKey(), url);

            RequestBuilder.updateBaseUrl(url);
            return false;
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

                String en = AppGlobal.preferences.getString(KEY_SERVER_URL_SUMMARY_EN, "");
                String ru = AppGlobal.preferences.getString(KEY_SERVER_URL_SUMMARY_RU, "");
                String uk = AppGlobal.preferences.getString(KEY_SERVER_URL_SUMMARY_UK, "");

                Locale currentLocale = LocaleUtils.getCurrentLocale(requireContext());

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
                        .putString(KEY_SERVER_URL_SUMMARY, serverSummary)
                        .apply();

                AppGlobal.updateLocale(requireActivity().getBaseContext(), (String) newValue);
//                LocaleHelper.setLocale(requireContext(), (String) newValue);
                restartActivities();
                return true;
            case KEY_NEWS_LANGUAGE:
                HashSet<String> values = (HashSet<String>) newValue;

                if (values.isEmpty()) {
                    String[] languages = getResources().getStringArray(R.array.languages_values);

                    values = new HashSet<>(Arrays.asList(languages));

                    AppGlobal.preferences.edit()
                            .putStringSet(preference.getKey(), values)
                            .apply();
                }

                setPreferenceValueSummary(preference.getKey(), values);
                return true;
        }
        return false;
    }

    @SuppressLint("PrivateResource")
    private void restartActivities() {
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(requireActivity().getApplicationContext())
                .addNextIntent(new Intent(requireContext(), MainActivity.class))
                .addNextIntent(new Intent(requireContext(), SettingsActivity.class));

        requireActivity().finishAffinity();

        stackBuilder.startActivities();

        requireActivity().overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    private void setPreferenceValueSummary(@NonNull String key, @Nullable Set<String> values) {
        StringBuilder builder = new StringBuilder();

        if (values == null) values = AppGlobal.preferences.getStringSet(key, null);
        if (values == null || values.isEmpty()) return;

        Object[] strings = values.toArray();

        builder.append(StringUtils.getLanguageByCode(requireContext(), (String) strings[0]));

        if (values.size() > 1) {
            for (int i = 1; i < strings.length; i++) {
                builder.append(", ");
                builder.append(StringUtils.getLanguageByCode(requireContext(), (String) strings[i]));
            }
        }

        setPreferenceSummary(key, builder.toString());
    }

    private void setPreferenceValueSummary(@NonNull String key, @Nullable String value) {
        String summary = (value == null ? AppGlobal.preferences.getString(key, "") : value).trim();

        if (key.equals(KEY_LANGUAGE))
            summary = StringUtils.getLanguageByCode(requireContext(), summary);

        setPreferenceSummary(key, summary);
    }

    private void setPreferenceSummary(@NonNull String preferenceKey, @Nullable String summary) {
        Preference preference = findPreference(preferenceKey);

        if (preference != null) {
            preference.setSummary(summary);
        }
    }
}