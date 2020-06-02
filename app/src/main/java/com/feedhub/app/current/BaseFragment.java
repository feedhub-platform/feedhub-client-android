package com.feedhub.app.current;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.feedhub.app.widget.Toolbar;

public abstract class BaseFragment extends Fragment {

    protected Toolbar toolbar;
    private boolean isAttached;

    private RecyclerView recyclerView;

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

    @Nullable
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
}
