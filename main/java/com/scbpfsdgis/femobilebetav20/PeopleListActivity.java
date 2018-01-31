package com.scbpfsdgis.femobilebetav20;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.scbpfsdgis.femobilebetav20.data.DBHelper;
import com.scbpfsdgis.femobilebetav20.data.repo.PersonRepo;

import java.util.ArrayList;
import java.util.HashMap;

public class PeopleListActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAdd;
    TextView tvPersonID;


    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.btnAddPerson)){
            Intent intent = new Intent(this, PersonDetailActivity.class);
            intent.putExtra("personID",0);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_list);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        btnAdd = findViewById(R.id.btnAddPerson);
        btnAdd.setOnClickListener(this);

        listAll();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        listAll();
    }

    public void listAll() {
        PersonRepo repo = new PersonRepo();
        ArrayList<HashMap<String, String>> ownerList =  repo.getPersonList();
        if(ownerList.size()!=0) {
            ListView lv = findViewById(R.id.lstPeople);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tvPersonID = view.findViewById(R.id.personID);
                    String planterID = tvPersonID.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(),PersonDetailActivity.class);
                    objIndent.putExtra("personID", Integer.parseInt( planterID));
                    startActivity(objIndent);
                }
            });
            ListAdapter adapter = new SimpleAdapter( PeopleListActivity.this,ownerList, R.layout.view_people_list_item, new String[] { "id","name", "contact"}, new int[] {R.id.personID, R.id.personName, R.id.personCont});
            lv.setAdapter(adapter);
        }else{
            Toast.makeText(this,"No records!",Toast.LENGTH_SHORT).show();
        }
    }
}