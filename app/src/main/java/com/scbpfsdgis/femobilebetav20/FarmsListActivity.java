package com.scbpfsdgis.femobilebetav20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.scbpfsdgis.femobilebetav20.data.model.Farms;
import com.scbpfsdgis.femobilebetav20.data.repo.FarmsRepo;

import java.util.ArrayList;
import java.util.HashMap;

public class FarmsListActivity extends AppCompatActivity {

    TextView tvFarmID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farms_list);

        Toolbar myToolbar =  findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        String action = intent.getStringExtra("action");
        if (action.equals("Manage Fields")) {
            getSupportActionBar().setTitle("Select Farm");
        }

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
                Intent intent = new Intent(this,FarmDetailActivity.class);
                intent.putExtra("farmID",0);
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


    private void listAll() {
        final FarmsRepo repo = new FarmsRepo();
        ArrayList<HashMap<String, String>> farmsList =  repo.getFarmsList();
        if(farmsList.size()!=0) {
            ListView lv = findViewById(R.id.list);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = getIntent();
                    Bundle b = intent.getExtras();

                    if (b!=null) {
                        String action = b.get("action").toString();
                        tvFarmID = view.findViewById(R.id.farmID);
                        String farmID = tvFarmID.getText().toString();
                        Farms farm = repo.getFarmByID(Integer.parseInt(farmID));
                        String farmName = farm.getFarmName();
                        Intent objIndent;
                        if (action.equalsIgnoreCase("Farm Details")) {
                            objIndent = new Intent(getApplicationContext(), FarmDetailActivity.class);
                            objIndent.putExtra("farmID", Integer.parseInt(farmID));
                        } else {
                            objIndent = new Intent(getApplicationContext(), FieldsListActivity.class);
                            objIndent.putExtra("farmID", Integer.parseInt(farmID));
                            objIndent.putExtra("farmName", farmName);
                        }
                        startActivity(objIndent);
                    }
                }
            });
            ListAdapter adapter = new SimpleAdapter( FarmsListActivity.this,farmsList, R.layout.view_farm_list_item, new String[] { "id","farmName", "planterName"}, new int[] {R.id.farmID, R.id.farmName, R.id.farmOwner});
            lv.setAdapter(adapter);
        }else{
            Toast.makeText(this,"No farm records!",Toast.LENGTH_SHORT).show();
        }
    }

}
