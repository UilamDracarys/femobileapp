package com.scbpfsdgis.fdrmobile.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.scbpfsdgis.fdrmobile.data.DBHelper;
import com.scbpfsdgis.fdrmobile.data.DatabaseManager;
import com.scbpfsdgis.fdrmobile.data.model.Fields;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by William on 1/7/2018.
 */

public class FieldsRepo {
    private Fields fields;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public FieldsRepo() {
        fields = new Fields();
    }

    public static String createTableBsc() {
        return "CREATE TABLE " + Fields.TABLE_BSC + " (" +
                Fields.COL_FLD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Fields.COL_FLD_NAME + " TEXT, " +
                Fields.COL_FLD_AREA + " DECIMAL(10,3), " +
                Fields.COL_FLD_VAR + " TEXT, " +
                Fields.COL_FLD_SOIL + " TEXT, " +
                Fields.COL_FLD_FARMID + " TEXT) ";
    }

    public static String createTableSuit() {
        return "CREATE TABLE " + Fields.TABLE_SUIT + " (" +
                Fields.COL_FLD_ID + " INTEGER PRIMARY KEY, " +
                Fields.COL_FLD_SUIT + " TEXT, " +
                Fields.COL_FLD_LIMITS + " TEXT, " +
                Fields.COL_FLD_RDCOND + " TEXT) ";
    }

    public static String createTableOthers() {
        return "CREATE TABLE " + Fields.TABLE_OTHERS + " (" +
                Fields.COL_FLD_ID + " INTEGER PRIMARY KEY, " +
                Fields.COL_FLD_TRACT + " TEXT, " +
                Fields.COL_FLD_HARVMETH + " TEXT, " +
                Fields.COL_FLD_MECHMETH + " TEXT, " +
                Fields.COL_FLD_ROWDIR + " TEXT, " +
                Fields.COL_FLD_ROWWIDTH + " TEXT, " +
                Fields.COL_FLD_CANAL + " TEXT, " +
                Fields.COL_FLD_CROPCLS + " TEXT, " +
                Fields.COL_FLD_CMT + " TEXT)";
    }

    public void insertBsc(Fields fields) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Fields.COL_FLD_NAME, fields.getFldName());
        values.put(Fields.COL_FLD_AREA, fields.getFldArea());
        values.put(Fields.COL_FLD_VAR, fields.getFldVar());
        values.put(Fields.COL_FLD_SOIL, fields.getFldSoilTyp());
        values.put(Fields.COL_FLD_FARMID, fields.getFldFarmId());

        db.insert(Fields.TABLE_BSC, null, values);
        db.close();
    }

    public void updateBsc(Fields fields) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Fields.COL_FLD_NAME, fields.getFldName());
        values.put(Fields.COL_FLD_AREA, fields.getFldArea());
        values.put(Fields.COL_FLD_VAR, fields.getFldVar());
        values.put(Fields.COL_FLD_SOIL, fields.getFldSoilTyp());
        values.put(Fields.COL_FLD_FARMID, fields.getFldFarmId());

        System.out.println("Updated Record of Field ID: " + fields.getFldId());
        db.update(Fields.TABLE_BSC, values, Fields.COL_FLD_ID + "= ? ", new String[] { String.valueOf(fields.getFldId()) });
        db.close(); // Closing database connection
    }

    public void insertSuit(Fields fields) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Fields.COL_FLD_ID, fields.getFldId());
        values.put(Fields.COL_FLD_SUIT, fields.getFldSuit());
        values.put(Fields.COL_FLD_LIMITS, fields.getFldLimits());
        values.put(Fields.COL_FLD_RDCOND, fields.getFldRdCond());

        db.insert(Fields.TABLE_SUIT, null, values);
        db.close();
    }

    public void updateSuit(Fields fields) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Fields.COL_FLD_SUIT, fields.getFldSuit());
        values.put(Fields.COL_FLD_LIMITS, fields.getFldLimits());
        values.put(Fields.COL_FLD_RDCOND, fields.getFldRdCond());

        db.update(Fields.TABLE_SUIT, values, Fields.COL_FLD_ID + "= ? ", new String[] { String.valueOf(fields.getFldId()) });
        db.close(); // Closing database connection
    }

    public void insertOthers(Fields fields) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Fields.COL_FLD_ID, fields.getFldId());
        values.put(Fields.COL_FLD_TRACT, fields.getFldTractAcc());
        values.put(Fields.COL_FLD_HARVMETH, fields.getFldHarvMeth());
        values.put(Fields.COL_FLD_MECHMETH, fields.getFldMechMeth());
        values.put(Fields.COL_FLD_ROWDIR, fields.getFldRowDir());
        values.put(Fields.COL_FLD_ROWWIDTH, fields.getFldRowWidth());
        values.put(Fields.COL_FLD_CANAL, fields.getFldCanals());
        values.put(Fields.COL_FLD_CROPCLS, fields.getFldCropCls());
        values.put(Fields.COL_FLD_CMT, fields.getFldCmt());

        db.insert(Fields.TABLE_OTHERS, null, values);
        db.close();
    }

    public void updateOthers(Fields fields) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Fields.COL_FLD_TRACT, fields.getFldTractAcc());
        values.put(Fields.COL_FLD_HARVMETH, fields.getFldHarvMeth());
        values.put(Fields.COL_FLD_MECHMETH, fields.getFldMechMeth());
        values.put(Fields.COL_FLD_ROWDIR, fields.getFldRowDir());
        values.put(Fields.COL_FLD_ROWWIDTH, fields.getFldRowWidth());
        values.put(Fields.COL_FLD_CANAL, fields.getFldCanals());
        values.put(Fields.COL_FLD_CROPCLS, fields.getFldCropCls());
        values.put(Fields.COL_FLD_CMT, fields.getFldCmt());

        db.update(Fields.TABLE_OTHERS, values, Fields.COL_FLD_ID + "= ? ", new String[] { String.valueOf(fields.getFldId()) });
        db.close(); // Closing database connection
    }

    //Get List of Farms
    public ArrayList<HashMap<String, String>> getFieldsList(int farmID) {
        //Open connection to read only
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT b.fld_id as FieldID, b.fld_name as FieldName, b.fld_area as Area, " +
                "CASE s.fld_suit WHEN 'S' THEN 'Suitable' " +
                "WHEN 'PS' THEN 'Part. Suitable' " +
                "WHEN 'CS' THEN 'Cond. Suitable' " +
                "WHEN 'NS' THEN 'Not Suitable' " +
                "WHEN 'NU' THEN 'Not in Use' " +
                "WHEN 'TBD' THEN 'TBD' END as Suitability, b.fld_farm_id as FarmID" +
                " FROM " + Fields.TABLE_BSC + " b JOIN " + Fields.TABLE_SUIT + " s " +
                " ON b.fld_id = s.fld_id WHERE b.fld_farm_id = " + farmID +
                " ORDER BY FieldID COLLATE NOCASE";

        ArrayList<HashMap<String, String>> fieldsList = new ArrayList<HashMap<String, String>>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> fields = new HashMap<String, String>();
                fields.put("id", cursor.getString(cursor.getColumnIndex("FieldID")));
                fields.put("fieldName", cursor.getString(cursor.getColumnIndex("FieldName")));
                fields.put("suitArea", cursor.getString(cursor.getColumnIndex("Suitability")) + " - " + cursor.getString(cursor.getColumnIndex("Area")));
                fieldsList.add(fields);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return fieldsList;
    }

    public int getFldId() {
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();
        int fldId;

        String selectQuery = "SELECT MAX(" + Fields.COL_FLD_ID + ") AS FieldID FROM " + Fields.TABLE_BSC;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            fldId = cursor.getInt(cursor.getColumnIndex("FieldID"));
        } else {
            fldId = 0;
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return fldId;
    }

    public boolean isFieldExisting(String fieldName, int farmID) {
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();
        fieldName = fieldName.replaceAll("'", "''");
        String selectQuery =  "SELECT fld_name FROM " + Fields.TABLE_BSC + " WHERE " + Fields.COL_FLD_NAME + " = '" + fieldName + "' AND fld_farm_id = " + farmID;

        Cursor cursor = db.rawQuery(selectQuery, null );
        if (cursor.moveToFirst()) {
            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
            return true;
        } else {
            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
            return false;
        }
    }

    public Fields getFieldByID(int Id){
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT b.fld_id as FieldID, b.fld_farm_id as FarmID, b.fld_name as FieldName, s.fld_suit as Suitability, " +
                " b.fld_area as Area, s.fld_limits as Limits, o.fld_canals as Canals, s.fld_rdcond as RoadCond, " +
                " o.fld_mechmeth as MechMeth, o.fld_tractacc as TractorAcc, o.fld_rowwidth as RowWidth, " +
                " o.fld_rowdir as RowDir, b.fld_soilTyp as SoilType, b.fld_var as Variety, " +
                " o.fld_harvmeth as HarvMeth, o.fld_cropcls as CropClass, o.fld_cmt as Comment " +
                " FROM fldsBasic b JOIN fldsSuit s JOIN fldsOthers o ON (b.fld_id = o.fld_id) AND (b.fld_id = s.fld_id) " +
                " WHERE b.fld_id =?";


        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        Fields fields = new Fields();
        if (cursor.moveToFirst()) {
            do {
                fields.setFldId(cursor.getString(cursor.getColumnIndex("FieldID")));
                fields.setFldFarmId(cursor.getString(cursor.getColumnIndex("FarmID")));
                fields.setFldName(cursor.getString(cursor.getColumnIndex("FieldName")));
                fields.setFldSuit(cursor.getString(cursor.getColumnIndex("Suitability")));
                fields.setFldArea(cursor.getDouble(cursor.getColumnIndex("Area")));
                fields.setFldLimits(cursor.getString(cursor.getColumnIndex("Limits")));
                fields.setFldCanals(cursor.getString(cursor.getColumnIndex("Canals")));
                fields.setFldRdCond(cursor.getString(cursor.getColumnIndex("RoadCond")));
                fields.setFldMechMeth(cursor.getString(cursor.getColumnIndex("MechMeth")));
                fields.setFldTractAcc(cursor.getString(cursor.getColumnIndex("TractorAcc")));
                fields.setFldRowWidth(cursor.getString(cursor.getColumnIndex("RowWidth")));
                fields.setFldRowDir(cursor.getString(cursor.getColumnIndex("RowDir")));
                fields.setFldSoilTyp(cursor.getString(cursor.getColumnIndex("SoilType")));
                fields.setFldVar(cursor.getString(cursor.getColumnIndex("Variety")));
                fields.setFldHarvMeth(cursor.getString(cursor.getColumnIndex("HarvMeth")));
                fields.setFldCropCls(cursor.getString(cursor.getColumnIndex("CropClass")));
                fields.setFldCmt(cursor.getString(cursor.getColumnIndex("Comment")));

            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return fields;
    }
}

