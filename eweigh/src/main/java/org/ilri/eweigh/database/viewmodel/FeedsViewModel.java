package org.ilri.eweigh.database.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.ilri.eweigh.database.AppDatabase;
import org.ilri.eweigh.feeds.Feed;
import org.ilri.eweigh.database.dao.FeedsDao;

import java.util.List;

public class FeedsViewModel extends AndroidViewModel {
    private FeedsDao feedsDao;
    private LiveData<List<Feed>> feedLiveData;
    private int count;

    public FeedsViewModel(@NonNull Application application) {
        super(application);
        feedsDao = AppDatabase.getAppDatabase(application).feedsDao();
        feedLiveData = feedsDao.get();
        count = feedsDao.getCount();
    }

    public LiveData<List<Feed>> getAll(){
        return feedLiveData;
    }

    public LiveData<List<Feed>> getAllByFeedType(String feedType){
        return feedsDao.getAllByFeedType(feedType);
    }

    public int getCount(){
        return count;
    }

    public Feed getFeed(int id){
        return feedsDao.getFeed(id);
    }

    public void insert(Feed feed){
        feedsDao.insert(feed);
    }

    public void update(Feed feed){
        feedsDao.update(feed);
    }

    public void delete(Feed feed){
        feedsDao.delete(feed);
    }

    public void deleteAll(){
        feedsDao.delete();
    }
}
