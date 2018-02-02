package com.scbpfsdgis.femobilebetav20.data.model;

/**
 * Created by William on 1/7/2018.
 */

public class Fields {
    public static final String TAG = Farms.class.getSimpleName();
    public static final String TABLE_BSC = "fldsBasic";
    public static final String TABLE_SUIT = "fldsSuit";
    public static final String TABLE_OTHERS = "fldsOthers";

    //Column Names
    ///Basic
    public static final String COL_FLD_ID = "fld_id";
    public static final String COL_FLD_NAME = "fld_name";
    public static final String COL_FLD_AREA = "fld_area";
    public static final String COL_FLD_VAR = "fld_var";
    public static final String COL_FLD_SOIL = "fld_soilTyp";
    public static final String COL_FLD_FARMID = "fld_farm_id";

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

    private String fldId;
    private String fldName;
    private double fldArea;
    private String fldVar;
    private String fldSoilTyp;
    private String fldFarmId;
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

}