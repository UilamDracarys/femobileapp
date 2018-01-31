package com.scbpfsdgis.femobilebetav20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.scbpfsdgis.femobilebetav20.data.model.Farms;
import com.scbpfsdgis.femobilebetav20.data.repo.FarmsRepo;
import com.scbpfsdgis.femobilebetav20.data.repo.PersonRepo;

import java.util.List;

public class FarmDetailActivity extends AppCompatActivity {

    Spinner spnPltr, spnOvsr;
    EditText etFarmName, etFarmLoc, etFarmCity, etFarmCmt;
    private int farmID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_detail);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        initialize();

        farmID = 0;
        Intent intent = getIntent();
        farmID = intent.getIntExtra("farmID", 0);

        FarmsRepo repo = new FarmsRepo();
        Farms farm;
        farm = repo.getFarmByID(farmID);

        etFarmName.setText(farm.getFarmName());
        etFarmLoc.setText(farm.getFarmLoc());
        etFarmCity.setText(farm.getFarmCity());
        etFarmCmt.setText(farm.getFarmCmt());
        int planterID;
        int ovsrID;
        if (farmID != 0) {
            planterID = Integer.parseInt(farm.getFarmPltrID());
            String ovsrIDStr = farm.getFarmOvsrID();
            if(ovsrIDStr.equalsIgnoreCase("N/A")) {
                ovsrID = 0;
            } else {
                ovsrID = Integer.parseInt(ovsrIDStr);
            }
        } else {
            planterID = 0;
            ovsrID = 0;
        }
        loadPlanters(farmID, planterID);
        loadOverseers(farmID, ovsrID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the save_cancel; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_cancel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                return true;
            case R.id.action_cancel:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialize() {
        etFarmName =  findViewById(R.id.etFarmName);
        spnPltr = findViewById(R.id.spnPlanter);
        spnOvsr = findViewById(R.id.spnOvsr);
        etFarmLoc = findViewById(R.id.etFarmLoc);
        etFarmCity =  findViewById(R.id.etFarmCity);
        etFarmCmt =  findViewById(R.id.etFarmCmt);
    }

    private void loadPlanters(int farmID, int pltrID) {

        String cls = "P";
        PersonRepo pltrRepo = new PersonRepo();
        List<String> planterList =  pltrRepo.getPersonNamesList(cls);
        String planterName;
        addDefaultChoice(planterList, cls);

        if(planterList.size()!=0) {

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, planterList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnPltr.setAdapter(adapter);
            if (farmID!=0) {
                planterName = pltrRepo.getPersonByID(pltrID).getPersonName() + " - ID:" + pltrID;
                int spnPost = adapter.getPosition(planterName);
                spnPltr.setSelection(spnPost);
            }
        } else {
            Toast.makeText(this,"No planter records shit!",Toast.LENGTH_SHORT).show();
        }

    }

    private void addDefaultChoice(List<String> list, String cls) {
        if (cls.equalsIgnoreCase("P")) {
            list.add(0, "- Planter -");
        } else {
            list.add(0, "- Overseer -");
        }
    }

    private void loadOverseers(int farmID, int ovsrID ) {
        String cls = "O";
        PersonRepo pltrRepo = new PersonRepo();
        List<String> ovsrList =  pltrRepo.getPersonNamesList("O");
        addDefaultChoice(ovsrList, cls);
        String ovsrName;
        if(ovsrList.size()!=0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ovsrList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnOvsr.setAdapter(adapter);
            if (farmID!=0) {
                ovsrName = pltrRepo.getPersonByID(ovsrID).getPersonName() + " - ID:" + ovsrID;
                int spnPost = adapter.getPosition(ovsrName);
                spnOvsr.setSelection(spnPost);
            }
        } else {
            Toast.makeText(this,"No overseer records shit!",Toast.LENGTH_SHORT).show();
        }
    }

    private void save() {
        FarmsRepo repo = new FarmsRepo();
        Farms farm = new Farms();

        if (isValid()){

            String planter = spnPltr.getSelectedItem().toString();
            int ovsrIdx = spnOvsr.getSelectedItemPosition();
            String ovsr = spnOvsr.getSelectedItem().toString();
            farm.setFarmID(farmID);
            farm.setFarmName(etFarmName.getText().toString());
            farm.setFarmPltrID(planter.substring(planter.lastIndexOf("ID:")+3));
            if (ovsrIdx == 0) {
                farm.setFarmOvsrID("N/A");
            } else {
                farm.setFarmOvsrID(ovsr.substring(ovsr.lastIndexOf("ID:") + 3));
            }
            farm.setFarmLoc(etFarmLoc.getText().toString());
            farm.setFarmCity(etFarmCity.getText().toString());
            farm.setFarmCmt(etFarmCmt.getText().toString());

            if (farmID == 0){
                if (repo.isFarmExisting(farm.getFarmName())) {
                    Toast.makeText(this,"Farm " + farm.getFarmName() + " already exists.",Toast.LENGTH_SHORT).show();
                    return;
                }
                farmID = farm.getFarmID();
                repo.insert(farm);
                Toast.makeText(this,"New Farm Added",Toast.LENGTH_SHORT).show();
                finish();
            } else {
                repo.update(farm);
                Toast.makeText(this,"Farm Record updated",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean isValid() {
        String farmName = etFarmName.getText().toString();

        if (farmName.equals("")) {
            Toast.makeText(this,"Farm Name required.",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (spnPltr.getSelectedItemPosition() == 0) {
            Toast.makeText(this,"Assign a planter.",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}