package com.scbpfsdgis.fdrmobile;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.scbpfsdgis.fdrmobile.data.model.Farms;
import com.scbpfsdgis.fdrmobile.data.repo.FarmsRepo;
import com.scbpfsdgis.fdrmobile.data.DBHelper;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class FarmsListActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_WRITESTORAGE = 0;

    private View mLayout;
    TextView tvFarmID;

    String device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farms_list);
        mLayout = findViewById(R.id.farms_list_layout);

        Toolbar myToolbar =  findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        String action = intent.getStringExtra("action");
        if (action.equals("Manage Fields")) {
            getSupportActionBar().setTitle("Select Farm");
        }

        device = MainActivity.getDeviceName();
        listAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the save_cancel; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this,FarmDetailActivity.class);
                intent.putExtra("farmID",0);
                startActivity(intent);
                return true;

            case R.id.action_back:
                finish();
                return true;
            case R.id.action_exportdb:
                export();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        listAll();
    }


    private void listAll() {
        final FarmsRepo repo = new FarmsRepo();
        ArrayList<HashMap<String, String>> farmsList =  repo.getFarmsList();
        if(farmsList.size()!=0) {
            ListView lv = findViewById(R.id.list);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = getIntent();
                    Bundle b = intent.getExtras();

                    if (b!=null) {
                        String action = b.get("action").toString();
                        tvFarmID = view.findViewById(R.id.farmID);
                        String farmID = tvFarmID.getText().toString();
                        Farms farm = repo.getFarmByID(Integer.parseInt(farmID));
                        String farmName = farm.getFarmName();
                        Intent objIndent;
                        if (action.equalsIgnoreCase("Farm Details")) {
                            objIndent = new Intent(getApplicationContext(), FarmDetailActivity.class);
                            objIndent.putExtra("farmID", Integer.parseInt(farmID));
                        } else {
                            objIndent = new Intent(getApplicationContext(), FieldsListActivity.class);
                            objIndent.putExtra("farmID", Integer.parseInt(farmID));
                            objIndent.putExtra("farmName", farmName);
                        }
                        startActivity(objIndent);
                    }
                }
            });
            ListAdapter adapter = new SimpleAdapter( FarmsListActivity.this,farmsList, R.layout.view_farm_list_item, new String[] { "id","farmName", "planterName"}, new int[] {R.id.farmID, R.id.farmName, R.id.farmOwner});
            lv.setAdapter(adapter);
        }else{
            Toast.makeText(this,"No farm records!",Toast.LENGTH_SHORT).show();
        }
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
            exportCSV();
        } else {
            // Permission is missing and must be requested.
            requestStoragePermission();
        }
    }

    private boolean isExportValid() {
        DBHelper dbhelper = new DBHelper();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String selectFarmsNotExported = "SELECT b.fld_id as FieldID \n" +
                "FROM fldsBasic b \n" +
                "INNER JOIN farms f \n" +
                "ON b.fld_farm_id = f.farm_id \n" +
                "WHERE f.farm_exported IS NULL";
        Cursor cursor = db.rawQuery(selectFarmsNotExported,null);

        if (cursor.getCount() == 0) {
            if (isThereData()) {
                Snackbar.make(mLayout,
                        "All farms have already been exported. Please check FDRMobile/Exports directory.",
                        Snackbar.LENGTH_LONG).show();
                return false;
            } else {
                Snackbar.make(mLayout,
                        "No farm records found.",
                        Snackbar.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    private void exportCSV() {
        File exportDir = new File(Environment.getExternalStorageDirectory() + "/FDRMobile/Exports" , "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        DateFormat fileDF = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        final DateFormat dataDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        final Date date = new Date();

        final String fdFileName = "FARMS_" + fileDF.format(date) + ".csv";
        final String flcFilename = "FIELDS_" + fileDF.format(date) + "_CODE.csv";
        final String fldFilename = "FIELDS_" + fileDF.format(date) + "_DESC.csv";

        final File farmDetails = new File(exportDir, fdFileName);
        final File fldsListDesc = new File(exportDir, fldFilename);
        final File fldsListCode = new File(exportDir, flcFilename);

        if (isExportValid()) {
            try {
                new AlertDialog.Builder(this)
                        .setTitle(getResources().getString(R.string.ExportCSV))
                        .setMessage(
                                getResources().getString(R.string.ExportCSVConfirm))
                        .setIcon(
                                getResources().getDrawable(
                                        android.R.drawable.ic_dialog_info))
                        .setPositiveButton(
                                getResources().getString(R.string.ExportButton),
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        csvWriter(farmDetails, qryFarmDetails(dataDF.format(date)), fdFileName);
                                        csvWriter(fldsListDesc, qryFieldsDesc(), fldFilename);
                                        csvWriter(fldsListCode, qryFieldsCode(), flcFilename);
                                        updateExportDate(dataDF.format(date));
                                        Snackbar.make(mLayout,
                                                "Farms successfully exported FDRMobile/Exports.",
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton(
                                getResources().getString(R.string.CancelButton),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                    }
                                }).show();

            } catch (Exception sqlEx) {
                Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
            }
        }
    }

    private boolean isThereData() {
        DBHelper dbhelper = new DBHelper();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        String query = "SELECT farm_id FROM farms";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void csvWriter(File file, String query, String fileName) {
        DBHelper dbhelper = new DBHelper();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor curCSV = db.rawQuery(query, null);
        try {
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                String arrStr[] = new String[curCSV.getColumnCount()];
                for (int i = 0; i < curCSV.getColumnCount(); i++) {
                    arrStr[i] = curCSV.getString(i);
                }
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    public void updateExportDate(String value) {
        DBHelper dbHelper = new DBHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] ids = getFarmIDsToUpdate();

        for (int i=0; i<ids.length; i++) {
            values.put(Farms.COL_FARM_EXP, value);
            db.update(Farms.TABLE, values, Farms.COL_FARM_ID + "= ? ", new String[] { ids[i] });
        }

        db.close(); // Closing database connection
    }

    private String[] getFarmIDsToUpdate() {
        String[] ids;
        DBHelper dbhelper = new DBHelper();
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(qryFarmDetails(""), null);
        ids = new String[cursor.getCount()];
        if (cursor.moveToFirst()) {
            for (int i=0; i<cursor.getCount(); i++) {
                ids[i] = cursor.getString(cursor.getColumnIndex("FarmID"));
                cursor.moveToNext();
            }
        }
        return ids;
    }

    private String qryFarmDetails(String expDate) {
        return "SELECT f.farm_id as FarmID, f.farm_name AS FarmName, \n" +
                "(SELECT person_name FROM person WHERE person_id = f.farm_pltr_id) As PlanterName,\n" +
                "(SELECT person_name FROM person WHERE person_id = f.farm_ovsr_id) As OverseerName,\n" +
                "f.farm_locality As Locality, f.farm_city As City, f.farm_cmt As Comment,\n" +
                "\"" + expDate + "\" as DateExported, \"" + getDeviceName() + "\" as Device " +
                "FROM farms f LEFT JOIN person p\n" +
                "ON (f.farm_pltr_id = p.person_id AND f.farm_ovsr_id = p.person_id) \n" +
                "INNER JOIN fldsBasic b\n" +
                "on f.farm_id = b.fld_farm_id \n" +
                "WHERE f.farm_exported IS NULL\n" +
                "GROUP BY FarmID\n" +
                "ORDER BY FarmName COLLATE NOCASE";
    }

    private String qryFieldsCode() {
        return "SELECT Fld.FarmID as FarmID, Frm.FarmName as FarmName, Frm.PlanterName as Planter,\n" +
                "Frm.OverseerName as Overseer, Frm.Locality as Barangay, Frm.City as City,\n" +
                "Frm.Comment as FarmComment, Fld.FieldName as FldName, Fld.Area as OwnArea,\n" +
                "Fld.Suitability as FldSuit, Fld.Limits as MainLim, \n" +
                "\"-\" AS SecLim, \"-\" AS TertLim, Fld.Canals as Canal,\n" +
                "Fld.RoadCond as FldRoad, Fld.MechMeth as MechMeth, Fld.TractorAcc as TractAcc,\n" +
                "Fld.RowWidth as RowWidth, Fld.RowDir as RowDir, Fld.SoilType as SoiTyp,\n" +
                "Fld.Variety as Variety, Fld.HarvMeth as HarvMeth, Fld.CropClass as CropClas, Fld.Comment as FldComnt\n" +
                "FROM\n" +
                "(SELECT b.fld_id as FieldID, b.fld_farm_id as FarmID, b.fld_name as FieldName,\n" +
                "s.fld_suit as Suitability, b.fld_area as Area, s.fld_limits as Limits,\n" +
                "o.fld_canals as Canals, s.fld_rdcond as RoadCond, o.fld_mechmeth as MechMeth,\n" +
                "o.fld_tractacc as TractorAcc, o.fld_rowwidth as RowWidth, o.fld_rowdir as RowDir,\n" +
                "b.fld_soiltyp as SoilType, b.fld_var as Variety, o.fld_harvmeth as HarvMeth,\n" +
                "o.fld_cropcls as CropClass, o.fld_cmt as Comment\n" +
                "FROM fldsBasic b JOIN fldsSuit s JOIN fldsOthers o\n" +
                "ON (b.fld_id = o.fld_id) AND (b.fld_id = s.fld_id)) Fld\n" +
                "INNER JOIN\n" +
                "(SELECT f.farm_id as FarmID, f.farm_name AS FarmName,\n" +
                "(SELECT person_name FROM person WHERE person_id = f.farm_pltr_id) As PlanterName,\n" +
                "(SELECT person_name FROM person WHERE person_id = f.farm_ovsr_id) As OverseerName,\n" +
                "f.farm_locality As Locality, f.farm_city As City, f.farm_cmt As Comment\n" +
                "FROM farms f LEFT JOIN person p\n" +
                "ON (f.farm_pltr_id = p.person_id AND f.farm_ovsr_id = p.person_id)\n" +
                "WHERE f.farm_exported IS NULL) Frm\n" +
                "ON Fld.FarmID = Frm.FarmID\n" +
                "ORDER BY FarmName, FieldName COLLATE NOCASE";
    }

    private String qryFieldsDesc() {
        return "SELECT Fld.FarmID as FarmID, Frm.FarmName as FarmName, Frm.PlanterName as Planter,\n" +
                "Frm.OverseerName as Overseer, Frm.Locality as Barangay, Frm.City as City,\n" +
                "Frm.Comment as FarmComment, Fld.FieldName as FieldName, Fld.Area as Area,\n" +
                "Fld.Suitability as Suitability, Fld.Limits as Limitations, Fld.Canals as Canals,\n" +
                "Fld.RoadCond as RoadCondition, Fld.MechMeth as MechanizedMethod, Fld.TractorAcc as TractorAccess,\n" +
                "Fld.RowWidth as RowWidth, Fld.RowDir as RowDirection, Fld.SoilType as SoilType,\n" +
                "Fld.Variety as Variety, Fld.HarvMeth as HarvestMethod, Fld.CropClass as CropClass, Fld.Comment as FieldComment\n" +
                "FROM\n" +
                "(SELECT b.fld_id as FieldID, b.fld_farm_id as FarmID, b.fld_name as FieldName,\n" +
                "(SELECT fld_att_desc FROM fldAtts WHERE fld_att_code = s.fld_suit and fld_att_id = 'fld_suit') as Suitability, \n" +
                "b.fld_area as Area, s.fld_limits as Limits, o.fld_canals as Canals, \n" +
                "(SELECT fld_att_desc FROM fldAtts WHERE fld_att_code = s.fld_rdcond and fld_att_id = 'fld_rdcond') as RoadCond, \n" +
                "(SELECT fld_att_desc FROM fldAtts WHERE fld_att_code = o.fld_mechmeth and fld_att_id = 'fld_mechmeth') as MechMeth,\n" +
                "(SELECT fld_att_desc FROM fldAtts WHERE fld_att_code = o.fld_tractacc and fld_att_id = 'fld_tractacc') as TractorAcc, o.fld_rowwidth as RowWidth, \n" +
                "(SELECT fld_att_desc FROM fldAtts WHERE fld_att_code =  o.fld_rowdir and fld_att_id = 'fld_rowdir') as RowDir,\n" +
                "(SELECT fld_att_desc FROM fldAtts WHERE fld_att_code = b.fld_soilTyp and fld_att_id = 'fld_soiltyp') as SoilType, b.fld_var as Variety, \n" +
                "(SELECT fld_att_desc FROM fldAtts WHERE fld_att_code = o.fld_harvmeth and fld_att_id = 'fld_harvmeth') as HarvMeth,\n" +
                "(SELECT fld_att_desc FROM fldAtts WHERE fld_att_code = o.fld_cropcls and fld_att_id = 'fld_cropcls') as CropClass, o.fld_cmt as Comment\n" +
                "FROM fldsBasic b JOIN fldsSuit s JOIN fldsOthers o\n" +
                "ON (b.fld_id = o.fld_id) AND (b.fld_id = s.fld_id)) Fld\n" +
                "INNER JOIN\n" +
                "(SELECT f.farm_id as FarmID, f.farm_name AS FarmName,\n" +
                "(SELECT person_name FROM person WHERE person_id = f.farm_pltr_id) As PlanterName,\n" +
                "(SELECT person_name FROM person WHERE person_id = f.farm_ovsr_id) As OverseerName,\n" +
                "f.farm_locality As Locality, f.farm_city As City, f.farm_cmt As Comment\n" +
                "FROM farms f LEFT JOIN person p\n" +
                "ON (f.farm_pltr_id = p.person_id AND f.farm_ovsr_id = p.person_id)\n" +
                "WHERE f.farm_exported IS NULL) Frm\n" +
                "ON Fld.FarmID = Frm.FarmID\n" +
                "ORDER BY FarmName, FieldName COLLATE NOCASE";
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
                        ActivityCompat.requestPermissions(FarmsListActivity.this,
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

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        return manufacturer + " " + model;
    }
}
