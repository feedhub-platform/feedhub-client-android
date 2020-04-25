package com.feedhub.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.feedhub.app.R;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.current.BaseAdapter;
import com.feedhub.app.current.BaseHolder;
import com.feedhub.app.item.News;
import com.feedhub.app.util.AndroidUtils;
import com.feedhub.app.util.StringUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends BaseAdapter<News, NewsAdapter.ItemHolder> {

//    private static final int TYPE_HEADER = 2001;

    public NewsAdapter(@NonNull Context context, @NonNull ArrayList<News> values) {
        super(context, values);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == TYPE_HEADER) return new HeaderHolder(generateEmptyView());
        return new ItemHolder(view(R.layout.item_news, parent));
    }
//
//    @Override
//    public News getItem(int position) {
//        return super.getItem(position - 1);
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (position == 0) return TYPE_HEADER;
//        return super.getItemViewType(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return super.getItemCount() + 1;
//    }
//
//    private View generateEmptyView() {
//        View view = new View(context);
//        view.setClickable(false);
//        view.setFocusable(false);
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AndroidUtils.px(56)));
//
//        return view;
//    }
//
//    class HeaderHolder extends ItemHolder {
//
//        HeaderHolder(@NonNull View v) {
//            super(v);
//        }
//
//        @Override
//        public void bind(int position) {
//            currentPosition = 0;
//        }
//    }

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

//            if (this instanceof HeaderHolder) return;

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
                widePicture.setImageDrawable(null);

                picture.setVisibility(View.GONE);
                picture.setImageDrawable(null);
            } else {
                new Thread(() -> {
                    try {
                        Bitmap bitmap = Picasso.get().load(sPicture).get();

//                        float h = bitmap.getHeight();
//                        float w = bitmap.getWidth();

                        AppGlobal.handler.post(() -> {
                            widePicture.setImageBitmap(bitmap);
//                            if (w / h > 1.5) {
//                                picture.setVisibility(View.GONE);
//                                widePicture.setVisibility(View.VISIBLE);
//
//                                widePicture.setImageBitmap(bitmap);
//                            } else {
//                                picture.setVisibility(View.VISIBLE);
//                                widePicture.setVisibility(View.GONE);
//
//                                picture.setImageBitmap(bitmap);
//                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }
}
