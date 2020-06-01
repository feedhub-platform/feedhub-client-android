package com.feedhub.app.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.facebook.drawee.view.SimpleDraweeView;
import com.feedhub.app.R;
import com.feedhub.app.current.BaseAdapter;
import com.feedhub.app.current.BaseHolder;
import com.feedhub.app.item.News;
import com.feedhub.app.util.StringUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends BaseAdapter<News, NewsAdapter.ItemHolder> {

    private OnMoreClickListener onMoreClickListener;

    public NewsAdapter(@NonNull Context context, @NonNull ArrayList<News> values) {
        super(context, values);
    }

    public void setOnMoreClickListener(OnMoreClickListener onMoreClickListener) {
        this.onMoreClickListener = onMoreClickListener;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(view(R.layout.item_news, parent));
    }

    @Override
    public boolean onQueryItem(@NonNull News item, @NonNull String lowerQuery) {
        return item.title.toLowerCase().contains(lowerQuery) ||
                item.body.toLowerCase().contains(lowerQuery);
    }

    public interface OnMoreClickListener {
        void onClick(View view, int position);
    }

    class ItemHolder extends BaseHolder {

        private final int MAX_TITLE_LENGTH = 100;
        private final int MAX_BODY_LENGTH = 400;

        @BindView(R.id.newsRoot)
        View root;

        @BindView(R.id.newsCard)
        MaterialCardView cardView;

        @BindView(R.id.newsTitle)
        TextView title;

        @BindView(R.id.newsBody)
        TextView body;

        @BindView(R.id.newsPicture)
        RoundedImageView picture;

        @BindView(R.id.newsWidePicture)
        SimpleDraweeView widePicture;

        @BindView(R.id.newsLanguage)
        Chip language;

        @BindView(R.id.newsOriginTitle)
        Chip originTitle;

        @BindView(R.id.newsMore)
        ImageButton more;

        ItemHolder(@NonNull View v) {
            super(v);

            ButterKnife.bind(this, v);
        }

        @Override
        public void bind(int position) {
            currentPosition = position;

            News item = getItem(position);

            language.setText(item.language.toUpperCase());

            originTitle.setText(item.originTitle);

            String sTitle = StringUtils.cutString(item.title, MAX_TITLE_LENGTH, true);
            title.setText(sTitle);

            String sBody = StringUtils.cutString(item.body, MAX_BODY_LENGTH, true);
            body.setText(sBody);

            String sPicture = item.picture;

            widePicture.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(sPicture)) {
                widePicture.setVisibility(View.GONE);
                picture.setVisibility(View.GONE);
            } else {
                widePicture.setImageURI(Uri.parse(sPicture));
            }

            more.setOnClickListener(v -> {
                if (onMoreClickListener != null) onMoreClickListener.onClick(v, position);
            });
        }
    }
}
