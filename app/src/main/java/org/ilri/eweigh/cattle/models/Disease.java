package org.ilri.eweigh.cattle.models;

import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Disease {

    /**
     *
     * Request/Response keys
     *
     * */
    public static final String ID = "id";
    public static final String DISEASE = "disease";

    /**
     *
     * Model vars
     *
     * */

    @PrimaryKey
    private int id;

    private String disease;

    public Disease(){}

    public Disease(int id, String disease){
        this.id = id;
        this.disease = disease;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    /**
     *
     * Model functions
     *
     * */

    @Override
    public String toString() {
        return disease;
    }
}
