package org.ilri.eweigh.hg_lw.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Cattle {

    /**
     *
     * Response/request keys
     *
     * */
    public static final String ID = "id";
    public static final String USER_ID = "userid";
    public static final String TAG = "tag";
    public static final String BREED = "breed";
    public static final String INITIAL_HG = "initialhg";
    public static final String CREATED_ON = "createdon";

    /**
     *
     * Model vars
     *
     * */
    private int id, userId, initialHg;
    private String tag, breed, createdOn;

    public Cattle(JSONObject obj){

        try {
            this.id = obj.getInt(ID);
            this.userId = obj.getInt(USER_ID);
            this.tag = obj.optString(TAG, "-");
            this.breed = obj.optString(BREED, "-");
            this.initialHg = obj.optInt(INITIAL_HG, 0);
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

    public int getInitialHg() {
        return initialHg;
    }

    public void setInitialHg(int initialHg) {
        this.initialHg = initialHg;
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
}
