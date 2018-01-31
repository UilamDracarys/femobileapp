package com.scbpfsdgis.femobilebetav20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.scbpfsdgis.femobilebetav20.data.model.Person;
import com.scbpfsdgis.femobilebetav20.data.repo.PersonRepo;

import java.util.ArrayList;
import java.util.List;

public class PersonDetailActivity extends AppCompatActivity implements View.OnClickListener{

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

        btnSavePrsn =  findViewById(R.id.btnSavePerson);
        btnClose =  findViewById(R.id.btnCancel);

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

        btnSavePrsn.setOnClickListener(this);
        btnClose.setOnClickListener(this);

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


    public void onClick(View view) {
        if (view == findViewById(R.id.btnSavePerson)){
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
        } else if (view == findViewById(R.id.btnCancel)) {
            System.out.println("FUCK");
            finish();
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

        if (repo.isPersonExisting(personName)) {
            Toast.makeText(this,personName + " already exists.",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
