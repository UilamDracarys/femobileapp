package com.scbpfsdgis.fdrmobile;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.scbpfsdgis.fdrmobile.data.DBHelper;
import com.scbpfsdgis.fdrmobile.data.DatabaseManager;
import com.scbpfsdgis.fdrmobile.data.model.Fields;
import com.scbpfsdgis.fdrmobile.data.repo.FieldsRepo;

import java.io.File;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        initAttVals();
    }

    public void showFarms(View view) {
        Intent intent = new Intent(this, FarmsListActivity.class);
        intent.putExtra("action", "Farm Details");
        startActivity(intent);
    }

    public void showFieldManager(View view) {
        Intent intent = new Intent(this, FarmsListActivity.class);
        intent.putExtra("action", "Manage Fields");
        startActivity(intent);
    }

    public void showPeople(View view) {
        Intent intent = new Intent(this, PeopleListActivity.class);
        startActivity(intent);
    }

    public void initAttVals() {
        DBHelper dbHelper = new DBHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        FieldsRepo repo = new FieldsRepo();
        int attCount = Fields.fldAtt.length;

        String selectQuery = "SELECT COUNT(" + Fields.COL_FLD_ATT_ID + ") AS AttID FROM " + Fields.TABLE_ATTVALS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (attCount != cursor.getCount()) {
            db.delete(Fields.TABLE_ATTVALS, "", null);
            repo.insertAttVals();
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
    }
}