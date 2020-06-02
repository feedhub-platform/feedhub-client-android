package com.feedhub.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.feedhub.app.item.News;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news")
    List<News> getAll();

    @Query("SELECT * FROM news WHERE id = :id")
    News getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(News news);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<News> news);

    @Update
    void update(News news);

    @Delete
    void delete(News news);

    @Query("DELETE FROM news")
    void clear();

}