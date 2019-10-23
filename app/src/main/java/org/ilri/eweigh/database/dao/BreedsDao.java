package org.ilri.eweigh.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.ilri.eweigh.cattle.Breed;

import java.util.List;

/**
 *
 * BreedsDao
 *
 * Store a list of cattle breeds
 *
 *
 * */

@Dao
public interface BreedsDao {

    @Query("SELECT * FROM breeds ORDER BY breed ASC")
    LiveData<List<Breed>> get();

    @Query("SELECT COUNT(id) FROM breeds")
    int getCount();

    @Query("SELECT * FROM breeds WHERE id = :id")
    Breed getBreed(int id);

    @Insert
    void insert(Breed... breed);

    @Update
    void update(Breed breed);

    @Delete
    void delete(Breed breed);

    @Query("DELETE FROM breeds")
    void delete();
}
