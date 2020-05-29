package com.feedhub.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.feedhub.app.item.Favorite;

import java.util.List;

@Dao
public interface FavoritesDao {

    @Query("SELECT * FROM favorite")
    List<Favorite> getAll();

    @Query("SELECT * FROM headline WHERE id = :id")
    Favorite getById(String id);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert
    void insert(Favorite favorite);

    @Update
    void update(Favorite favorite);

    @Delete
    void delete(Favorite favorite);

    @Query("DELETE FROM favorite")
    void clear();

}
