package org.ilri.eweigh.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.ilri.eweigh.cattle.models.Cattle;

import java.util.List;

/**
 *
 * CattleDao
 *
 * Store a list of cattle
 *
 *
 * */

@Dao
public interface CattleDao {

    @Query("SELECT * FROM cattle ORDER BY id DESC")
    LiveData<List<Cattle>> get();

    @Query("SELECT COUNT(id) FROM cattle")
    int getCount();

    @Query("SELECT * FROM cattle WHERE id = :id")
    Cattle getCattle(int id);

    @Insert
    void insert(Cattle... cattle);

    @Update
    void update(Cattle cattle);

    @Delete
    void delete(Cattle cattle);

    @Query("DELETE FROM cattle")
    void delete();
}
