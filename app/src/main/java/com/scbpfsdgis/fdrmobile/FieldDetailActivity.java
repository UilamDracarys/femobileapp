package com.scbpfsdgis.fdrmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.guna.libmultispinner.MultiSelectionSpinner;
import com.scbpfsdgis.fdrmobile.data.model.Fields;
import com.scbpfsdgis.fdrmobile.data.repo.FieldsRepo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by William on 1/27/2018.
 */

public class FieldDetailActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener{

    Spinner spnMechMeth, spnTract, spnSuit, spnRdCond, spnRowDir, spnSoilType, spnCropClass, spnHarvMeth;
    EditText etFldName, etFldArea, etRowWidth, etVar, etCmt;
    MultiSelectionSpinner mssLimits, mssCanals;
    TextView frmName;
    private int fieldId, farmId;
    String farmName;
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);
        mLayout = findViewById(R.id.field_detail_layout);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        initialize();

        fieldId = 0;
        Intent intent = getIntent();
        farmId = intent.getIntExtra("farmID", 0);
        farmName = intent.getStringExtra("farmName");
        fieldId = intent.getIntExtra("fieldID", 0);

        FieldsRepo repo = new FieldsRepo();
        Fields fields = repo.getFieldByID(fieldId);

        //Set ActionBar title
        String title = "Field Details - ";
        if (fieldId ==0) {
            title += farmName;
        } else {
            title += farmName + " Fld. " + fields.getFldName();
        }
        getSupportActionBar().setTitle(title);

        //Set MultiSelectionSpinner items
        setMSSItems(R.array.limitations, mssLimits);
        setMSSItems(R.array.canals, mssCanals);

        //Set values from database
        if (fieldId != 0) {
            System.out.println("Field ID in Details: " + fieldId);
            etFldName.setText(fields.getFldName());
            etFldArea.setText(String.valueOf(fields.getFldArea()));
            etRowWidth.setText(fields.getFldRowWidth());
            etVar.setText(fields.getFldVar());
            etCmt.setText(fields.getFldCmt());
            spnSuit.setSelection(fields.getIdxByCode(getResources().getStringArray(R.array.suitability), fields.getFldSuit()));
            spnRdCond.setSelection(fields.getIdxByCode(getResources().getStringArray(R.array.roadcond), fields.getFldRdCond()));
            spnMechMeth.setSelection(Integer.parseInt(fields.getFldMechMeth()) + 1);
            spnTract.setSelection(Integer.parseInt(fields.getFldTractAcc()) + 1);
            spnRowDir.setSelection(fields.getIdxByCode(getResources().getStringArray(R.array.rowdir), fields.getFldRowDir()));
            spnSoilType.setSelection(fields.getIdxByCode(getResources().getStringArray(R.array.soilTypes), fields.getFldSoilTyp()));
            spnHarvMeth.setSelection(fields.getIdxByCode(getResources().getStringArray(R.array.hvstmeth), fields.getFldHarvMeth()));
            spnCropClass.setSelection(fields.getIdxByCode(getResources().getStringArray(R.array.cropclass), fields.getFldCropCls()));
            String[] limits = fields.getFldLimits().split(",");
            String[] canals = fields.getFldCanals().split(",");
            if(limits.length != 0 && !limits[0].equals("-")) {
                mssLimits.setSelection(fields.getIndexArray(limits, getResources().getStringArray(R.array.limitations)));
            }
            if(canals.length != 0 && !canals[0].equals("TBD")) {
                mssCanals.setSelection(fields.getIndexArray(canals, getResources().getStringArray(R.array.canals)));
            }
        }
    }

    private void setMSSItems(int id, MultiSelectionSpinner mss) {
        String[] items = getResources().getStringArray(id);
        List<String> list = new ArrayList<>(Arrays.asList(items));
        mss.setItems(list);
        mss.setListener(this);
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

    public String getCode(String str) {
        return str.substring(str.indexOf("(") + 1, str.indexOf(")"));
    }

    public boolean isValid() {
        String fieldName = etFldName.getText().toString();

        if (fieldName.equalsIgnoreCase("")) {
            Snackbar.make(mLayout,"Field name required.", Snackbar.LENGTH_SHORT).show();
            etFldName.requestFocus();
            return false;
        }
        if (etFldArea.getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(mLayout,"Field area required.", Snackbar.LENGTH_SHORT).show();
            etFldArea.requestFocus();
            return false;
        }
        if (spnSuit.getSelectedItemPosition() == 0) {
            Snackbar.make(mLayout,"Choose suitability.", Snackbar.LENGTH_SHORT).show();
            spnSuit.requestFocus();
            return false;
        }
        if (spnRdCond.getSelectedItemPosition() == 0) {
            Snackbar.make(mLayout,"Choose road condition.", Snackbar.LENGTH_SHORT).show();
            spnRdCond.requestFocus();
            return false;
        }
        if (spnMechMeth.getSelectedItemPosition() == 0) {
            Snackbar.make(mLayout,"Mechanized method required.", Snackbar.LENGTH_SHORT).show();
            spnMechMeth.requestFocus();
            return false;
        }
        if (spnTract.getSelectedItemPosition() == 0) {
            Snackbar.make(mLayout,"Tractor access required.", Snackbar.LENGTH_SHORT).show();
            spnTract.requestFocus();
            return false;
        }
        if (etRowWidth.getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(mLayout,"Row width requuired.", Snackbar.LENGTH_SHORT).show();
            etRowWidth.requestFocus();
            return false;
        }
        if (spnRowDir.getSelectedItemPosition() == 0) {
            Snackbar.make(mLayout,"Choose row direction.", Snackbar.LENGTH_SHORT).show();
            spnRowDir.requestFocus();
            return false;
        }
        if (spnSoilType.getSelectedItemPosition() == 0) {
            Snackbar.make(mLayout,"Choose soil type.", Snackbar.LENGTH_SHORT).show();
            spnSoilType.requestFocus();
            return false;
        }
        if (etVar.getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(mLayout,"Variety required.", Snackbar.LENGTH_SHORT).show();
            etVar.requestFocus();
            return false;
        }
        if (spnHarvMeth.getSelectedItemPosition() == 0) {
            Snackbar.make(mLayout,"Choose harvest method.", Snackbar.LENGTH_SHORT).show();
            spnHarvMeth.requestFocus();
            return false;
        }
        if (spnCropClass.getSelectedItemPosition() == 0) {
            Snackbar.make(mLayout,"Choose crop class.", Snackbar.LENGTH_SHORT).show();
            spnCropClass.requestFocus();
            return false;
        }
        return true;
    }

    private void save() {
        FieldsRepo repo = new FieldsRepo();
        Fields fields = new Fields();

        if (isValid()) {

            fields.setFldId(String.valueOf(fieldId));
            fields.setFldName(etFldName.getText().toString());
            fields.setFldArea(Double.parseDouble(etFldArea.getText().toString()));
            fields.setFldSuit(getCode(spnSuit.getSelectedItem().toString()));
            fields.setFldFarmId(String.valueOf(farmId));
            System.out.println("Edit Farm ID: " + fields.getFldFarmId());
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

            if (fieldId == 0) {
                if (repo.isFieldExisting(fields.getFldName(), Integer.parseInt(fields.getFldFarmId()))) {
                    Toast.makeText(this, "Field " + fields.getFldName() + " of " + farmName + " already exists.", Toast.LENGTH_SHORT).show();
                    etFldName.requestFocus();
                    return;
                }
                repo.insertBsc(fields);
                fields.setFldId(String.valueOf(repo.getFldId()));
                repo.insertSuit(fields);
                repo.insertOthers(fields);
                Toast.makeText(this, "New Field Added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                System.out.println("Updated Field Name: " + fields.getFldName());
                repo.updateBsc(fields);
                repo.updateSuit(fields);
                repo.updateOthers(fields);
                Toast.makeText(this, "Field Record updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }

}