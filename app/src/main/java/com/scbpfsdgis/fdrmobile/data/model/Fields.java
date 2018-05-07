package com.scbpfsdgis.fdrmobile.data.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by William on 1/7/2018.
 */

public class Fields {
    public static final String TAG = Farms.class.getSimpleName();
    public static final String TABLE_BSC = "fldsBasic";
    public static final String TABLE_SUIT = "fldsSuit";
    public static final String TABLE_OTHERS = "fldsOthers";
    public static final String TABLE_ATTVALS = "fldAtts";

    //Column Names
    ///Basic
    public static final String COL_FLD_ID = "fld_id";
    public static final String COL_FLD_NAME = "fld_name";
    public static final String COL_FLD_AREA = "fld_area";
    public static final String COL_FLD_VAR = "fld_var";
    public static final String COL_FLD_SOIL = "fld_soilTyp";
    public static final String COL_FLD_FARMID = "fld_farm_id";
    public static final String COL_FLD_SURVEYOR = "fld_svyor";
    public static final String COL_FLD_DP = "fld_dp";
    public static final String COL_FLD_CROPCYCLE = "fld_cropCycle";
    public static final String COL_FLD_HD = "fld_hd";

    ///Suitability
    public static final String COL_FLD_SUIT = "fld_suit";
    public static final String COL_FLD_LIMITS = "fld_limits";
    public static final String COL_FLD_RDCOND = "fld_rdcond";

    ///Other Attributes
    public static final String COL_FLD_TRACT = "fld_tractacc";
    public static final String COL_FLD_HARVMETH = "fld_harvmeth";
    public static final String COL_FLD_MECHMETH = "fld_mechmeth";
    public static final String COL_FLD_ROWDIR = "fld_rowdir";
    public static final String COL_FLD_ROWWIDTH = "fld_rowwidth";
    public static final String COL_FLD_CANAL = "fld_canals";
    public static final String COL_FLD_CROPCLS = "fld_cropcls";
    public static final String COL_FLD_CMT = "fld_cmt";

    ///Field Attribute Table
    public static final String COL_FLD_OBJ_ID = "fld_att_objid";
    public static final String COL_FLD_ATT_ID = "fld_att_id";
    public static final String COL_FLD_ATT_CODE = "fld_att_code";
    public static final String COL_FLD_ATT_DESC = "fld_att_desc";

    //Attribute Values
    public static final String[][] fldAtt = {{"fld_soiltyp","Sandy Loam","SL"},
        {"fld_soiltyp","Sandy","S"},
        {"fld_soiltyp","Loamy Sand","LS"},
        {"fld_soiltyp","Loam","L"},
        {"fld_soiltyp","Clay Loam","CL"},
        {"fld_soiltyp","Clay","C"},
        {"fld_soiltyp","Heavy Clay","HC"},
        {"fld_suit","Suitable","S"},
        {"fld_suit","Partially Suitable","P"},
        {"fld_suit","Conditionally Suitable","CS"},
        {"fld_suit","Not Suitable","NS"},
        {"fld_suit","Not in Use","NU"},
        {"fld_suit","TBD","TBD"},
        {"fld_limits","Area less than 0.80ha","HAR"},
        {"fld_limits","Hindrance - Boulders","HBO"},
        {"fld_limits","Hindrance - Canal","HCA"},
        {"fld_limits","Hindrance - Rocks","HR"},
        {"fld_limits","Hindrance - Row width less than 1.2m","HRW"},
        {"fld_limits","Hindrance - Soil Type","HSST"},
        {"fld_limits","Hindrance - Stones","HST"},
        {"fld_limits","Hindrance - Too many trees","HTR"},
        {"fld_limits","Steepness - Slope","STSL"},
        {"fld_limits","Steepness - Hilly","STH"},
        {"fld_limits","Wagon access - Entry rd. inaccessible","WAI"},
        {"fld_limits","Wagon access - No entry rd.","WAN"},
        {"fld_limits","Wagon access - Entry via river","WARI"},
        {"fld_limits","Wagon access - Maneuver difficult","WAMD"},
        {"fld_mechmeth","Yes","0"},
        {"fld_mechmeth","No","1"},
        {"fld_tractacc","Yes","0"},
        {"fld_tractacc","No","1"},
        {"fld_rowdir","Along entry rd.","A"},
        {"fld_rowdir","Perpendicular to entry rd.","P"},
        {"fld_cropcls","To be Determined","TBD"},
        {"fld_rdcond","Asphalt-OK-<3m","AO<3"},
        {"fld_rdcond","Asphalt-Good-<3m","AG<3"},
        {"fld_rdcond","Asphalt-Poor-<3m","AP<3"},
        {"fld_rdcond","Asphalt-OK->=3m","AO>=3"},
        {"fld_rdcond","Asphalt-Good->=3m","AG>=3"},//
        {"fld_rdcond","Asphalt-Poor->=3m","AP>=3"},
        {"fld_rdcond","Graded-OK-<3m","GO<3"},
        {"fld_rdcond","Graded-Good-<3m","GG<3"},//
        {"fld_rdcond","Graded-Poor-<3m","GP<3"},
        {"fld_rdcond","Graded-OK->=3m","GO>=3"},
        {"fld_rdcond","Graded-Good->=3m","GG>=3"},//
        {"fld_rdcond","Graded-Poor->=3m","GP>=3"},
        {"fld_rdcond","NotGraded-OK-<3m","NGO<3"},
        {"fld_rdcond","NotGraded-Good-<3m","NGG<3"},//
        {"fld_rdcond","NotGraded-Poor-<3m","NGP<3"},
        {"fld_rdcond","NotGraded-OK->=3m","NGO>=3"},
        {"fld_rdcond","NotGraded-Good->=3m","NGG>=3"},//
        {"fld_rdcond","NotGraded-Poor->=3m","NGP>=3"},
        {"fld_cropcls","New Plant","NP"},
        {"fld_cropcls","1st Rtn","R1"},
        {"fld_cropcls","2nd Rtn","R2"},
        {"fld_cropcls","3rd Rtn","R3"},
        {"fld_cropcls","4th Rtn","R4"},
        {"fld_cropcls","5th Rtn","R5"},
        {"fld_cropcls","6th Rtn","R6"},
        {"fld_cropcls","7th Rtn","R7"},
        {"fld_cropcls","FAL","F"},
        {"fld_harvmeth","By Ton","T"},
        {"fld_harvmeth","Linear","L"},
        {"fld_canals","Front","F"},
        {"fld_canals","Left","L"},
        {"fld_canals","Back","B"},
        {"fld_canals","Right","R"}};

    private String fldId;
    private String fldName;
    private double fldArea;
    private String fldVar;
    private String fldSoilTyp;
    private String fldFarmId;
    private String fldSurveyor;
    private String fldSuit;
    private String fldLimits;
    private String fldRdCond;
    private String fldTractAcc;
    private String fldHarvMeth;
    private String fldMechMeth;
    private String fldRowDir;
    private String fldRowWidth;
    private String fldCanals;
    private String fldCropCls;
    private String fldCmt;
    private String fldCropCycle;
    private String fldDatePlanted;
    private String fldHarvestDate;

    public String getFldCropCycle() {
        return fldCropCycle;
    }

    public void setFldCropCycle(String fldCropCycle) {
        this.fldCropCycle = fldCropCycle;
    }

    public String getFldDatePlanted() {
        return fldDatePlanted;
    }

    public void setFldDatePlanted(String fldDatePlanted) {
        this.fldDatePlanted = fldDatePlanted;
    }

    public String getFldHarvestDate() {
        return fldHarvestDate;
    }

    public void setFldHarvestDate(String fldHarvestDate) {
        this.fldHarvestDate = fldHarvestDate;
    }

    public String getFldId() {
        return fldId;
    }

    public String getFldCropCls() {
        return fldCropCls;
    }

    public void setFldCropCls(String fldCropCls) {
        this.fldCropCls = fldCropCls;
    }

    public void setFldId(String fldId) {
        this.fldId = fldId;
    }

    public String getFldName() {
        return fldName;
    }

    public double getFldArea() {
        return fldArea;
    }

    public void setFldArea(double fldArea) {
        this.fldArea = fldArea;
    }

    public void setFldName(String fldName) {
        this.fldName = fldName;
    }

    public String getFldVar() {
        return fldVar;
    }

    public void setFldVar(String fldVar) {
        this.fldVar = fldVar;
    }

    public String getFldSoilTyp() {
        return fldSoilTyp;
    }

    public void setFldSoilTyp(String fldSoilTyp) {
        this.fldSoilTyp = fldSoilTyp;
    }

    public String getFldFarmId() {
        return fldFarmId;
    }

    public void setFldFarmId(String fldFarmId) {
        this.fldFarmId = fldFarmId;
    }

    public String getFldSuit() {
        return fldSuit;
    }

    public void setFldSuit(String fldSuit) {
        this.fldSuit = fldSuit;
    }

    public String getFldLimits() {
        return fldLimits;
    }

    public void setFldLimits(String fldLimits) {
        this.fldLimits = fldLimits;
    }

    public String getFldRdCond() {
        return fldRdCond;
    }

    public void setFldRdCond(String fldRdCond) {
        this.fldRdCond = fldRdCond;
    }

    public String getFldTractAcc() {
        return fldTractAcc;
    }

    public void setFldTractAcc(String fldTractAcc) {
        this.fldTractAcc = fldTractAcc;
    }

    public String getFldHarvMeth() {
        return fldHarvMeth;
    }

    public void setFldHarvMeth(String fldHarvMeth) {
        this.fldHarvMeth = fldHarvMeth;
    }

    public String getFldMechMeth() {
        return fldMechMeth;
    }

    public void setFldMechMeth(String fldMechMeth) {
        this.fldMechMeth = fldMechMeth;
    }

    public String getFldRowDir() {
        return fldRowDir;
    }

    public void setFldRowDir(String fldRowDir) {
        this.fldRowDir = fldRowDir;
    }

    public String getFldRowWidth() {
        return fldRowWidth;
    }

    public void setFldRowWidth(String fldRowWidth) {
        this.fldRowWidth = fldRowWidth;
    }

    public String getFldCanals() {
        return fldCanals;
    }

    public void setFldCanals(String fldCanals) {
        this.fldCanals = fldCanals;
    }

    public String getFldCmt() {
        return fldCmt;
    }

    public void setFldCmt(String fldCmt) {
        this.fldCmt = fldCmt;
    }

    public int getIdxByCode(String[] array, String att) {
        int idx = 0;
        for (int i=0; i <array.length; i++) {
            if(array[i].contains("(" + att + ")")) {
                idx = i;
                break;
            }
        }
        return idx;
    }

    public int[] getIndexArray(String[] selArray, String[] array) {
        int[] intIdxArray = new int[selArray.length];

        for (int i=0; i<selArray.length;i++) {
            intIdxArray[i] = getIdxByCode(array, selArray[i].trim());
        }
        return intIdxArray;
    }

    public String getFldSurveyor() {
        return fldSurveyor;
    }

    public void setFldSurveyor(String fldSurveyor) {
        this.fldSurveyor = fldSurveyor;
    }


}
