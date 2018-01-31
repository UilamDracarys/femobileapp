package com.scbpfsdgis.femobilebetav20.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.scbpfsdgis.femobilebetav20.data.DBHelper;
import com.scbpfsdgis.femobilebetav20.data.DatabaseManager;
import com.scbpfsdgis.femobilebetav20.data.model.Farms;
import com.scbpfsdgis.femobilebetav20.data.model.Farms;
import com.scbpfsdgis.femobilebetav20.data.model.Person;

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
        return "CREATE TABLE " + Farms.TABLE + " (" +
                Farms.COL_FARM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Farms.COL_FARM_NAME + " TEXT, " +
                Farms.COL_FARM_PLTR + " TEXT, " +
                Farms.COL_FARM_OVSR + " TEXT, " +
                Farms.COL_FARM_LOC + " TEXT, " +
                Farms.COL_FARM_CITY + " TEXT, " +
                Farms.COL_FARM_CMT + " TEXT)";
    }

    public void insert(Farms farms) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        //db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();

        values.put(Farms.COL_FARM_NAME, farms.getFarmName());
        values.put(Farms.COL_FARM_PLTR, farms.getFarmPltrID());
        values.put(Farms.COL_FARM_OVSR, farms.getFarmOvsrID());
        values.put(Farms.COL_FARM_LOC, farms.getFarmLoc());
        values.put(Farms.COL_FARM_CITY, farms.getFarmCity());
        values.put(Farms.COL_FARM_CMT, farms.getFarmCmt());

        // Inserting Row
        db.insert(Farms.TABLE, null, values);
        db.close();
    }

    public void delete( ) {
        /*SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        //TODO: Code to delete farms records;
        db.delete(Farms.TABLE, null,null);
        DatabaseManager.getInstance().closeDatabase();*/
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

        db.update(Farms.TABLE, values, Farms.COL_FARM_ID + "= ? ", new String[] { String.valueOf(farms.getFarmID()) });
        db.close(); // Closing database connection
    }

    //Get List of Farms
    public ArrayList<HashMap<String, String>> getFarmsList() {

        //Open connection to read only
        //db = DatabaseManager.getInstance().openDatabase();
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT " +
                Farms.TABLE + "." + Farms.COL_FARM_ID + " As FarmID, " +
                Farms.TABLE + "." + Farms.COL_FARM_NAME + " As FarmName, " +
                "(SELECT " + Person.COL_PRSN_NAME +
                " FROM " + Person.TABLE +
                " WHERE " + Person.COL_PRSN_ID + " = " + Farms.TABLE + "." + Farms.COL_FARM_PLTR + ") As PlanterName, " +
                "(SELECT " + Person.COL_PRSN_NAME +
                " FROM " + Person.TABLE +
                " WHERE " + Person.COL_PRSN_ID + " = " + Farms.TABLE + "." + Farms.COL_FARM_OVSR + ") As OverseerName, " +
                Farms.TABLE + "." + Farms.COL_FARM_LOC + " As Locality, " +
                Farms.TABLE + "." + Farms.COL_FARM_CITY+ " As City, " +
                Farms.TABLE + "." + Farms.COL_FARM_CMT + " As Comment" +
                " FROM " + Farms.TABLE +
                " LEFT JOIN " + Person.TABLE +
                " ON (" + Person.TABLE + "." + Person.COL_PRSN_ID + " = " + Farms.TABLE + "." + Farms.COL_FARM_PLTR +
                " AND " + Person.TABLE + "." + Person.COL_PRSN_ID + " = " + Farms.TABLE + "." + Farms.COL_FARM_OVSR + ")" +
                " ORDER BY FarmName COLLATE NOCASE";

        ArrayList<HashMap<String, String>> farmsList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> farms = new HashMap<>();
                farms.put("id", cursor.getString(cursor.getColumnIndex("FarmID")));
                farms.put("farmName", cursor.getString(cursor.getColumnIndex("FarmName")));
                farms.put("planterName", "Planter: " + cursor.getString(cursor.getColumnIndex("PlanterName")));

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

        int iCount =0;
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
        String selectQuery =  "SELECT farm_name FROM " + Farms.TABLE + " WHERE " + Farms.COL_FARM_NAME + " = '" + farmName + "'";

        Cursor cursor = db.rawQuery(selectQuery, null );
        System.out.println("Select farmname query: " + selectQuery);
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