package org.ilri.eweigh.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.ilri.eweigh.cattle.models.Breed;
import org.ilri.eweigh.cattle.models.Dosage;

import java.util.List;

/**
 *
 * DosagesDao
 *
 * Store a list dosages for different diseases
 *
 * */

@Dao
public interface DosagesDao {

    @Query("SELECT * FROM dosages GROUP BY disease ORDER BY disease ASC")
    List<Dosage> getDiseases();

    @Query("SELECT * FROM dosages WHERE diseaseId = :diseaseId " +
            "GROUP BY chemicalAgent ORDER BY chemicalAgent ASC")
    List<Dosage> getChemicalAgents(int diseaseId);

    @Query("SELECT COUNT(id) FROM dosages")
    int getCount();

    @Query("SELECT * FROM dosages WHERE id = :id")
    Breed getDosage(int id);

    @Insert
    void insert(Dosage... dosage);

    @Query("DELETE FROM dosages")
    void delete();
}
