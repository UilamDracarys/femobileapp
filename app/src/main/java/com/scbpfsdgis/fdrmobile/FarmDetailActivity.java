package com.scbpfsdgis.fdrmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scbpfsdgis.fdrmobile.data.model.Farms;
import com.scbpfsdgis.fdrmobile.data.repo.FarmsRepo;
import com.scbpfsdgis.fdrmobile.data.repo.PersonRepo;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FarmDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, android.view.View.OnClickListener {

    Spinner spnPltr, spnOvsr;
    EditText etFarmName, etFarmLoc, etFarmCity, etFarmCmt;
    TextView tvSurveyDate;
    private int farmID = 0;
    Date surveyDate = null;
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_detail);
        mLayout = findViewById(R.id.farm_detail_layout);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        initialize();

        Intent intent = getIntent();
        farmID = intent.getIntExtra("farmID", 0);

        FarmsRepo repo = new FarmsRepo();
        Farms farm = repo.getFarmByID(farmID);

        int planterID, ovsrID;
        if (farmID != 0) {
            DateFormat df = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
            etFarmName.setText(farm.getFarmName());
            etFarmLoc.setText(farm.getFarmLoc());
            etFarmCity.setText(farm.getFarmCity());
            surveyDate = strToDate(farm.getFarmSvy());
            tvSurveyDate.setText(df.format(surveyDate));
            tvSurveyDate.setTextColor(Color.BLACK);
            etFarmCmt.setText(farm.getFarmCmt());
            planterID = Integer.parseInt(farm.getFarmPltrID());
            String ovsrIDStr = farm.getFarmOvsrID();
            if (ovsrIDStr.equalsIgnoreCase("N/A")) {
                ovsrID = 0;
            } else {
                ovsrID = Integer.parseInt(ovsrIDStr);
            }
        } else {
            planterID = 0;
            ovsrID = 0;
        }
        loadPlanters(farmID, planterID);
        loadFarmRep(farmID, ovsrID);
    }

    @Override
    public void onBackPressed() {
        showDiscardDialog(farmID);
    }

    private void showDiscardDialog(int farmID) {
        FarmsRepo repo = new FarmsRepo();
        Farms farms = repo.getFarmByID(farmID);
        DateFormat df = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());

        String[] defaults;
        if(farmID == 0) {
            defaults = new String[]{"", "0", "0", "", "", "Date Surveyed", ""};
        } else {
            defaults = new String[]{farms.getFarmName(), farms.getFarmPltrID(), farms.getFarmOvsrID(), farms.getFarmLoc(), farms.getFarmCity(), df.format(strToDate(farms.getFarmSvy())), farms.getFarmCmt()};
        }
        String[] currentInput = {etFarmName.getText().toString(), String.valueOf(spnPltr.getSelectedItemPosition()), String.valueOf(spnOvsr.getSelectedItemPosition()), etFarmLoc.getText().toString(), etFarmCity.getText().toString(),
        tvSurveyDate.getText().toString(), etFarmCmt.getText().toString()};

        int count = 0;

        for (int i=0; i<defaults.length; i++) {
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


    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.tvSvyDate)) {
            showDatePicker();
        }
    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                FarmDetailActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "StartDatePD");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the save_cancel; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_cancel, menu);
        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        Date date = cal.getTime();
        surveyDate = date;
        DateFormat df = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        tvSurveyDate.setTextColor(Color.BLACK);
        tvSurveyDate.setText(df.format(date));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                return true;
            case R.id.action_cancel:
                showDiscardDialog(farmID);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialize() {
        etFarmName = findViewById(R.id.etFarmName);
        spnPltr = findViewById(R.id.spnPlanter);
        spnOvsr = findViewById(R.id.spnOvsr);
        etFarmLoc = findViewById(R.id.etFarmLoc);
        etFarmCity = findViewById(R.id.etFarmCity);
        etFarmCmt = findViewById(R.id.etFarmCmt);
        tvSurveyDate = findViewById(R.id.tvSvyDate);
        tvSurveyDate.setOnClickListener(this);
    }

    private void loadPlanters(int farmID, int pltrID) {

        String cls = "P";
        PersonRepo pltrRepo = new PersonRepo();
        List<String> planterList =  pltrRepo.getPersonNamesList(cls);
        String planterName;
        addDefaultChoice(planterList, cls);

        if(planterList.size()!=0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, planterList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnPltr.setAdapter(adapter);
            if (farmID!=0) {
                planterName = pltrRepo.getPersonByID(pltrID).getPersonName() + " - ID:" + pltrID;
                int spnPost = adapter.getPosition(planterName);
                spnPltr.setSelection(spnPost);
            }
        } else {
            Toast.makeText(this,"No planter records!",Toast.LENGTH_SHORT).show();
        }
    }

    private void addDefaultChoice(List<String> list, String cls) {
        if (cls.equalsIgnoreCase("P")) {
            list.add(0, "- Planter -");
        } else {
            list.add(0, "- Farm Representative -");
        }
    }

    private void loadFarmRep(int farmID, int farmRepID) {
        String cls = "O";
        PersonRepo pltrRepo = new PersonRepo();
        List<String> ovsrList = pltrRepo.getPersonNamesList("O");
        addDefaultChoice(ovsrList, cls);
        String ovsrName;
        if (ovsrList.size() != 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ovsrList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnOvsr.setAdapter(adapter);
            if (farmID != 0) {
                ovsrName = pltrRepo.getPersonByID(farmRepID).getPersonName() + " - ID:" + farmRepID;
                int spnPost = adapter.getPosition(ovsrName);
                spnOvsr.setSelection(spnPost);
            }
        } else {
            Toast.makeText(this, "No overseer records shit!", Toast.LENGTH_SHORT).show();
        }
    }

    private void save() {
        FarmsRepo repo = new FarmsRepo();
        Farms farm = new Farms();

        if (isValid()) {

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String planter = spnPltr.getSelectedItem().toString();
            int ovsrIdx = spnOvsr.getSelectedItemPosition();
            String ovsr = spnOvsr.getSelectedItem().toString();
            farm.setFarmID(farmID);
            farm.setFarmName(etFarmName.getText().toString());
            farm.setFarmPltrID(planter.substring(planter.lastIndexOf("ID:") + 3));
            if (ovsrIdx == 0) {
                farm.setFarmOvsrID("N/A");
            } else {
                farm.setFarmOvsrID(ovsr.substring(ovsr.lastIndexOf("ID:") + 3));
            }
            farm.setFarmLoc(etFarmLoc.getText().toString());
            farm.setFarmCity(etFarmCity.getText().toString());
            farm.setFarmSvy(df.format(surveyDate));
            farm.setFarmCmt(etFarmCmt.getText().toString());

            if (farmID == 0) {
                if (repo.isFarmExisting(farm.getFarmName())) {
                    Toast.makeText(this, "Farm " + farm.getFarmName() + " already exists.", Toast.LENGTH_SHORT).show();
                    return;
                }
                farmID = farm.getFarmID();
                repo.insert(farm);
                Toast.makeText(this, "New Farm Added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                repo.update(farm);
                Toast.makeText(this, "Farm Record updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean isValid() {
        String farmName = etFarmName.getText().toString();
        Date date = new Date();

        if (farmName.equals("")) {
            Snackbar.make(mLayout,"Farm Name required.", Snackbar.LENGTH_SHORT).show();
            etFarmName.requestFocus();
            return false;
        }

        if (spnPltr.getSelectedItemPosition() == 0) {
            Snackbar.make(mLayout,"Select planter.", Snackbar.LENGTH_SHORT).show();
            spnPltr.requestFocus();
            return false;
        }

        if (surveyDate == null) {
            Snackbar.make(mLayout,"Select survey date.", Snackbar.LENGTH_SHORT).show();
            tvSurveyDate.requestFocus();
            return false;
        }

        if (surveyDate.after(date)) {
            Snackbar.make(mLayout,"Invalid survey date.", Snackbar.LENGTH_SHORT).show();
            tvSurveyDate.requestFocus();
            return false;
        }

        return true;
    }

    private Date strToDate(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = format.parse(strDate);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}