package com.scbpfsdgis.femobilebetav20;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.scbpfsdgis.femobilebetav20.data.repo.FieldsRepo;
import com.scbpfsdgis.femobilebetav20.data.repo.PersonRepo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by William on 1/27/2018.
 */

public class FieldsListActivity extends AppCompatActivity implements android.view.View.OnClickListener {
    Button btnAdd;
    TextView tvFieldID;
    TextView tvFarmName;
    String farmName;
    int farmID;
    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.btnAddField)){
            Intent prIntent = getIntent();
            farmName = prIntent.getStringExtra("farmName");
            farmID = prIntent.getIntExtra("farmID", 0);

            Intent intent = new Intent(this,FieldDetailActivity.class);
            intent.putExtra("fieldId",0);
            intent.putExtra("farmID", farmID);
            intent.putExtra("farmName", farmName);
            startActivity(intent);
        }
    }

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

        btnAdd = findViewById(R.id.btnAddField);
        btnAdd.setOnClickListener(this);


        Intent intent = getIntent();
        getSupportActionBar().setTitle("Field List - " + intent.getStringExtra("farmName"));

        farmID = 0;
        farmID = intent.getIntExtra("farmID", 0);

        listAll(farmID);
    }

    private void listAll(int farmId) {
        FieldsRepo repo = new FieldsRepo();
        ArrayList<HashMap<String, String>> fieldsList =  repo.getFieldsList(farmId);
        if(fieldsList.size()!=0) {
            ListView lv = findViewById(R.id.lstFields);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tvFieldID = findViewById(R.id.fieldID);
                    String fieldID = tvFieldID.getText().toString();
                    Intent objIndent;
                    objIndent = new Intent(getApplicationContext(), FarmDetailActivity.class);
                    objIndent.putExtra("farmID", fieldID);
                }

            });
            ListAdapter adapter = new SimpleAdapter( FieldsListActivity.this,fieldsList, R.layout.view_field_list_item, new String[] { "id","fieldName", "suitArea"}, new int[] {R.id.fieldID, R.id.fieldName, R.id.fldSuitArea});
            lv.setAdapter(adapter);
        } else {
            Toast.makeText(this,"No field records!",Toast.LENGTH_SHORT).show();
        }
    }
}

