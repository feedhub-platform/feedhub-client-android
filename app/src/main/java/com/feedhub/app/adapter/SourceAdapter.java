package com.feedhub.app.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.drawee.view.SimpleDraweeView;
import com.feedhub.app.R;
import com.feedhub.app.current.BaseAdapter;
import com.feedhub.app.current.BaseHolder;
import com.feedhub.app.item.Source;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SourceAdapter extends BaseAdapter<Source, SourceAdapter.ItemHolder> {

    public SourceAdapter(@NonNull Context context, @NonNull ArrayList<Source> values) {
        super(context, values);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(view(R.layout.item_source, parent));
    }

    class ItemHolder extends BaseHolder {

        @BindView(R.id.sourceLogo)
        SimpleDraweeView logo;

        @BindView(R.id.sourceTitle)
        TextView title;

        @BindView(R.id.sourceDescription)
        TextView description;

        ItemHolder(@NonNull View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @Override
        public void bind(int position) {
            currentPosition = position;

            Source source = getItem(position);

            logo.setImageURI(Uri.parse(source.frontPictureUrl));

            title.setText(source.title);
            description.setText(source.description);
        }
    }
}
