package com.feedhub.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.feedhub.app.item.Search;

import java.util.List;

@Dao
public interface SearchesDao {

    @Query("SELECT * FROM searches")
    List<Search> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Search search);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Search> searches);

    @Update
    void update(Search search);

    @Delete
    void delete(Search search);

    @Query("DELETE FROM searches")
    void clear();

}