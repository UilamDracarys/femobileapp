package com.scbpfsdgis.femobilebetav20.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.scbpfsdgis.femobilebetav20.data.DBHelper;
import com.scbpfsdgis.femobilebetav20.data.DatabaseManager;
import com.scbpfsdgis.femobilebetav20.data.model.Farms;
import com.scbpfsdgis.femobilebetav20.data.model.Fields;

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
                Fields.COL_FLD_MAINLIM + " TEXT, " +
                Fields.COL_FLD_SECLIM + " TEXT, " +
                Fields.COL_FLD_TERTLIM + " TEXT, " +
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
        values.put(Fields.COL_FLD_FARMID, fields.getFldArea());

        db.insert(Fields.TABLE_BSC, null, values);
        db.close();
    }

    public void insertSuit(Fields fields) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Fields.COL_FLD_ID, fields.getFldId());
        values.put(Fields.COL_FLD_SUIT, fields.getFldSuit());
        values.put(Fields.COL_FLD_MAINLIM, fields.getFldMainLim());
        values.put(Fields.COL_FLD_SECLIM, fields.getFldSecLim());
        values.put(Fields.COL_FLD_TERTLIM, fields.getFldTertLim());
        values.put(Fields.COL_FLD_RDCOND, fields.getFldRdCond());

        db.insert(Fields.TABLE_SUIT, null, values);
        db.close();
    }

    public void insertOthers(Fields fields) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Fields.COL_FLD_ID, fields.getFldId());
        values.put(Fields.COL_FLD_HARVMETH, fields.getFldHarvMeth());
        values.put(Fields.COL_FLD_MECHMETH, fields.getFldMechMeth());
        values.put(Fields.COL_FLD_ROWDIR, fields.getFldRowDir());
        values.put(Fields.COL_FLD_ROWWIDTH, fields.getFldRowWidth());
        values.put(Fields.COL_FLD_CANAL, fields.getFldCanal());
        values.put(Fields.COL_FLD_CMT, fields.getFldCmt());

        db.insert(Fields.TABLE_OTHERS, null, values);
        db.close();
    }

    public int getFldId() {
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();
        int fldId;

        String selectQuery = "SELECT MAX(" + Fields.COL_FLD_ID + ") FROM " + Fields.TABLE_BSC;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            fldId = cursor.getInt(cursor.getColumnIndex("FarmID"));
        } else {
            fldId = 0;
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return fldId;
    }
}

