package com.scbpfsdgis.femobilebetav20.data.repo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.scbpfsdgis.femobilebetav20.data.DBHelper;
import com.scbpfsdgis.femobilebetav20.data.DatabaseManager;
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
                Farms.COL_FARM_SVY + " TEXT, " +
                Farms.COL_FARM_CMT + " TEXT)";
    }

    public static String createTableExported() {
        return "CREATE TABLE " + Farms.TABLE_EXPORTED + " (" +
                Farms.COL_EXP_OBJ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Farms.COL_EXP_FARM_ID + " INTEGER, " +
                Farms.COL_EXP_DATE + " TEXT)";
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

    public void insertLog(List<String> list, String filename) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        for (int i=0; i<list.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(Farms.COL_EXP_FARM_ID, list.get(i));
            values.put(Farms.COL_EXP_DATE, filename);
            // Inserting Row
            db.insert(Farms.TABLE_EXPORTED, null, values);
        }
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
        values.put(Farms.COL_FARM_SVY, farms.getFarmSvy());

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

    public List<String> getFarmIDsExported(String query) {
        List<String> ids = new ArrayList<>();

        DBHelper dbHelper = new DBHelper();
        db =  dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return  ids;
    }
}