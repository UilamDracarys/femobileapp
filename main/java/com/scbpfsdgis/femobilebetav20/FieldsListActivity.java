package com.scbpfsdgis.femobilebetav20;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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

public class FieldsListActivity extends ListActivity implements android.view.View.OnClickListener {
    Button btnAdd;
    TextView tvFarmID;
    TextView tvFarmName;
    int farmID;
    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.btnAddField)){
            Intent intent = new Intent(this,FieldDetailActivity.class);
            intent.putExtra("fieldId",0);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fields_list);

        btnAdd = (Button) findViewById(R.id.btnAddField);
        btnAdd.setOnClickListener(this);

        tvFarmName = findViewById(R.id.tvListLabel);
        Intent intent = getIntent();
        String titleBar = "Fields List: " + intent.getStringExtra("farmName");
        tvFarmName.setText(titleBar);

        farmID = 0;
        farmID = intent.getIntExtra("farmID", 0);

        listAll(farmID);
    }

    private void listAll(int farmId) {
        FieldsRepo repo = new FieldsRepo();
        ArrayList<HashMap<String, String>> ownerList =  repo.getFieldsList(farmId);
        if(ownerList.size()!=0) {
            ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }

            });
            ListAdapter adapter = new SimpleAdapter( FieldsListActivity.this,ownerList, R.layout.view_people_list_item, new String[] { "id","name", "contact"}, new int[] {R.id.personID, R.id.personName, R.id.personCont});
            setListAdapter(adapter);
        } else {
            Toast.makeText(this,"No field records!",Toast.LENGTH_SHORT).show();
        }
    }
}

