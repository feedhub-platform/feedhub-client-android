package com.feedhub.app.common;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.feedhub.app.dao.NewsDao;
import com.feedhub.app.item.News;

@Database(entities = {News.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
   public abstract NewsDao newsDao();
}