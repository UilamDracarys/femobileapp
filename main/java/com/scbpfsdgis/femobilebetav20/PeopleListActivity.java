package com.scbpfsdgis.femobilebetav20;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.scbpfsdgis.femobilebetav20.data.repo.PersonRepo;

import java.util.ArrayList;
import java.util.HashMap;

public class PeopleListActivity extends AppCompatActivity{

    TextView tvPersonID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_list);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        listAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the save_cancel; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, PersonDetailActivity.class);
                intent.putExtra("personID",0);
                startActivity(intent);
                return true;

            case R.id.action_back:
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
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