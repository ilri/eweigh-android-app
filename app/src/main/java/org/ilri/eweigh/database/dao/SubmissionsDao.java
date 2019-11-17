package org.ilri.eweigh.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.ilri.eweigh.hg_lw.models.Submission;

import java.util.List;

/**
 *
 * SubmissionsDao
 *
 * Store a list of live weight submissions
 *
 *
 * */

@Dao
public interface SubmissionsDao {

    @Query("SELECT * FROM submissions ORDER BY createdOn ASC")
    LiveData<List<Submission>> get();

    @Query("SELECT * FROM submissions WHERE cattleId = :id")
    LiveData<List<Submission>> getCattleSubmission(int id);

    @Query("SELECT COUNT(id) FROM submissions")
    int getCount();

    @Insert
    void insert(Submission... submission);

    @Update
    void update(Submission submission);

    @Delete
    void delete(Submission... submission);

    @Query("DELETE FROM submissions")
    void delete();
}
