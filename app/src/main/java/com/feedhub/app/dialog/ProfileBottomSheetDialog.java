package com.feedhub.app.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.feedhub.app.R;
import com.feedhub.app.activity.AboutActivity;
import com.feedhub.app.activity.SettingsActivity;
import com.feedhub.app.adapter.MainMenuAdapter;
import com.feedhub.app.current.BaseAdapter;
import com.feedhub.app.item.MainMenuItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileBottomSheetDialog extends BottomSheetDialogFragment implements MainMenuAdapter.OnItemClickListener {

    private static final String TAG = "ProfileDialog";

    private static final String ID_SETTINGS = "_settings";
    private static final String ID_ABOUT = "_about";
    @BindView(R.id.bottomSheetItems)
    RecyclerView recyclerView;
    private BaseAdapter.OnItemClickListener listener;
    private MainMenuAdapter adapter;

    public static void show(FragmentManager fragmentManager) {
        ProfileBottomSheetDialog dialog = new ProfileBottomSheetDialog();
        dialog.show(fragmentManager, TAG);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        ArrayList<MainMenuItem> items = new ArrayList<>();

        items.add(new MainMenuItem(
                ID_SETTINGS,
                getString(R.string.navigation_settings),
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_settings_outline)
        ));

        items.add(new MainMenuItem(
                ID_ABOUT,
                getString(R.string.preference_about),
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_outline_info)
        ));

        adapter = new MainMenuAdapter(requireContext(), items);
        adapter.setOnItemClickListener(this);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        if (listener != null) {
            listener.onItemClick(position);
            return;
        }

        MainMenuItem item = adapter.getItem(position);

        switch (item.id) {
            case ID_SETTINGS:
                openSettingsScreen();
                break;
            case ID_ABOUT:
                openAboutScreen();
                break;
        }

        dismiss();
    }

    private void openAboutScreen() {
        startActivity(new Intent(requireContext(), AboutActivity.class));
    }

    private void openSettingsScreen() {
        startActivity(new Intent(requireContext(), SettingsActivity.class));
    }

    public void setOnItemClickListener(BaseAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
