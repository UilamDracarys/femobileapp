package com.scbpfsdgis.fdrmobile.data.model;

/**
 * Created by William on 1/7/2018.
 */

public class Farms {

    public static final String TAG = Farms.class.getSimpleName();
    public static final String TABLE = "farms";
    public static final String TABLE_EXPORTED = "exported_farms";

    //Column Names
    public static final String COL_FARM_ID = "farm_id";
    public static final String COL_FARM_PLTR = "farm_pltr_id";
    public static final String COL_FARM_OVSR = "farm_ovsr_id";
    public static final String COL_FARM_NAME = "farm_name";
    public static final String COL_FARM_CMT = "farm_cmt";
    public static final String COL_FARM_LOC = "farm_locality";
    public static final String COL_FARM_CITY = "farm_city";
    public static final String COL_FARM_SVY = "farm_svy_start";
    public static final String COL_FARM_EXP = "farm_exported";

    private int farmID;
    private String farmName;

    public String getFarmPltrID() {
        return farmPltrID;
    }

    public void setFarmPltrID(String farmPltrID) {
        this.farmPltrID = farmPltrID;
    }

    public String getFarmOvsrID() {
        return farmOvsrID;
    }

    public void setFarmOvsrID(String farmOvsrID) {
        this.farmOvsrID = farmOvsrID;
    }

    private String farmPltrID;
    private String farmOvsrID;
    private String farmCmt;
    private String farmLoc;
    private String farmCity;
    private String farmSvyStart;

    public String getFarmSvy() {
        return farmSvyStart;
    }
    public void setFarmSvy(String farmSvyStart) {
        this.farmSvyStart = farmSvyStart;
    }

    public int getFarmID() {
        return farmID;
    }

    public void setFarmID(int farmID) {
        this.farmID = farmID;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getFarmCmt() {
        return farmCmt;
    }

    public void setFarmCmt(String farmCmt) {
        this.farmCmt = farmCmt;
    }

    public String getFarmLoc() {
        return farmLoc;
    }

    public void setFarmLoc(String farmLoc) {
        this.farmLoc = farmLoc;
    }

    public String getFarmCity() {
        return farmCity;
    }

    public void setFarmCity(String farmCity) {
        this.farmCity = farmCity;
    }
}