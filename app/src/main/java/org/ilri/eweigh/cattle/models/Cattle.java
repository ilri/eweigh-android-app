package org.ilri.eweigh.cattle.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity(tableName = "cattle")
public class Cattle implements Serializable {

    /**
     *
     * Response/request keys
     *
     * */
    public static final String CATTLE = "cattle";
    public static final String ID = "id";
    public static final String USER_ID = "userid";
    public static final String TAG = "tag";
    public static final String BREED = "breed";
    public static final String LIVE_WEIGHT = "lw";
    public static final String CREATED_ON = "createdon";

    /**
     *
     * Model vars
     *
     * */

    @PrimaryKey
    private int id;

    private int userId;
    private double liveWeight;
    private String tag, breed, createdOn;

    public Cattle(){}

    public Cattle(JSONObject obj){

        try {
            this.id = obj.getInt(ID);
            this.userId = obj.getInt(USER_ID);
            this.tag = obj.optString(TAG, "-");
            this.breed = obj.optString(BREED, "-");
            this.liveWeight = obj.optInt(LIVE_WEIGHT, 0);
            this.createdOn = obj.optString(CREATED_ON, "-");

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getLiveWeight() {
        return liveWeight;
    }

    public void setLiveWeight(double liveWeight) {
        this.liveWeight = liveWeight;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    /**
     *
     * Model functions
     *
     * */

    @Override
    public String toString() {
        return tag;
    }

    public static boolean hasItems(JSONArray array){
        return array.length() > 0;
    }
}
