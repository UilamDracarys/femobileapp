package com.scbpfsdgis.fdrmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.scbpfsdgis.fdrmobile.data.model.Person;
import com.scbpfsdgis.fdrmobile.data.repo.PersonRepo;

import java.util.ArrayList;
import java.util.List;

public class PersonDetailActivity extends AppCompatActivity {

    Button btnSavePrsn, btnClose;
    EditText etPersonName;
    EditText etPersonCont;
    Spinner spnClass;
    private int personID = 0;
    List<String> personCls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        Toolbar myToolbar =  findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        etPersonName =  findViewById(R.id.etPersonName);
        etPersonCont =  findViewById(R.id.etPersonCont);

        spnClass = findViewById(R.id.spnPersonCls);

        personCls = new ArrayList<String>();

        personCls.add("Planter");
        personCls.add("Overseer");
        personCls.add("Planter/Overseer");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, personCls);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClass.setAdapter(adapter);

        personID = 0;
        Intent intent = getIntent();
        personID = intent.getIntExtra("personID", 0);

        Person person;
        PersonRepo repo = new PersonRepo();
        person = repo.getPersonByID(personID);

        if(personID!=0) {
            String prsnCls = person.getPersonCls();
            switch (prsnCls) {
                case "P":
                    spnClass.setSelection(0);
                    break;
                case "O":
                    spnClass.setSelection(1);
                    break;
                default:
                    spnClass.setSelection(2);
                    break;
            }
        }

        etPersonName.setText(person.getPersonName());
        etPersonCont.setText(person.getPersonCont());
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

    private void save() {
        PersonRepo repo = new PersonRepo();
        Person planter = new Person();
        planter.setPersonCont(etPersonCont.getText().toString());
        planter.setPersonName(etPersonName.getText().toString());
        planter.setPersonID(personID);

        if (isValid()) {
            int personClsInt = spnClass.getSelectedItemPosition();
            String personCls;
            switch (personClsInt) {
                case 1:
                    personCls = "O";
                    break;
                case 2:
                    personCls = "PO";
                    break;
                default:
                    personCls = "P";
            }
            planter.setPersonCls(personCls);

            if (personID == 0) {

                if (repo.isPersonExisting(planter.getPersonName())) {
                    Toast.makeText(this,planter.getPersonName() + " already exists.",Toast.LENGTH_SHORT).show();
                    return;
                }
                personID = planter.getPersonID();
                repo.insert(planter, personCls);
                Toast.makeText(this, "New Person Added", Toast.LENGTH_SHORT).show();
                finish();
            } else {

                repo.update(planter);
                Toast.makeText(this, "Record updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean isValid() {
        String personName = etPersonName.getText().toString();
        String personCont = etPersonCont.getText().toString();
        PersonRepo repo = new PersonRepo();

        if (personName.equalsIgnoreCase("") && personCont.equalsIgnoreCase("")) {
            Toast.makeText(this,"Nothing to save.",Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }

        return true;
    }
}
