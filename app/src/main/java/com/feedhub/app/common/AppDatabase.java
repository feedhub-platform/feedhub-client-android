package com.feedhub.app.common;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.feedhub.app.dao.HeadlinesDao;
import com.feedhub.app.dao.NewsDao;
import com.feedhub.app.item.Headline;
import com.feedhub.app.item.News;

@Database(entities = {News.class, Headline.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();

    public abstract HeadlinesDao headlinesDao();
}