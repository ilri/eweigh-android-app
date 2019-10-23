package org.ilri.eweigh.database.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.ilri.eweigh.cattle.Breed;
import org.ilri.eweigh.database.AppDatabase;
import org.ilri.eweigh.database.dao.BreedsDao;

import java.util.List;

public class BreedsViewModel extends AndroidViewModel {
    private BreedsDao breedsDao;
    private LiveData<List<Breed>> breedsLiveData;
    private int count;

    public BreedsViewModel(@NonNull Application application) {
        super(application);

        breedsDao = AppDatabase.getAppDatabase(application).breedsDao();
        breedsLiveData = breedsDao.get();
        count = breedsDao.getCount();
    }

    public LiveData<List<Breed>> getAll(){
        return breedsLiveData;
    }

    public int getCount(){
        return count;
    }

    public Breed getBreed(int id){
        return breedsDao.getBreed(id);
    }

    public void insert(Breed breed){
        breedsDao.insert(breed);
    }

    public void update(Breed breed){
        breedsDao.update(breed);
    }

    public void delete(Breed breed){
        breedsDao.delete(breed);
    }

    public void deleteAll(){
        breedsDao.delete();
    }
}
