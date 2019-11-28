package org.ilri.eweigh.cattle.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity(tableName = "breeds")
public class Breed implements Serializable {

    /**
     *
     * Request/Response keys
     *
     * */
    public static final String ID = "id";
    public static final String BREED = "breed";
    public static final String MATURE_WEIGHT = "matureweight";

    /**
     *
     * Model vars
     *
     * */

    @PrimaryKey
    private int id;

    private int matureWeight;
    private String breed;

    public Breed(){}

    public Breed(int id, String breed){
        this.id = id;
        this.breed = breed;
    }

    public Breed(int id, String breed, int matureWeight){
        this.id = id;
        this.breed = breed;
        this.matureWeight = matureWeight;
    }

    public Breed(JSONObject obj){

        try {
            this.id = obj.getInt(ID);
            this.breed = obj.getString(BREED);
            this.matureWeight = obj.getInt(MATURE_WEIGHT);

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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getMatureWeight() {
        return matureWeight;
    }

    public void setMatureWeight(int matureWeight) {
        this.matureWeight = matureWeight;
    }

    /**
     *
     * Model functions
     *
     * */

    @Override
    public String toString() {
        return breed;
    }

    public static boolean hasItems(JSONArray array){
        return array.length() > 0;
    }

    public double getReferenceWeight(){
        return matureWeight * 0.65;
    }
}
