package org.ilri.eweigh.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.ilri.eweigh.feeds.Feed;

import java.util.List;

/**
 *
 * FeedsDao
 *
 * Handles database access to Feeds data
 *
 *
 * */

@Dao
public interface FeedsDao {

    @Query("SELECT * FROM feeds")
    LiveData<List<Feed>> get();

    @Query("SELECT * FROM feeds WHERE feedType = :feedType")
    LiveData<List<Feed>> getAllByFeedType(String feedType);

    @Query("SELECT COUNT(id) FROM feeds")
    int getCount();

    @Query("SELECT * FROM feeds WHERE id = :id")
    Feed getFeed(int id);

    @Insert
    void insert(Feed... feed);

    @Update
    void update(Feed content);

    @Delete
    void delete(Feed feed);

    @Query("DELETE FROM feeds")
    void delete();
}
