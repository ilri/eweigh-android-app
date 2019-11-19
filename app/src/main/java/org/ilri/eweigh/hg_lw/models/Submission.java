package org.ilri.eweigh.hg_lw.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "submissions")
public class Submission {

    /**
     *
     * Request/response keys
     *
     * */
    public static final String ID = "id";
    public static final String USER_ID = "userid";
    public static final String CATTLE_ID = "cattleid";
    public static final String HG = "hg";
    public static final String LW = "lw";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String CREATED_ON = "createdon";

    /**
     *
     * Model vars
     *
     * */
    @PrimaryKey
    private int id;

    private int userId, cattleId;
    private double hg, lw, lat, lng;
    private String createdOn;

    public Submission(){}

    public Submission(JSONObject obj){
        try {
            this.id = obj.getInt(ID);
            this.userId = obj.getInt(USER_ID);
            this.cattleId = obj.getInt(CATTLE_ID);
            this.hg = obj.getDouble(HG);
            this.lw = obj.getDouble(LW);
            this.lat = obj.getDouble(LAT);
            this.lng = obj.getDouble(LNG);
            this.createdOn = obj.getString(CREATED_ON);

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

    public int getCattleId() {
        return cattleId;
    }

    public void setCattleId(int cattleId) {
        this.cattleId = cattleId;
    }

    public double getHg() {
        return hg;
    }

    public void setHg(double hg) {
        this.hg = hg;
    }

    public double getLw() {
        return lw;
    }

    public void setLw(double lw) {
        this.lw = lw;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
