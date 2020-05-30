package com.feedhub.app.current;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.feedhub.app.widget.Toolbar;

public abstract class BaseFragment extends Fragment {

    protected Toolbar toolbar;
    private boolean isAttached;

    protected void initToolbar(@IdRes int resId) {
        toolbar = requireView().findViewById(resId);
    }

    protected BaseActivity activity() {
        return (BaseActivity) requireActivity();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        isAttached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isAttached = false;
    }

    public boolean isAttached() {
        return isAttached;
    }

    protected void runOnUiThread(Runnable runnable) {
        requireActivity().runOnUiThread(runnable);
    }
}
