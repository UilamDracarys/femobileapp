package com.scbpfsdgis.fdrmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.scbpfsdgis.fdrmobile.data.repo.FieldsRepo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by William on 1/27/2018.
 */

public class FieldsListActivity extends AppCompatActivity {
    TextView tvFieldID;
    String farmName;
    int farmID;

    @Override
    public void onRestart(){
        super.onRestart();
        listAll(farmID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fields_list);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        getSupportActionBar().setTitle("Field List - " + intent.getStringExtra("farmName"));

        farmID = 0;
        farmID = intent.getIntExtra("farmID", 0);

        listAll(farmID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the save_cancel; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_back, menu);
        menu.findItem(R.id.action_exportdb).setVisible(false);
        menu.findItem(R.id.action_restoreall).setVisible(false);
        menu.findItem(R.id.action_restoresel).setVisible(false);
        menu.findItem(R.id.action_backupdb).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent prIntent = getIntent();
                farmName = prIntent.getStringExtra("farmName");
                farmID = prIntent.getIntExtra("farmID", 0);

                Intent intent = new Intent(this,FieldDetailActivity.class);
                intent.putExtra("fieldId",0);
                intent.putExtra("farmID", farmID);
                intent.putExtra("farmName", farmName);
                startActivity(intent);
                return true;

            case R.id.action_back:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void listAll(final int farmId) {
        final FieldsRepo repo = new FieldsRepo();
        ArrayList<HashMap<String, String>> fieldsList =  repo.getFieldsList(farmId);
        if(fieldsList.size()!=0) {
            ListView lv = findViewById(R.id.lstFields);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tvFieldID = view.findViewById(R.id.fieldID);
                    int fieldID = Integer.parseInt(tvFieldID.getText().toString());
                    FarmsRepo repo = new FarmsRepo();
                    Farms farm = repo.getFarmByID(farmId);
                    String farmName = farm.getFarmName();
                    Intent objIndent = new Intent(getApplicationContext(), FieldDetailActivity.class);
                    objIndent.putExtra("fieldID", fieldID);
                    objIndent.putExtra("farmID", farmId);
                    objIndent.putExtra("farmName", farmName);
                    startActivity(objIndent);
                }
            });
            ListAdapter adapter = new SimpleAdapter( FieldsListActivity.this,fieldsList, R.layout.view_field_list_item, new String[] { "id","fieldName", "suitArea"}, new int[] {R.id.fieldID, R.id.fieldName, R.id.fldSuitArea});
            lv.setAdapter(adapter);
        } else {
            Toast.makeText(this,"No field records!",Toast.LENGTH_SHORT).show();
        }
    }
}