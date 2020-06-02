package com.feedhub.app.current;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.fragment.FragmentSettings;
import com.feedhub.app.util.Utils;

public abstract class BaseActivity extends AppCompatActivity {

    public BaseActivity() {
//        updateLocale();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        updateLocale();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateLocale();
    }

    protected void updateLocale() {
        String strLocale = AppGlobal.preferences.getString(FragmentSettings.KEY_LANGUAGE, Utils.LOCALE_EN);

        Utils.changeLocale(getBaseContext(), strLocale);
        AppGlobal.updateLocale(getBaseContext(), strLocale);
    }
}
