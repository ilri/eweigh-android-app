package org.ilri.eweigh.feeds;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity(tableName = "feeds")
public class Feed implements Serializable {

    /**
     *
     * Model constants
     *
     * */
    public static final String FEED_TYPE_FORAGE = "forage";
    public static final String FEED_TYPE_CONCENTRATE = "concentrate";

    public static final String FEED_FOR_MILK = "milk";
    public static final String FEED_FOR_WEIGHT = "weight";

    public static final String FEED_STYLE_STALL = "stall";
    public static final String FEED_STYLE_GRAZE_LOCAL = "graze_local";
    public static final String FEED_STYLE_GRAZE_EXT = "graze_ext";

    /**
     *
     * Request/Response keys
     *
     * */
    public static final String ID = "id";
    public static final String FEED_NAME = "feed";
    public static final String DESCRIPTION = "description";
    public static final String FEED_TYPE = "feedtype";
    public static final String DRY_MATTER = "drymatter";
    public static final String ENERGY = "energy";
    public static final String PROTEIN = "protein";
    public static final String NDF = "ndf";

    public static final String LIVE_WEIGHT = "lw";
    public static final String FEED_FOR = "feedfor";
    public static final String FEED_STYLE = "feedstyle";
    public static final String MILK_PRODUCTION = "milkprod";
    public static final String TARGET_WEIGHT = "weight";
    public static final String TARGET_DATE = "date";
    public static final String FORAGE = "forage";
    public static final String CONCENTRATE = "concentrate";

    /**
     *
     * Model vars
     *
     * */
    @PrimaryKey
    private int id;

    private String feedName, feedType, description;
    private double dryMatter, energy, protein, ndf;

    public Feed(){}

    public Feed(int id, String feedName){
        this.id = id;
        this.feedName = feedName;
    }

    public Feed(JSONObject obj){
        try {
            this.id = obj.getInt(ID);
            this.feedName = obj.getString(FEED_NAME);
            this.description = obj.getString(DESCRIPTION);
            this.feedType = obj.getString(FEED_TYPE);
            this.dryMatter = obj.getDouble(DRY_MATTER);
            this.energy = obj.getDouble(ENERGY);
            this.protein = obj.getDouble(PROTEIN);
            this.ndf = obj.getDouble(NDF);

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

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDryMatter() {
        return dryMatter;
    }

    public void setDryMatter(double dryMatter) {
        this.dryMatter = dryMatter;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getNdf() {
        return ndf;
    }

    public void setNdf(double ndf) {
        this.ndf = ndf;
    }

    /**
     *
     * Model functions
     *
     * */
    @Override
    public String toString() {
        return feedName;
    }

    public static boolean hasItems(JSONArray array){
        return array.length() > 0;
    }
}
