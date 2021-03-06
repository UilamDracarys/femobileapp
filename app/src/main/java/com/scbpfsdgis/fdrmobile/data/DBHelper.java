package com.scbpfsdgis.fdrmobile.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.scbpfsdgis.fdrmobile.BuildConfig;
import com.scbpfsdgis.fdrmobile.app.App;
import com.scbpfsdgis.fdrmobile.data.model.Farms;
import com.scbpfsdgis.fdrmobile.data.model.Fields;
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
    private static final int DATABASE_VERSION =39;
    // Database Name
    private static final String DATABASE_NAME = "FEMobile.db";
    private static final String TAG = DBHelper.class.getSimpleName();

    public int getDatabaseVersion() {
        return DATABASE_VERSION;
    }

    public DBHelper() {
        super(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here
        db.execSQL(FarmsRepo.createTable());
        db.execSQL(PersonRepo.createTable());
        db.execSQL(FieldsRepo.createTableBsc());
        db.execSQL(FieldsRepo.createTableSuit());
        db.execSQL(FieldsRepo.createTableOthers());
        db.execSQL(FieldsRepo.createTableFldAtts());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));

        db.execSQL("DROP TABLE IF EXISTS " + Farms.TABLE_EXPORTED);
        if (oldVersion <= 21) {
            db.execSQL("ALTER TABLE " + Fields.TABLE_BSC + " ADD " + Fields.COL_FLD_SURVEYOR + " TEXT");
            db.execSQL("ALTER TABLE " + Farms.TABLE + " ADD " + Farms.COL_FARM_EXP + " TEXT");
        }
        if (oldVersion <=33 && oldVersion > 21) {
            db.execSQL("UPDATE fldAtts SET " + Fields.COL_FLD_ATT_CODE + " = 'P' WHERE " + Fields.COL_FLD_ATT_ID + " = 'fld_suit' AND " + Fields.COL_FLD_ATT_DESC + " = 'Partially Suitable' ");
        }
        if (oldVersion <= 34) {
            db.execSQL("UPDATE fldAtts SET " + Fields.COL_FLD_ATT_DESC + " = 'Asphalt-Good->=3m' WHERE " + Fields.COL_FLD_ATT_ID + " = 'AG>=3'");
            db.execSQL("UPDATE fldAtts SET " + Fields.COL_FLD_ATT_DESC + " = 'Graded-Good-<3m' WHERE " + Fields.COL_FLD_ATT_ID + " = 'GG<3'");
            db.execSQL("UPDATE fldAtts SET " + Fields.COL_FLD_ATT_DESC + " = 'Graded-Good->=3m' WHERE " + Fields.COL_FLD_ATT_ID + " = 'GG>=3'");
            db.execSQL("UPDATE fldAtts SET " + Fields.COL_FLD_ATT_DESC + " = 'NotGraded-Good->=3m' WHERE " + Fields.COL_FLD_ATT_ID + " = 'NGG>=3'");
            db.execSQL("UPDATE fldAtts SET " + Fields.COL_FLD_ATT_DESC + " = 'NotGraded-Good-<3m' WHERE " + Fields.COL_FLD_ATT_ID + " = 'NGG<3'");
        }
        if (oldVersion <= 38) {
            if (!isFieldExist(db, Fields.TABLE_BSC, Fields.COL_FLD_CROPCYCLE)) {
                db.execSQL("ALTER TABLE " + Fields.TABLE_BSC + " ADD " + Fields.COL_FLD_CROPCYCLE + " TEXT");
            }
            if (!isFieldExist(db, Fields.TABLE_BSC, Fields.COL_FLD_DP)) {
                db.execSQL("ALTER TABLE " + Fields.TABLE_BSC + " ADD " + Fields.COL_FLD_DP + " TEXT");
            }
            if (!isFieldExist(db, Fields.TABLE_BSC, Fields.COL_FLD_HD)) {
                db.execSQL("ALTER TABLE " + Fields.TABLE_BSC + " ADD " + Fields.COL_FLD_HD + " TEXT");
            }
        }
         onCreate(db);
    }

    public boolean isFieldExist(SQLiteDatabase db, String tableName, String fieldName) {
        boolean isExist = false;
        Cursor res = db.rawQuery("PRAGMA table_info("+tableName+")",null);
        res.moveToFirst();
        do {
            String currentColumn = res.getString(1);
            if (currentColumn.equals(fieldName)) {
                isExist = true;
            }
        } while (res.moveToNext());
        return isExist;
    }
}
