package com.scbpfsdgis.femobilebetav20;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.scbpfsdgis.femobilebetav20.data.model.Farms;
import com.scbpfsdgis.femobilebetav20.data.repo.FarmsRepo;
import com.scbpfsdgis.femobilebetav20.data.repo.PersonRepo;

import java.util.List;

public class FarmDetailActivity extends AppCompatActivity implements android.view.View.OnClickListener{

    Spinner spnPltr, spnOvsr;
    Button btnSave, btnClose;
    EditText etFarmName, etFarmLoc, etFarmCity, etFarmCmt;
    private int farmID = 0, planterID, ovsrID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_detail);

        initialize();
        btnSave.setOnClickListener(this);
        btnClose.setOnClickListener(this);

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
        if (farmID != 0) {
            planterID = Integer.parseInt(farm.getFarmPltrID());
            ovsrID = Integer.parseInt(farm.getFarmOvsrID());
        } else {
            planterID = 0;
            ovsrID = 0;
        }
        System.out.println("OnCreate FarmID: " + farmID);
        loadPlanters(farmID, planterID);
        loadOverseers(farmID, ovsrID);
    }

    private void initialize() {
        btnSave = (Button) findViewById(R.id.btnSaveFarm);
        btnClose = (Button) findViewById(R.id.btnClose);
        etFarmName = (EditText) findViewById(R.id.etFarmName);
        spnPltr = (Spinner) findViewById(R.id.spnPlanter);
        spnOvsr = (Spinner) findViewById(R.id.spnOvsr);
        etFarmLoc = (EditText) findViewById(R.id.etFarmLoc);
        etFarmCity = (EditText) findViewById(R.id.etFarmCity);
        etFarmCmt = (EditText) findViewById(R.id.etFarmCmt);
    }

    private void loadPlanters(int farmID, int pltrID) {

        String cls = "P";
        PersonRepo pltrRepo = new PersonRepo();
        List planterList =  pltrRepo.getPersonNamesList(cls);
        String planterName;
        addDefaultChoice(planterList, cls);

        if(planterList.size()!=0) {

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, planterList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnPltr.setAdapter(adapter);
            if (farmID!=0) {
                System.out.println("FarmID: " + farmID);
                planterName = pltrRepo.getPersonByID(pltrID).getPersonName() + " - ID:" + pltrID;
                int spnPost = adapter.getPosition(planterName);
                spnPltr.setSelection(spnPost);
                System.out.println("Planter Name: " + planterName + "; Position At: " + spnPost);
            }
        } else {
            Toast.makeText(this,"No planter records shit!",Toast.LENGTH_SHORT).show();
        }

    }

    private void addDefaultChoice(List list, String cls) {
        if (cls.equalsIgnoreCase("P")) {
            list.add(0, "Choose planter...");
        } else {
            list.add(0, "Choose overseer...");
        }
    }

    private void loadOverseers(int farmID, int ovsrID ) {
        String cls = "O";
        PersonRepo pltrRepo = new PersonRepo();
        List ovsrList =  pltrRepo.getPersonNamesList("O");
        addDefaultChoice(ovsrList, cls);
        String ovsrName;
        if(ovsrList.size()!=0) {

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ovsrList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnOvsr.setAdapter(adapter);
            if (farmID!=0) {
                System.out.println("FarmID: " + farmID);
                ovsrName = pltrRepo.getPersonByID(ovsrID).getPersonName() + " - ID:" + ovsrID;
                int spnPost = adapter.getPosition(ovsrName);
                spnOvsr.setSelection(spnPost);
                System.out.println("Overseer Name: " + ovsrName + "; Position At: " + spnPost);
            }
        } else {
            Toast.makeText(this,"No overseer records shit!",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.btnSaveFarm)){
            FarmsRepo repo = new FarmsRepo();
            Farms farm = new Farms();

            if (isValid()){
                String planter = spnPltr.getSelectedItem().toString();
                String ovsr = spnOvsr.getSelectedItem().toString();
                farm.setFarmID(farmID);
                farm.setFarmName(etFarmName.getText().toString());
                farm.setFarmPltrID(planter.substring(planter.lastIndexOf("ID:")+3));
                farm.setFarmOvsrID(ovsr.substring(ovsr.lastIndexOf("ID:")+3));
                farm.setFarmLoc(etFarmLoc.getText().toString());
                farm.setFarmCity(etFarmCity.getText().toString());
                farm.setFarmCmt(etFarmCmt.getText().toString());

                if (farmID == 0){
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
        } else if (view== findViewById(R.id.btnClose)){
            finish();
        }
    }

    private boolean isValid() {
        if (etFarmName.getText().toString().equals("")) {
            Toast.makeText(this,"Farm Name required.",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (spnPltr.getSelectedItemPosition() == 0) {
            Toast.makeText(this,"Assign a planter.",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (spnOvsr.getSelectedItemPosition() == 0) {
            Toast.makeText(this,"Assign an overseer. Choose N/A if none.",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}