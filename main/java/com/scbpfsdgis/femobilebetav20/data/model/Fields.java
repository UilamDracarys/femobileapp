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
    public static final String COL_FLD_MAINLIM = "fld_mainlim";
    public static final String COL_FLD_SECLIM = "fld_seclim";
    public static final String COL_FLD_TERTLIM = "fld_tertlim";
    public static final String COL_FLD_RDCOND = "fld_rdcond";

    ///Other Attributes
    public static final String COL_FLD_TRACT = "fld_tractacc";
    public static final String COL_FLD_HARVMETH = "fld_harvmeth";
    public static final String COL_FLD_MECHMETH = "fld_mechmeth";
    public static final String COL_FLD_ROWDIR = "fld_rowdir";
    public static final String COL_FLD_ROWWIDTH = "fld_rowwidth";
    public static final String COL_FLD_CANAL = "fld_canal";
    public static final String COL_FLD_CMT = "fld_cmt";

    private String fldId;
    private String fldName;
    private String fldArea;
    private String fldVar;
    private String fldSoilTyp;
    private String fldFarmId;
    private String fldSuit;
    private String fldMainLim;
    private String fldSecLim;
    private String fldTertLim;
    private String fldRdCond;
    private String fldTractAcc;
    private String fldHarvMeth;
    private String fldMechMeth;
    private String fldRowDir;
    private String fldRowWidth;
    private String fldCanal;
    private String fldCmt;

    public String getFldId() {
        return fldId;
    }

    public void setFldId(String fldId) {
        this.fldId = fldId;
    }

    public String getFldName() {
        return fldName;
    }

    public void setFldName(String fldName) {
        this.fldName = fldName;
    }

    public String getFldArea() {
        return fldArea;
    }

    public void setFldArea(String fldArea) {
        this.fldArea = fldArea;
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

    public String getFldMainLim() {
        return fldMainLim;
    }

    public void setFldMainLim(String fldMainLim) {
        this.fldMainLim = fldMainLim;
    }

    public String getFldSecLim() {
        return fldSecLim;
    }

    public void setFldSecLim(String fldSecLim) {
        this.fldSecLim = fldSecLim;
    }

    public String getFldTertLim() {
        return fldTertLim;
    }

    public void setFldTertLim(String fldTertLim) {
        this.fldTertLim = fldTertLim;
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

    public String getFldCanal() {
        return fldCanal;
    }

    public void setFldCanal(String fldCanal) {
        this.fldCanal = fldCanal;
    }

    public String getFldCmt() {
        return fldCmt;
    }

    public void setFldCmt(String fldCmt) {
        this.fldCmt = fldCmt;
    }
}
