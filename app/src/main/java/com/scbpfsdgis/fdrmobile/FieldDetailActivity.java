package com.scbpfsdgis.fdrmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.guna.libmultispinner.MultiSelectionSpinner;
import com.scbpfsdgis.fdrmobile.data.model.Fields;
import com.scbpfsdgis.fdrmobile.data.repo.FieldsRepo;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by William on 1/27/2018.
 */

public class FieldDetailActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener, DatePickerDialog.OnDateSetListener,  android.view.View.OnClickListener{

    Spinner spnMechMeth, spnTract, spnSuit, spnRdCond, spnRowDir, spnSoilType, spnCropClass, spnHarvMeth, spnHS, spnCropCycle;
    EditText etFldName, etFldArea, etRowWidth, etCmt;
    AutoCompleteTextView etFldSvyor, etVar;
    MultiSelectionSpinner mssLimits, mssCanals;
    TextView frmName, tvHS;
    Date harvestSched = null;
    private int fieldId, farmId;
    String farmName;
    String[] surveyors = null, varieties = null;
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

        surveyors = repo.getValues(Fields.COL_FLD_SURVEYOR, Fields.TABLE_BSC);
        varieties = repo.getValues(Fields.COL_FLD_VAR, Fields.TABLE_BSC);
        if (surveyors != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, surveyors);
            etFldSvyor.setAdapter(adapter);
            etFldSvyor.setThreshold(0);
        }
        if (varieties != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, varieties);
            etVar.setAdapter(adapter);
            etVar.setThreshold(2);
        }

        //Set ActionBar title
        String title = "Field Details - ";
        if (fieldId == 0) {
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
            DateFormat df = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
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
            etFldSvyor.setText(fields.getFldSurveyor());

            String strHS = "";

            if (fields.getFldDatePlanted() == null) {
                if (fields.getFldHarvestDate() == null) {
                    System.out.println("NO HS SET!");
                    spnHS.setSelection(0);
                    spnCropCycle.setSelection(0);
                    strHS = "Tap to Set";
                } else {
                    System.out.println("DP: " + fields.getFldDatePlanted());
                    spnHS.setSelection(1);
                    spnCropCycle.setVisibility(View.GONE);
                    harvestSched = strToDate(fields.getFldHarvestDate());
                }
            } else {
                harvestSched = strToDate(fields.getFldDatePlanted());
                spnCropCycle.setVisibility(View.VISIBLE);
                spnCropCycle.setSelection(Integer.parseInt(fields.getFldCropCycle()) - 9);
            }

            if (strHS.equals("Tap to Set")) {
                tvHS.setText(strHS);
            } else {
                tvHS.setText(df.format(harvestSched));
            }

            String[] limits = fields.getFldLimits().split(",");
            String[] canals = fields.getFldCanals().split(",");
            if(limits.length != 0 && !limits[0].equals("-")) {
                mssLimits.setSelection(fields.getIndexArray(limits, getResources().getStringArray(R.array.limitations)));
            }
            if(canals.length != 0 && !canals[0].equals("TBD")) {
                mssCanals.setSelection(fields.getIndexArray(canals, getResources().getStringArray(R.array.canals)));
            }
        }

        spnHS = findViewById(R.id.spnHS);
        spnCropCycle = findViewById(R.id.spnCropCycle);
        spnHS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spnHS.getSelectedItemPosition() == 1) {
                    spnCropCycle.setVisibility(View.GONE);
                } else {
                    spnCropCycle.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }

        });
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.tvHSDate)) {
            System.out.println("DATECLICKED");
            showDatePicker();
        }
    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                FieldDetailActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "HarvestSched");
    }

    @Override
    public void onBackPressed() {
        showDiscardDialog(fieldId);
    }


    private void showDiscardDialog(int fieldId) {
        FieldsRepo repo = new FieldsRepo();
        Fields fld = repo.getFieldByID(fieldId);
        DateFormat df = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());

        String[] defaults;
        if (fieldId == 0) {
            defaults = new String[]{"", "", "0", "", "", "0", "0", "0", "", "0", "0", "", "0", "0", "", ""};
        } else {
            defaults = new String[]{fld.getFldName(), String.valueOf(fld.getFldArea()), fld.getFldSuit(), fld.getFldLimits(), fld.getFldCanals(), fld.getFldRdCond(),
                    fld.getFldMechMeth(), fld.getFldTractAcc(), fld.getFldRowWidth(), fld.getFldRowDir(), fld.getFldSoilTyp(), fld.getFldVar(), fld.getFldHarvMeth(),
                    fld.getFldCropCls(), fld.getFldCmt(), fld.getFldSurveyor()};
        }

        String ciSuit = spnSuit.getSelectedItem().toString();
        if (spnSuit.getSelectedItemPosition() !=0) {
            ciSuit = ciSuit.substring(ciSuit.indexOf("(")+1, ciSuit.indexOf(")"));
        }

        String ciMechMeth = spnMechMeth.getSelectedItem().toString();
        if (spnMechMeth.getSelectedItemPosition() !=0) {
            ciMechMeth = ciMechMeth.substring(ciMechMeth.indexOf("(") + 1, ciMechMeth.indexOf(")"));
        }
        String ciTractAcc = spnTract.getSelectedItem().toString();
        if (spnTract.getSelectedItemPosition() !=0) {
            ciTractAcc = ciTractAcc.substring(ciTractAcc.indexOf("(") + 1, ciTractAcc.indexOf(")"));
        }
        String ciRowDir = spnRowDir.getSelectedItem().toString();
        if (spnRowDir.getSelectedItemPosition() !=0) {
            ciRowDir = ciRowDir.substring(ciRowDir.indexOf("(")+1, ciRowDir.indexOf(")"));
        }
        String ciSoilType = spnSoilType.getSelectedItem().toString();
        if (spnSoilType.getSelectedItemPosition() !=0) {
            ciSoilType = ciSoilType.substring(ciSoilType.indexOf("(")+1, ciSoilType.indexOf(")"));
        }
        String ciHarvMeth = spnHarvMeth.getSelectedItem().toString();
        if (spnHarvMeth.getSelectedItemPosition() !=0) {
            ciHarvMeth = ciHarvMeth.substring(ciHarvMeth.indexOf("(")+1, ciHarvMeth.indexOf(")"));
        }
        String ciRdCond = spnRdCond.getSelectedItem().toString();
        if (spnRdCond.getSelectedItemPosition() !=0) {
            ciRdCond = ciRdCond.substring(ciRdCond.indexOf("(")+1, ciRdCond.indexOf(")"));
        }
        String ciCropCls = spnCropClass.getSelectedItem().toString();
        if (spnCropClass.getSelectedItemPosition() !=0) {
            ciCropCls = ciCropCls.substring(ciCropCls.indexOf("(")+1, ciCropCls.indexOf(")"));
        }
        String limits = mssLimits.getSelectedItemsAsString();
        if (limits.equalsIgnoreCase("")) {
            limits = "-";
        }
        String canals = mssCanals.getSelectedItemsAsString();
        if (canals.equalsIgnoreCase("")) {
            canals = "TBD";
        }

        String[] currentInput = {etFldName.getText().toString(), etFldArea.getText().toString(), ciSuit, limits, canals,
                ciRdCond, ciMechMeth, ciTractAcc, String.valueOf(etRowWidth.getText().toString()),
                ciRowDir, ciSoilType, etVar.getText().toString(), ciHarvMeth,
                ciCropCls, etCmt.getText().toString(), etFldSvyor.getText().toString()};

        int count = 0;

        for (int i = 0; i < defaults.length; i++) {
            System.out.println(i + " " + defaults[i] + " vs " + currentInput[i]);
            if (!defaults[i].equals(currentInput[i])) {
                count += 1;
            }
        }

        if (count > 0) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.discard))
                    .setMessage(
                            getResources().getString(R.string.discardMsg))
                    .setIcon(
                            getResources().getDrawable(
                                    android.R.drawable.ic_dialog_alert))
                    .setPositiveButton(
                            getResources().getString(R.string.DiscardBtn),
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
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
        } else {
            finish();
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
                showDiscardDialog(fieldId);
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
        etFldSvyor = findViewById(R.id.etFldSvyor);
        tvHS = findViewById(R.id.tvHSDate);
        tvHS.setOnClickListener(this);
        spnHS = findViewById(R.id.spnHS);
        spnCropCycle = findViewById(R.id.spnCropCycle);
    }

    public String getCode(String str) {
        return str.substring(str.indexOf("(") + 1, str.indexOf(")"));
    }

    public boolean isValid() {
        String fieldName = etFldName.getText().toString();
        Date date = new Date();

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
        if (Double.parseDouble(etRowWidth.getText().toString()) >= 2) {
            Snackbar.make(mLayout,"Allowed row width is < 2m only.", Snackbar.LENGTH_SHORT).show();
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
        if (etFldSvyor.getText().toString().equals("")) {
            Snackbar.make(mLayout,"Surveyor name required.", Snackbar.LENGTH_SHORT).show();
            etFldSvyor.requestFocus();
            return false;
        }
        if (mssLimits.getSelectedItemsAsString().equalsIgnoreCase("HCA") && mssCanals.getSelectedItemsAsString().equalsIgnoreCase("")) {
            Snackbar.make(mLayout,"You have canals as your limitations. Please select canal locations.", Snackbar.LENGTH_LONG).show();
            mssCanals.requestFocus();
            return false;
        }
        if (mssLimits.getSelectedItemsAsString().equalsIgnoreCase("HAR") && Double.parseDouble(etFldArea.getText().toString()) >= 0.8) {
            Snackbar.make(mLayout,"You have 'Area < 0.80ha' as your limitations but entered area is >= 0.80", Snackbar.LENGTH_LONG).show();
            etFldArea.requestFocus();
            return false;
        }
        if (mssLimits.getSelectedItemsAsString().equalsIgnoreCase("HRW") && Double.parseDouble(etRowWidth.getText().toString()) >= 1.2) {
            Snackbar.make(mLayout,"You have 'Row width < 1.2m' as your limitations but entered row width is >= 1.2", Snackbar.LENGTH_LONG).show();
            etRowWidth.requestFocus();
            return false;
        }
        if(!etVar.getText().toString().equalsIgnoreCase("f") && !etVar.getText().toString().equalsIgnoreCase("n/a")) {
            if (tvHS.getText().equals("Tap to Set")) {
                Snackbar.make(mLayout,"Please set plant/harvest date.", Snackbar.LENGTH_LONG).show();
                tvHS.requestFocus();
                return false;
            }
            if (spnHS.getSelectedItemPosition() == 0 && spnCropCycle.getSelectedItemPosition() == 0) {
                Snackbar.make(mLayout,"Crop cycle required for date planted.", Snackbar.LENGTH_LONG).show();
                tvHS.requestFocus();
                return false;
            }
            if (spnHS.getSelectedItemPosition() == 1 && harvestSched.before(date)) {
                Snackbar.make(mLayout,"Chosen harvest date has passed.", Snackbar.LENGTH_LONG).show();
                tvHS.requestFocus();
                return false;
            }
            if (spnHS.getSelectedItemPosition() == 0 && harvestSched.after(date)) {
                Snackbar.make(mLayout,"Chosen plant date has not yet passed.", Snackbar.LENGTH_LONG).show();
                tvHS.requestFocus();
                return false;
            }
            return true;
        }

        return true;
    }

    private Date incrementDate(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        Date newDate = cal.getTime();
        DateFormat df = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        System.out.println(df.format(newDate));
        return newDate;
    }

    private void save() {
        FieldsRepo repo = new FieldsRepo();
        Fields fields = new Fields();
        Date harvestDate = null;
        int cropCycle;

        if (isValid()) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            fields.setFldId(String.valueOf(fieldId));
            fields.setFldName(etFldName.getText().toString());
            fields.setFldArea(Double.parseDouble(etFldArea.getText().toString()));
            fields.setFldSuit(getCode(spnSuit.getSelectedItem().toString()));
            fields.setFldFarmId(String.valueOf(farmId));

            cropCycle = spnCropCycle.getSelectedItemPosition() + 9;
            if (etVar.getText().toString().equalsIgnoreCase("f") || etVar.getText().toString().equalsIgnoreCase("n/a")) {
                fields.setFldCropCycle(null);
                fields.setFldDatePlanted(null);
                fields.setFldHarvestDate(null);
            } else {
                if (spnHS.getSelectedItemPosition() == 0) {
                    harvestDate = incrementDate(harvestSched, cropCycle);
                    fields.setFldCropCycle(String.valueOf(cropCycle));
                    fields.setFldDatePlanted(df.format(harvestSched));
                    fields.setFldHarvestDate(df.format(harvestDate));
                } else {

                    fields.setFldCropCycle("11*");
                    fields.setFldDatePlanted(null);
                    fields.setFldHarvestDate(df.format(harvestSched));
                }
            }


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
            fields.setFldSurveyor(etFldSvyor.getText().toString());
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        Date date = cal.getTime();
        harvestSched = date;
        DateFormat df = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        tvHS.setTextColor(Color.BLACK);
        tvHS.setText(df.format(date));
    }
    private Date strToDate(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        if (strDate == null) {
            strDate = "0000-00-00";
        }
        try {
            date = format.parse(strDate);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}