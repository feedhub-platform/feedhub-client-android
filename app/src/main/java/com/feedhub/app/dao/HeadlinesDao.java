package com.feedhub.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.feedhub.app.item.Headline;
import com.feedhub.app.item.News;

import java.util.List;

@Dao
public interface HeadlinesDao {

    @Query("SELECT * FROM headline")
    List<Headline> getAll();

    @Query("SELECT * FROM headline WHERE id = :id")
    Headline getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Headline headline);

    @Update
    void update(Headline headline);

    @Delete
    void delete(Headline headline);

    @Query("DELETE FROM headline")
    void clear();

}
