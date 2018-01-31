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
import com.scbpfsdgis.femobilebetav20.data.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by William on 1/7/2018.
 */

public class PersonRepo {
    private Person person;
    private DBHelper dbHelper;
    private SQLiteDatabase db;


    public PersonRepo() {
        person = new Person();
    }

    public static String createTable() {
        return "CREATE TABLE " + Person.TABLE + " (" +
                Person.COL_PRSN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Person.COL_PRSN_NAME + " TEXT, " +
                Person.COL_PRSN_CLS + " TEXT, " +
                Person.COL_PRSN_CONT + " TEXT)";
    }

    public void insert(Person person, String cls) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        //db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put(Person.COL_PRSN_NAME, person.getPersonName());
        values.put(Person.COL_PRSN_CLS, cls);
        values.put(Person.COL_PRSN_CONT, person.getPersonCont());

        // Inserting Row
        db.insert(Person.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void delete( ) {
        db = DatabaseManager.getInstance().openDatabase();
        //TODO: Code to delete person records;
        db.delete(Person.TABLE, null,null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void update(Person person) {
        dbHelper = new DBHelper();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Person.COL_PRSN_NAME, person.getPersonName());
        values.put(Person.COL_PRSN_CONT, person.getPersonCont());
        values.put(Person.COL_PRSN_CLS, person.getPersonCls());


        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Person.TABLE, values, Person.COL_PRSN_ID + " = ? ", new String[] { String.valueOf(person.getPersonID()) });
        db.close(); // Closing database connection
    }

    //Get List of Owners

    public ArrayList<HashMap<String, String>> getPersonList() {
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Person.COL_PRSN_ID + "," +
                Person.COL_PRSN_NAME + "," +
                Person.COL_PRSN_CONT + ", " +
                Person.COL_PRSN_CLS + " FROM " + Person.TABLE + " ORDER BY person_name COLLATE NOCASE" ;

        ArrayList<HashMap<String, String>> personList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> owner = new HashMap<String, String>();
                owner.put("id", cursor.getString(cursor.getColumnIndex(Person.COL_PRSN_ID)));
                owner.put("name", cursor.getString(cursor.getColumnIndex(Person.COL_PRSN_NAME)));
                owner.put("contact", cursor.getString(cursor.getColumnIndex(Person.COL_PRSN_CONT)));
                owner.put("class", cursor.getString(cursor.getColumnIndex(Person.COL_PRSN_CLS)));
                personList.add(owner);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return personList;
    }


    public List<String> getPersonNamesList(String cls) {
        List<String> names = new ArrayList<String>();

        DBHelper dbHelper = new DBHelper();
        String selectNames = "SELECT person_id, person_name FROM person ";
        if (!cls.equalsIgnoreCase("PO")) {
            selectNames += " WHERE person_class = '" + cls + "' or person_class = 'PO' ";
        }
        selectNames += " ORDER BY person_name COLLATE NOCASE";

        db =  dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectNames, null);

        if (cursor.moveToFirst()) {
            do {
                names.add(cursor.getString(1) + " - ID:" + cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return  names;
    }

    public Person getPersonByID(int Id){
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Person.COL_PRSN_ID + "," +
                Person.COL_PRSN_NAME + "," +
                Person.COL_PRSN_CONT + ", " +
                Person.COL_PRSN_CLS + " FROM " + Person.TABLE + " WHERE " + Person.COL_PRSN_ID + "=?";
        // It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        Person person = new Person();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                person.setPersonID(cursor.getInt(cursor.getColumnIndex(Person.COL_PRSN_ID)));
                person.setPersonName(cursor.getString(cursor.getColumnIndex(Person.COL_PRSN_NAME)));
                person.setPersonCont(cursor.getString(cursor.getColumnIndex(Person.COL_PRSN_CONT)));
                person.setPersonCls(cursor.getString(cursor.getColumnIndex(Person.COL_PRSN_CLS)));
                System.out.println("Person calss = " + person.getPersonCls());
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return person;
    }

    public boolean isPersonExisting(String personName) {
        dbHelper = new DBHelper();
        db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT person_name FROM " + Person.TABLE + " WHERE " + Person.COL_PRSN_NAME + " = '" + personName + "'";

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