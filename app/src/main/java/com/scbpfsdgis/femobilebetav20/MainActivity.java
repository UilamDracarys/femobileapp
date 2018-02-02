package com.scbpfsdgis.femobilebetav20;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.scbpfsdgis.femobilebetav20.data.DBHelper;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_WRITESTORAGE = 0;

    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.main_layout);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        System.out.println("Path: " + exportDir.getAbsolutePath());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_WRITESTORAGE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, "Storage access granted. Exporting file.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                export();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Storage access denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void export() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Snackbar.make(mLayout,
                    "Storage permission granted. Exporting file.",
                    Snackbar.LENGTH_SHORT).show();
            exportCSV();
        } else {
            // Permission is missing and must be requested.
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(mLayout, "Permission to access storage is required.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_WRITESTORAGE);
                }
            }).show();

        } else {
            Snackbar.make(mLayout,
                    "Permission is not available. Requesting storage permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_WRITESTORAGE);
        }
    }

    private void exportCSV() {
        DBHelper dbhelper = new DBHelper();
        File dbFile = getDatabasePath(dbhelper.getDatabaseName());

        File exportDir = new File(Environment.getExternalStorageDirectory() + "/FDRMobile/Exports" , "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();

        File farmDetails = new File(exportDir, "FD_" + dateFormat.format(date) + ".csv");
        File fieldsList = new File(exportDir, "FL_" + dateFormat.format(date) + ".csv");

        try {
            System.out.println("Filepath: " + farmDetails.getAbsolutePath());
            farmDetails.createNewFile();
            fieldsList.createNewFile();
            csvWriter(farmDetails, exportDir, "FD_",qryFarmDetails());
            csvWriter(farmDetails, exportDir, "FL_", qryFieldsList());
        }
        catch(Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }

    }

    private void csvWriter(File file, File exportDir, String suff, String query) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = new Date();
        File farmDetails = new File(exportDir, suff + dateFormat.format(date) + ".csv");
        DBHelper dbhelper = new DBHelper();
        try {
            CSVWriter csvWrite = new CSVWriter(new FileWriter(farmDetails));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery(query,null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                String arrStr[] = new String[curCSV.getColumnCount()];
                for (int i=0; i<curCSV.getColumnCount(); i++ ) {
                    arrStr[i] = curCSV.getString(i);
                }
                csvWrite.writeNext(arrStr);
            }
        csvWrite.close();
        curCSV.close();}
        catch(Exception sqlEx) {
                Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the save_cancel; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.export_db, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exportdb:
                export();
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

    private String qryFarmDetails() {
        return "SELECT f.farm_id as FarmID, f.farm_name AS FarmName, \n" +
                "(SELECT person_name FROM person WHERE person_id = f.farm_pltr_id) As PlanterName,\n" +
                "(SELECT person_name FROM person WHERE person_id = f.farm_ovsr_id) As OverseerName,\n" +
                "f.farm_locality As Locality, f.farm_city As City, f.farm_cmt As Comment FROM farms f LEFT JOIN person p\n" +
                "ON (f.farm_pltr_id = p.person_id AND f.farm_ovsr_id = p.person_id)\n" +
                "ORDER BY FarmName COLLATE NOCASE";
    }

    private String qryFieldsList() {
        return "SELECT Frm.FarmName as FarmName, Fld.FieldName as FieldName, Fld.Area as Area, Fld.Suitability as Suitability,\n" +
                "Fld.Limits as Limitations, Fld.Canals as Canals, Fld.RoadCond as RoadCondition, Fld.MechMeth as MechanizedMethod, Fld.TractorAcc as TractorAccess, Fld.RowWidth as RowWidth,\n" +
                "Fld.RowDir as RowDirection, Fld.SoilType as SoilType, Fld.Variety as Variety, Fld.HarvMeth as HarvestMethod,\n" +
                "Fld.CropClass as CropClass, Fld.Comment as Comment\n" +
                "FROM \n" +
                "(SELECT b.fld_id as FieldID, b.fld_farm_id as FarmID, b.fld_name as FieldName, s.fld_suit as Suitability, \n" +
                "b.fld_area as Area, s.fld_limits as Limits, o.fld_canals as Canals, s.fld_rdcond as RoadCond,\n" +
                "o.fld_mechmeth as MechMeth, o.fld_tractacc as TractorAcc, o.fld_rowwidth as RowWidth,\n" +
                "o.fld_rowdir as RowDir, b.fld_soilTyp as SoilType, b.fld_var as Variety, \n" +
                "o.fld_harvmeth as HarvMeth, o.fld_cropcls as CropClass, o.fld_cmt as Comment\n" +
                "FROM fldsBasic b JOIN fldsSuit s JOIN fldsOthers o ON (b.fld_id = o.fld_id) AND (b.fld_id = s.fld_id)) Fld\n" +
                "LEFT JOIN\n" +
                "(SELECT f.farm_id as FarmID, f.farm_name AS FarmName, \n" +
                "(SELECT person_name FROM person WHERE person_id = f.farm_pltr_id) As PlanterName,\n" +
                "(SELECT person_name FROM person WHERE person_id = f.farm_ovsr_id) As OverseerName,\n" +
                "f.farm_locality As Locality, f.farm_city As City, f.farm_cmt As Comment FROM farms f LEFT JOIN person p\n" +
                "ON (f.farm_pltr_id = p.person_id AND f.farm_ovsr_id = p.person_id)) Frm\n" +
                "ON Frm.FarmID = Fld.FarmID\n" +
                "ORDER BY FarmName, FieldName COLLATE NOCASE";
    }





}
