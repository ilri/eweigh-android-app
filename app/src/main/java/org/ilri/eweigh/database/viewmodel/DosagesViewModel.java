package org.ilri.eweigh.database.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.ilri.eweigh.cattle.models.ChemicalAgent;
import org.ilri.eweigh.cattle.models.Disease;
import org.ilri.eweigh.cattle.models.Dosage;
import org.ilri.eweigh.database.AppDatabase;
import org.ilri.eweigh.database.dao.DosagesDao;

import java.util.ArrayList;
import java.util.List;

public class DosagesViewModel extends AndroidViewModel {
    private DosagesDao dosagesDao;

    public DosagesViewModel(@NonNull Application application) {
        super(application);

        dosagesDao = AppDatabase.getAppDatabase(application).dosagesDao();
    }

    public List<Disease> getDiseases(){
        List<Disease> diseases = new ArrayList<>();

        for(Dosage d : dosagesDao.getDiseases()){
            diseases.add(new Disease(d.getDiseaseId(), d.getDisease()));
        }

        return diseases;
    }

    public List<ChemicalAgent> getChemicalAgents(int diseaseId){
        List<ChemicalAgent> agents = new ArrayList<>();

        for(Dosage d : dosagesDao.getChemicalAgents(diseaseId)){
            agents.add(new ChemicalAgent(d.getChemicalAgentId(), d.getChemicalAgent()));
        }

        return agents;
    }

    public void insert(Dosage dosage){
        dosagesDao.insert(dosage);
    }

    public void deleteAll(){
        dosagesDao.delete();
    }
}
