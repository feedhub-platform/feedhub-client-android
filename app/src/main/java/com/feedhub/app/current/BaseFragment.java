package com.feedhub.app.current;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.feedhub.app.R;
import com.feedhub.app.widget.Toolbar;

public abstract class BaseFragment extends Fragment {

    protected Toolbar toolbar;

    protected void initToolbar(@IdRes int resId) {
        toolbar = requireView().findViewById(resId);
    }

    protected BaseActivity activity() {
        return (BaseActivity) requireActivity();
    }
}
