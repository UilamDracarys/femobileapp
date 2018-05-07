package com.scbpfsdgis.fdrmobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.scbpfsdgis.fdrmobile.data.DBHelper;
import com.scbpfsdgis.fdrmobile.data.DatabaseManager;
import com.scbpfsdgis.fdrmobile.data.model.Fields;
import com.scbpfsdgis.fdrmobile.data.repo.FieldsRepo;

public class MainActivity extends AppCompatActivity {

    private static final int READ_PHONE_STATE_PERM = 0;
    String versionName = BuildConfig.VERSION_NAME;
    private View mLayout;

    String manufacturer, build, serial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.main_layout);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        initAttVals();
        initMenus();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        initMenus();
    }

    private void initMenus() {
        ListView menus = findViewById(R.id.menuList);
        String[] textString = {"Farm Details", "Surveys", "People & Groups"};
        String[] menuPreviews = {getFarmsSubTitle(), getFieldsSubtitle(), getPeopleSubtitle()};

        int[] drawableIds = {R.drawable.ic_farms, R.drawable.ic_surveys, R.drawable.ic_people};
        int[] drawableArrows = {R.drawable.ic_arrow, R.drawable.ic_arrow, R.drawable.ic_arrow};
        CustomAdapter adapter = new CustomAdapter(this, textString, menuPreviews, drawableIds, drawableArrows);
        menus.setAdapter(adapter);

        menus.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showFarms();
                        return;
                    case 1:
                        showFieldManager();
                        return;
                    case 2:
                        showPeople();
                        return;
                    default:
                }
            }

        });
    }

    private String getFarmsSubTitle() {
        DBHelper dbHelper = new DBHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "";

        String query = "SELECT COUNT(*) as TotalFarms, COUNT(farm_exported) as Exported , \n" +
                "(SELECT COUNT(DISTINCT f.farm_id) FROM farms f \n" +
                "JOIN fldsBasic b ON f.farm_id = b.fld_farm_id WHERE f.farm_exported IS NULL) as ExportReady,\n" +
                "(SELECT COUNT (distinct f.farm_id) AS NoFlds FROM farms f \n" +
                "LEFT JOIN fldsBasic b ON f.farm_id = b.fld_farm_id \n" +
                "WHERE f.farm_exported IS NULL AND b.fld_farm_id IS NULL) as NoFlds\n" +
                "FROM farms\n";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getInt(0) > 0) {
            str += cursor.getString(0) + " farm/s:\n" +
                    " • Ready to export: " + cursor.getString(2) +"\n" +
                    " • Without fields: " + cursor.getString (3) + "\n"+
                    " • Exported: " + cursor.getString(1);
        } else {
            str += "No records.";
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return str;
    }

    private String getFieldsSubtitle() {
        DBHelper dbHelper = new DBHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "";

        String query = "SELECT COUNT(*), SUM(fld_area) FROM fldsBasic";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getInt(0) > 0) {
            str += cursor.getString(0) + " flds, " + cursor.getString(1) + " has";
        } else {
            str += "No records.";
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return str;
    }

    private String getPeopleSubtitle() {
        DBHelper dbHelper = new DBHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String str = "";

        String query = "SELECT COUNT(*) FROM person";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.getInt(0) > 0) {
            str += cursor.getString(0) + " record/s";
        } else {
            str += "No records.";
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return str;
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
                showAppInfoDlg();
                return true;
            case R.id.action_whatsnew:
                showWhatsNew();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showFarms() {
        Intent intent = new Intent(this, FarmsListActivity.class);
        intent.putExtra("action", "Farm Details");
        startActivity(intent);
    }

    public void showFieldManager() {
        Intent intent = new Intent(this, FarmsListActivity.class);
        intent.putExtra("action", "Manage Fields");
        startActivity(intent);
    }

    public void showPeople() {
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



    private void showWhatsNew() {
        new AlertDialog.Builder(this)

                .setTitle(getResources().getString(R.string.WhatsNew))
                .setMessage("NEW: \n" +
                        "• Harvest schedule as field attributes.\n" +
                        "\n" +
                        "FIX:\n" +
                        "• Rename some varieties.")
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
    private void showAppInfoDlg() throws Resources.NotFoundException {
        DBHelper dbHelper = new DBHelper();
        new AlertDialog.Builder(this)

                .setTitle(getResources().getString(R.string.AppInfo))
                .setMessage("App Version: " + versionName + "\n" +
                        "DB Version: " + dbHelper.getDatabaseVersion() + "\n" +
                        "Device: " + getDeviceName())
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

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        //String serial = Build.getSerial();
        return manufacturer + " " + model;
    }

    public class CustomAdapter extends BaseAdapter {

        private Context mContext;
        private String[] Title;
        private String[] SubTitle;
        private int[] mnuIcon;
        private int[] rightArrow;

        public CustomAdapter(Context context, String[] menuTitle, String[] menuSubTitle, int[] imageIds, int[] arrows) {
            mContext = context;
            Title = menuTitle;
            SubTitle = menuSubTitle;
            mnuIcon = imageIds;
            rightArrow = arrows;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return Title.length;
        }

        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @SuppressLint("ViewHolder")
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row;
            row = inflater.inflate(R.layout.view_main_menu_item, parent, false);
            TextView title, subtitle;
            ImageView i1, ar;
            i1 = row.findViewById(R.id.icon);
            title = row.findViewById(R.id.menuName);
            subtitle = row.findViewById(R.id.menuPrev);
            ar = row.findViewById(R.id.arrow);
            title.setText(Title[position]);
            i1.setImageResource(mnuIcon[position]);
            ar.setImageResource(rightArrow[position]);
            subtitle.setText(SubTitle[position]);
            return (row);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == READ_PHONE_STATE_PERM) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, "Storage access granted. Exporting file.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                export();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Phone State access denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void export() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            //TODO
        } else {
            // Permission is missing and must be requested.
            requestPhoneStatePerm();
        }
    }
    private void requestPhoneStatePerm() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(mLayout, "Permission to access storage is required.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            READ_PHONE_STATE_PERM);
                }
            }).show();
        } else {
            Snackbar.make(mLayout,
                    "Permission is not available. Requesting storage permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    READ_PHONE_STATE_PERM);
        }
    }

}