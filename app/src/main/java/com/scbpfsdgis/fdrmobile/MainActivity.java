package com.scbpfsdgis.fdrmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.scbpfsdgis.fdrmobile.data.DBHelper;
import com.scbpfsdgis.fdrmobile.data.DatabaseManager;
import com.scbpfsdgis.fdrmobile.data.model.Fields;
import com.scbpfsdgis.fdrmobile.data.repo.FieldsRepo;

public class MainActivity extends AppCompatActivity {

    String versionName = BuildConfig.VERSION_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        initAttVals();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the save_cancel; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void showDialog() throws Resources.NotFoundException {
        DBHelper dbHelper = new DBHelper();
        new AlertDialog.Builder(this)

                .setTitle(getResources().getString(R.string.FDRMobile))
                .setMessage(
                        "App Version: " + versionName + "\n" +
                                "DB Version: " + dbHelper.getDatabaseVersion() )
                .setIcon(
                        getResources().getDrawable(
                                android.R.drawable.ic_dialog_info))
                .setPositiveButton(
                        getResources().getString(R.string.OKButton),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                            }
                        })
                .show();
    }
}