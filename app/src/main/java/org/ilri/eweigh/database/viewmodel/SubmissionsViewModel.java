package org.ilri.eweigh.database.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.ilri.eweigh.database.AppDatabase;
import org.ilri.eweigh.database.dao.SubmissionsDao;
import org.ilri.eweigh.hg_lw.models.Submission;

import java.util.List;

public class SubmissionsViewModel extends AndroidViewModel {
    private SubmissionsDao submissionsDao;
    private LiveData<List<Submission>> submissionsLiveData;
    private int count;

    public SubmissionsViewModel(@NonNull Application application) {
        super(application);

        submissionsDao = AppDatabase.getAppDatabase(application).submissionsDao();
        submissionsLiveData = submissionsDao.get();
        count = submissionsDao.getCount();
    }

    public LiveData<List<Submission>> getAll(){
        return submissionsLiveData;
    }

    public LiveData<List<Submission>> getCattleSubmissions(int cattleId){
        return submissionsDao.getCattleSubmission(cattleId);
    }

    public int getCount(){
        return count;
    }

    public void insert(Submission submission){
        submissionsDao.insert(submission);
    }

    public void update(Submission submission){
        submissionsDao.update(submission);
    }

    public void delete(Submission submission){
        submissionsDao.delete(submission);
    }

    public void deleteAll(){
        submissionsDao.delete();
    }
}
