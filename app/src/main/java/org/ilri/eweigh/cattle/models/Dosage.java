package org.ilri.eweigh.cattle.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity(tableName = "dosages")
public class Dosage implements Serializable {

    /**
     *
     * Request/Response keys
     *
     * */
    public static final String ID = "id";
    public static final String DOSAGE= "dosage";
    public static final String COUNTY = "county";
    public static final String DISEASE_ID = "did";
    public static final String DISEASE = "disease";
    public static final String CHEMICAL_AGENT_ID = "aid";
    public static final String CHEMICAL_AGENT = "agent";
    public static final String APPLICATION_MODE = "mode";

    /**
     *
     * Model vars
     *
     * */

    @PrimaryKey
    private int id;

    private int diseaseId, chemicalAgentId;
    private String dosage, county, disease, chemicalAgent, applicationMode;

    public Dosage(){}

    public Dosage(JSONObject obj){

        try {
            this.id = obj.getInt(ID);
            this.dosage = obj.getString(DOSAGE);
            this.county = obj.optString(COUNTY, "-");
            this.diseaseId = obj.getInt(DISEASE_ID);
            this.disease = obj.getString(DISEASE);
            this.chemicalAgentId = obj.getInt(CHEMICAL_AGENT_ID);
            this.chemicalAgent = obj.getString(CHEMICAL_AGENT);
            this.applicationMode = obj.getString(APPLICATION_MODE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(int diseaseId) {
        this.diseaseId = diseaseId;
    }

    public int getChemicalAgentId() {
        return chemicalAgentId;
    }

    public void setChemicalAgentId(int chemicalAgentId) {
        this.chemicalAgentId = chemicalAgentId;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getChemicalAgent() {
        return chemicalAgent;
    }

    public void setChemicalAgent(String chemicalAgent) {
        this.chemicalAgent = chemicalAgent;
    }

    public String getApplicationMode() {
        return applicationMode;
    }

    public void setApplicationMode(String applicationMode) {
        this.applicationMode = applicationMode;
    }

    /**
     *
     * Model functions
     *
     * */

    @Override
    public String toString() {
        return dosage;
    }

    public static boolean hasItems(JSONArray array){
        return array.length() > 0;
    }
}
