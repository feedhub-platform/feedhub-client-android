package com.feedhub.app.common;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.feedhub.app.dao.FavoritesDao;
import com.feedhub.app.dao.NewsDao;
import com.feedhub.app.dao.SearchesDao;
import com.feedhub.app.item.Favorite;
import com.feedhub.app.item.News;
import com.feedhub.app.item.Search;

@Database(entities = {News.class, Favorite.class, Search.class}, version = 12, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();

    public abstract FavoritesDao favoritesDao();

    public abstract SearchesDao searchesDao();
}