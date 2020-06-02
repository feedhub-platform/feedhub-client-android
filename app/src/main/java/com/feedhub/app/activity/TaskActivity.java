package com.feedhub.app.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.feedhub.app.R;
import com.feedhub.app.common.AppGlobal;
import com.feedhub.app.common.TaskManager;
import com.feedhub.app.dao.FavoritesDao;
import com.feedhub.app.item.Favorite;
import com.feedhub.app.item.News;

public class TaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkExtras();
    }

    private void checkExtras() {
        if (getIntent().hasExtra("news")) {
            News news = (News) getIntent().getSerializableExtra("news");

            if (news == null) return;

            TaskManager.execute(() -> {
                try {
                    Favorite favorite = new Favorite(news);

                    FavoritesDao dao = AppGlobal.database.favoritesDao();

                    boolean isAdd = getIntent().getBooleanExtra("add", false);

                    if (isAdd) {
                        dao.insert(favorite);
                    } else {
                        dao.delete(favorite);
                    }

                    runOnUiThread(() -> Toast.makeText(
                            TaskActivity.this,
                            isAdd ? R.string.added_to_favorites : R.string.removed_from_favorites,
                            Toast.LENGTH_SHORT
                    ).show());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            finish();
        }
    }
}
