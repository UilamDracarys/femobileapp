package com.scbpfsdgis.femobilebetav20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.guna.libmultispinner.MultiSelectionSpinner;
import com.scbpfsdgis.femobilebetav20.data.model.Fields;
import com.scbpfsdgis.femobilebetav20.data.repo.FieldsRepo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by William on 1/27/2018.
 */

public class FieldDetailActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener, android.view.View.OnClickListener {

    Spinner spnMechMeth, spnTract, spnSuit, spnRdCond, spnRowDir, spnSoilType, spnCropClass, spnHarvMeth;
    EditText etFldName, etFldArea, etRowWidth, etVar, etCmt;
    MultiSelectionSpinner mssLimits, mssCanals;
    TextView frmName;
    Button btnSave, btnCancel;
    private int fieldId, farmId;
    String farmName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);

        btnSave = findViewById(R.id.btnSaveFld);
        btnCancel = findViewById(R.id.btnCancel);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        initialize();

        String[] limits = getResources().getStringArray(R.array.limitations);
        List<String> limitList = new ArrayList<>(Arrays.asList(limits));
        String[] canals = getResources().getStringArray(R.array.canals);
        List<String> canalList = new ArrayList<>(Arrays.asList(canals));

        fieldId = 0;

        Intent intent = getIntent();
        farmId = intent.getIntExtra("farmID", 0);
        farmName = intent.getStringExtra("farmName");
        fieldId = intent.getIntExtra("fieldID", 0);

        FieldsRepo repo = new FieldsRepo();
        Fields fields = repo.getFieldByID(fieldId);

        String title = "Field Details - ";
        if (fieldId ==0) {
            title += farmName;
        } else {
            title += farmName + " Fld. " + fields.getFldName();
        }
        getSupportActionBar().setTitle(title);

        frmName.setText(farmName);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        if (fieldId != 0) {
            System.out.println("Field ID in Details: " + fieldId);
            etFldName.setText(fields.getFldName());
            etFldArea.setText(String.valueOf(fields.getFldArea()));
            etRowWidth.setText(fields.getFldRowWidth());
            etVar.setText(fields.getFldVar());
            etCmt.setText(fields.getFldCmt());
        }

        mssLimits.setItems(limitList);
        mssLimits.setListener(this);
        mssCanals.setItems(canalList);
        mssCanals.setListener(this);
    }

    private void initialize() {
        spnMechMeth = findViewById(R.id.spnMechMeth);
        spnTract = findViewById(R.id.spnTract);
        spnSuit = findViewById(R.id.spnSuit);
        spnRdCond = findViewById(R.id.spnRdCond);
        spnRowDir = findViewById(R.id.spnRowDir);
        mssLimits = findViewById(R.id.spnMainLim);
        mssCanals = findViewById(R.id.spnCanals);
        spnSoilType = findViewById(R.id.spnSoilTyp);
        spnCropClass = findViewById(R.id.spnCropCls);
        spnHarvMeth = findViewById(R.id.spnHarvMeth);
        etFldName  = findViewById(R.id.etFieldName);
        etFldArea = findViewById(R.id.etFieldArea);
        etRowWidth = findViewById(R.id.etRowWidth);
        etVar = findViewById(R.id.etFieldVar);
        etCmt = findViewById(R.id.etFldCmt);
        frmName = findViewById(R.id.frmName);

    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.btnSaveFld)){
            FieldsRepo repo = new FieldsRepo();
            Fields fields = new Fields();

            if (isValid()){

                if (fieldId == 0){

                    fields.setFldName(etFldName.getText().toString());
                    fields.setFldArea(Double.parseDouble(etFldArea.getText().toString()));
                    fields.setFldSuit(getCode(spnSuit.getSelectedItem().toString()));
                    fields.setFldFarmId(String.valueOf(farmId));
                    String limits = mssLimits.getSelectedItemsAsString();
                    String canals = mssCanals.getSelectedItemsAsString();
                    if (limits.equalsIgnoreCase("")) {
                        limits = "-";
                    }
                    if (canals.equalsIgnoreCase("")) {
                        canals = "TBD";
                    }
                    fields.setFldLimits(limits);
                    fields.setFldCanals(canals);
                    fields.setFldRdCond(getCode(spnRdCond.getSelectedItem().toString()));
                    fields.setFldMechMeth(getCode(spnMechMeth.getSelectedItem().toString()));
                    fields.setFldTractAcc(getCode(spnTract.getSelectedItem().toString()));
                    fields.setFldRowWidth(etRowWidth.getText().toString());
                    fields.setFldRowDir(getCode(spnRowDir.getSelectedItem().toString()));
                    fields.setFldSoilTyp(getCode(spnSoilType.getSelectedItem().toString()));
                    fields.setFldVar(etVar.getText().toString());
                    fields.setFldHarvMeth(getCode(spnHarvMeth.getSelectedItem().toString()));
                    fields.setFldCropCls(getCode(spnCropClass.getSelectedItem().toString()));
                    fields.setFldCmt(etCmt.getText().toString());

                    //fieldId = Integer.parseInt(fields.getFldId());
                    repo.insertBsc(fields);
                    fields.setFldId(String.valueOf(repo.getFldId()));
                    repo.insertSuit(fields);
                    repo.insertOthers(fields);
                    Toast.makeText(this,"New Field Added",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //repo.update(farm);
                    Toast.makeText(this,"Field Record updated",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        } else if (view == findViewById(R.id.btnCancel)){
            finish();
        }
    }

    public String getCode(String str) {
        return str.substring(str.indexOf("(") + 1, str.indexOf(")"));
    }

    public boolean isValid() {
        String fieldName = etFldName.getText().toString();
        String farmName = this.farmName;
        int farmID = this.farmId;
        FieldsRepo repo = new FieldsRepo();

        if (fieldName.equalsIgnoreCase("")) {
            Toast.makeText(this,"Field name required.",Toast.LENGTH_SHORT).show();
            etFldName.requestFocus();
            return false;
        }
        if (etFldArea.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this,"Field area required.",Toast.LENGTH_SHORT).show();
            etFldArea.requestFocus();
            return false;
        }
        if (spnSuit.getSelectedItemPosition() == 0) {
            Toast.makeText(this,"Choose suitability.",Toast.LENGTH_SHORT).show();
            spnSuit.requestFocus();
            return false;
        }
        if (spnRdCond.getSelectedItemPosition() == 0) {
            Toast.makeText(this,"Choose road condition.",Toast.LENGTH_SHORT).show();
            spnRdCond.requestFocus();
            return false;
        }
        if (spnMechMeth.getSelectedItemPosition() == 0) {
            Toast.makeText(this,"Mechanized method.",Toast.LENGTH_SHORT).show();
            spnMechMeth.requestFocus();
            return false;
        }
        if (spnTract.getSelectedItemPosition() == 0) {
            Toast.makeText(this,"Tractor access.",Toast.LENGTH_SHORT).show();
            spnTract.requestFocus();
            return false;
        }
        if (etRowWidth.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this,"Row width required.",Toast.LENGTH_SHORT).show();
            etRowWidth.requestFocus();
            return false;
        }
        if (spnRowDir.getSelectedItemPosition() == 0) {
            Toast.makeText(this,"Choose row direction.",Toast.LENGTH_SHORT).show();
            spnRowDir.requestFocus();
            return false;
        }
        if (spnSoilType.getSelectedItemPosition() == 0) {
            Toast.makeText(this,"Choose soil type.",Toast.LENGTH_SHORT).show();
            spnSoilType.requestFocus();
            return false;
        }
        if (etVar.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(this,"Variety required.",Toast.LENGTH_SHORT).show();
            etVar.requestFocus();
            return false;
        }
        if (spnHarvMeth.getSelectedItemPosition() == 0) {
            Toast.makeText(this,"Choose harvest method.",Toast.LENGTH_SHORT).show();
            spnHarvMeth.requestFocus();
            return false;
        }
        if (spnCropClass.getSelectedItemPosition() == 0) {
            Toast.makeText(this,"Choose crop class.",Toast.LENGTH_SHORT).show();
            spnCropClass.requestFocus();
            return false;
        }

        if (repo.isFieldExisting(fieldName, farmID)) {
            Toast.makeText(this,"Field " + fieldName + " of " + farmName + " already exists.",Toast.LENGTH_SHORT).show();
            etFldName.requestFocus();
            return false;
        }

        return true;
    }


    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }

}