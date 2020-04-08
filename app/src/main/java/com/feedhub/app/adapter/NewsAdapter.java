package com.feedhub.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsSession;

import com.feedhub.app.R;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.current.BaseAdapter;
import com.feedhub.app.current.BaseHolder;
import com.feedhub.app.item.News;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends BaseAdapter<News, NewsAdapter.ItemHolder> {

    public NewsAdapter(@NonNull Context context, @NonNull ArrayList<News> values) {
        super(context, values);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(view(R.layout.item_news, parent));
    }

    private void openUrl(@NonNull String url) {
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .addDefaultShareMenuItem()
                .setToolbarColor(context.getColor(R.color.primary))
                .setShowTitle(true)
                .build();

        customTabsIntent.launchUrl(context, Uri.parse(url));
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
        RoundedImageView widePicture;

        @BindView(R.id.newsLanguage)
        Chip language;

        @BindView(R.id.newsOriginTitle)
        Chip originTitle;

        ItemHolder(@NonNull View v) {
            super(v);

            ButterKnife.bind(this, v);
        }

        @Override
        public void bind(int position) {
            News item = getItem(position);

            root.setVisibility(View.INVISIBLE);

            language.setText(item.language.toUpperCase());

            originTitle.setText(item.originTitle);

            cardView.setOnClickListener(v -> {
                openUrl(item.originUrl);
            });

            String sTitle = item.title;
            sTitle = sTitle.length() > MAX_TITLE_LENGTH ? sTitle.substring(0, MAX_TITLE_LENGTH - 1) + "..." : sTitle;

            title.setText(sTitle);

            String sBody = item.body;
            sBody = sBody.length() > MAX_BODY_LENGTH ? sBody.substring(0, MAX_BODY_LENGTH - 1) + "..." : sBody;

            body.setText(sBody);

            String sPicture = item.picture;

            if (TextUtils.isEmpty(sPicture)) {
                widePicture.setVisibility(View.GONE);
                widePicture.setImageDrawable(null);

                picture.setVisibility(View.GONE);
                picture.setImageDrawable(null);
            } else {
                new Thread(() -> {
                    try {
                        Bitmap bitmap = Picasso.get().load(sPicture).get();

                        float h = bitmap.getHeight();
                        float w = bitmap.getWidth();

                        AppGlobal.handler.post(() -> {
                            if (w / h > 1.5) {
                                picture.setVisibility(View.GONE);
                                widePicture.setVisibility(View.VISIBLE);

                                widePicture.setImageBitmap(bitmap);
                            } else {
                                picture.setVisibility(View.VISIBLE);
                                widePicture.setVisibility(View.GONE);

                                picture.setImageBitmap(bitmap);
                            }

                            root.setVisibility(View.VISIBLE);
                            root.setAlpha(0);
                            root.animate().alpha(1).setDuration(150).start();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();

                        AppGlobal.handler.post(() -> {
                            root.setVisibility(View.VISIBLE);
                            root.setAlpha(0);
                            root.animate().alpha(1).setDuration(150).start();
                        });
                    }
                }).start();
            }
        }
    }
}
