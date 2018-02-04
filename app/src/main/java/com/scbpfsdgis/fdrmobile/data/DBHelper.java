package com.scbpfsdgis.fdrmobile.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.scbpfsdgis.fdrmobile.app.App;
import com.scbpfsdgis.fdrmobile.data.model.Farms;
import com.scbpfsdgis.fdrmobile.data.model.Fields;
import com.scbpfsdgis.fdrmobile.data.model.Person;
import com.scbpfsdgis.fdrmobile.data.repo.FarmsRepo;
import com.scbpfsdgis.fdrmobile.data.repo.FieldsRepo;
import com.scbpfsdgis.fdrmobile.data.repo.PersonRepo;

/**
 * Created by William on 1/7/2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION =21;
    // Database Name
    private static final String DATABASE_NAME = "FEMobile.db";
    private static final String TAG = DBHelper.class.getSimpleName();

    public DBHelper() {
        super(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here
        db.execSQL(FarmsRepo.createTable());
        db.execSQL(FarmsRepo.createTableExported());
        db.execSQL(PersonRepo.createTable());
        db.execSQL(FieldsRepo.createTableBsc());
        db.execSQL(FieldsRepo.createTableSuit());
        db.execSQL(FieldsRepo.createTableOthers());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));

        // Drop table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + Person.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Farms.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Farms.TABLE_EXPORTED);
        db.execSQL("DROP TABLE IF EXISTS " + Fields.TABLE_BSC);
        db.execSQL("DROP TABLE IF EXISTS " + Fields.TABLE_SUIT);
        db.execSQL("DROP TABLE IF EXISTS " + Fields.TABLE_OTHERS);
        onCreate(db);
    }

}
