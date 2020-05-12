package com.feedhub.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.feedhub.app.R;
import com.feedhub.app.current.BaseAdapter;
import com.feedhub.app.current.BaseHolder;
import com.feedhub.app.item.MainMenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMenuAdapter extends BaseAdapter<MainMenuItem, MainMenuAdapter.ViewHolder> {

    public MainMenuAdapter(@NonNull Context context, @NonNull ArrayList<MainMenuItem> values) {
        super(context, values);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(view(R.layout.activity_main_bottom_sheet_item, parent));
    }

    class ViewHolder extends BaseHolder {

        @BindView(R.id.mainMenuItem)
        TextView textView;

        ViewHolder(@NonNull View v) {
            super(v);

            ButterKnife.bind(this, v);
        }

        @Override
        public void bind(int position) {
            MainMenuItem item = getItem(position);

            textView.setText(item.title);
            textView.setCompoundDrawablesWithIntrinsicBounds(item.icon, null, null, null);
        }
    }
}
