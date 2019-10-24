package org.ilri.eweigh.cattle.models;

import androidx.room.PrimaryKey;

public class ChemicalAgent {

    /**
     *
     * Request/Response keys
     *
     * */
    public static final String ID = "id";
    public static final String CHEMICAL_AGENT = "agent";

    /**
     *
     * Model vars
     *
     * */

    @PrimaryKey
    private int id;

    private String agent;

    public ChemicalAgent(){}

    public ChemicalAgent(int id, String agent){
        this.id = id;
        this.agent = agent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    /**
     *
     * Model functions
     *
     * */

    @Override
    public String toString() {
        return agent;
    }
}
