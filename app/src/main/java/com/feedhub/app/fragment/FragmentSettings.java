package com.feedhub.app.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.feedhub.app.R;
import com.feedhub.app.common.AppGlobal;

public class FragmentSettings extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    public static final String KEY_SERVER_URL = "server_url";

    private Preference serverUrl;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey);

        serverUrl = findPreference(KEY_SERVER_URL);
        applyServerUrlSummary(null);

        serverUrl.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case KEY_SERVER_URL:


//                AppGlobal.preferences.edit().putString(KEY_SERVER_URL, new).apply();

                applyServerUrlSummary((String) newValue);
                return true;
        }
        return false;
    }

    private void applyServerUrlSummary(@Nullable String url) {
        String summary = url == null ? AppGlobal.preferences.getString(KEY_SERVER_URL, "") : url;

        if (serverUrl != null) {
            serverUrl.setSummary(summary.trim().isEmpty() ? "" : summary);
        }
    }
}
