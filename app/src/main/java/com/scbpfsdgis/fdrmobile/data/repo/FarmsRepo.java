package com.scbpfsdgis.fdrmobile.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.scbpfsdgis.fdrmobile.data.DatabaseManager;
import com.scbpfsdgis.fdrmobile.data.model.Farms;
import com.scbpfsdgis.fdrmobile.data.DBHelper;
import com.scbpfsdgis.fdrmobile.data.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by William on 1/7/2018.
 */

public class FarmsRepo {
    private DBHelper dbHelper;
    private SQLiteDatabase db;


    public FarmsRepo() {
        Farms farms = new Farms();
    }

    public static String createTable() {
        return "CREATE TABLE IF NOT EXISTS " + Farms.TABLE + " (" +
                Farms.COL_FARM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Farms.COL_FARM_NAME + " TEXT, " +
                Farms.COL_FARM_PLTR + " TEXT, " +
                Farms.COL_FARM_OVSR + " TEXT, " +
                Farms.COL_FARM_LOC + " TEXT, " +
                Farms.COL_FARM_CITY + " TEXT, " +
                Farms.COL_FARM_SVY + " TEXT, " +
                Farms.COL_FARM_CMT + " TEXT," +
                Farms.COL_FARM_EXP + " TEXT)";
    }


    public void insert(Farms farms) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Farms.COL_FARM_NAME, farms.getFarmName());
        values.put(Farms.COL_FARM_PLTR, farms.getFarmPltrID());
        values.put(Farms.COL_FARM_OVSR, farms.getFarmOvsrID());
        values.put(Farms.COL_FARM_LOC, farms.getFarmLoc());
        values.put(Farms.COL_FARM_CITY, farms.getFarmCity());
        values.put(Farms.COL_FARM_CMT, farms.getFarmCmt());
        values.put(Farms.COL_FARM_SVY, farms.getFarmSvy());

        // Inserting Row
        db.insert(Farms.TABLE, null, values);
        db.close();
    }


    public void delete( ) {
        //TODO: Code to delete farms records;
    }

    public void update(Farms farms) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Farms.COL_FARM_NAME, farms.getFarmName());
        values.put(Farms.COL_FARM_PLTR, farms.getFarmPltrID());
        values.put(Farms.COL_FARM_OVSR, farms.getFarmOvsrID());
        values.put(Farms.COL_FARM_LOC, farms.getFarmLoc());
        values.put(Farms.COL_FARM_CITY, farms.getFarmCity());
        values.put(Farms.COL_FARM_CMT, farms.getFarmCmt());
        values.put(Farms.COL_FARM_SVY, farms.getFarmSvy());

        db.update(Farms.TABLE, values, Farms.COL_FARM_ID + "= ? ", new String[] { String.valueOf(farms.getFarmID()) });
        db.close(); // Closing database connection
    }

    public void resetExportDates() {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        String update = "UPDATE " + Farms.TABLE + " SET " + Farms.COL_FARM_EXP + " = NULL ";
        db.execSQL(update);
        db.close(); // Closing database connection
    }

    //Get List of Farms
    public ArrayList<HashMap<String, String>> getFarmsList() {

        //Open connection to read only
        //db = DatabaseManager.getInstance().openDatabase();
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT farm_id As FarmID, farm_name As FarmName, \n" +
                "(SELECT person_name FROM person WHERE person_id = farms.farm_pltr_id) As PlanterName,\n" +
                "(SELECT SUM(fld_area) FROM fldsBasic WHERE fld_farm_id = farms.farm_id) As Area,\n" +
                "(SELECT COUNT(fld_id) FROM fldsBasic WHERE fld_farm_id = farms.farm_id) As FldCount\n" +
                "FROM farms WHERE farm_exported IS NULL ORDER BY FarmName COLLATE NOCASE";

        System.out.println("FarmsListQuery: " +selectQuery);

        ArrayList<HashMap<String, String>> farmsList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> farms = new HashMap<>();
                farms.put("id", cursor.getString(cursor.getColumnIndex("FarmID")));
                farms.put("farmName", cursor.getString(cursor.getColumnIndex("FarmName")));
                if (cursor.getInt(cursor.getColumnIndex("FldCount")) == 0) {
                    farms.put("pltrAreaCount", cursor.getString(cursor.getColumnIndex("PlanterName")) + " | No fields added.");
                } else {
                    farms.put("pltrAreaCount", cursor.getString(cursor.getColumnIndex("PlanterName")) + " | " + cursor.getString(cursor.getColumnIndex("Area")) + " has., " + cursor.getString(cursor.getColumnIndex("FldCount")) + " fld/s");
                }
                farmsList.add(farms);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return farmsList;
    }

   public Farms getFarmByID(int Id){
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT * FROM " + Farms.TABLE + " WHERE " + Farms.COL_FARM_ID + " =?";

        Farms farms = new Farms();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                farms.setFarmID(cursor.getInt(cursor.getColumnIndex(Farms.COL_FARM_ID)));
                farms.setFarmName(cursor.getString(cursor.getColumnIndex(Farms.COL_FARM_NAME)));
                farms.setFarmPltrID(cursor.getString(cursor.getColumnIndex(Farms.COL_FARM_PLTR)));
                farms.setFarmOvsrID(cursor.getString(cursor.getColumnIndex(Farms.COL_FARM_OVSR)));
                farms.setFarmLoc(cursor.getString(cursor.getColumnIndex(Farms.COL_FARM_LOC)));
                farms.setFarmCity(cursor.getString(cursor.getColumnIndex(Farms.COL_FARM_CITY)));
                farms.setFarmSvy(cursor.getString(cursor.getColumnIndex(Farms.COL_FARM_SVY)));
                farms.setFarmCmt(cursor.getString(cursor.getColumnIndex(Farms.COL_FARM_CMT)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return farms;
    }

    public boolean isFarmExisting(String farmName) {
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();
        farmName = farmName.replaceAll("'", "''");
        String selectQuery =  "SELECT farm_name FROM " + Farms.TABLE + " WHERE " + Farms.COL_FARM_NAME + " = '" + farmName + "'";

        System.out.println("Select farmname query: " + selectQuery);
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


}