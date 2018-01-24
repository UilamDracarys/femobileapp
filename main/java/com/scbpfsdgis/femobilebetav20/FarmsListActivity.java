package com.scbpfsdgis.femobilebetav20;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.scbpfsdgis.femobilebetav20.data.repo.FarmsRepo;
import com.scbpfsdgis.femobilebetav20.data.repo.PersonRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FarmsListActivity extends ListActivity implements android.view.View.OnClickListener {

    Button btnAdd;
    TextView tvFarmID;


    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.btnAddFarm)){
            Intent intent = new Intent(this,FarmDetailActivity.class);
            intent.putExtra("farmID",0);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farms_list);

        btnAdd = (Button) findViewById(R.id.btnAddFarm);
        btnAdd.setOnClickListener(this);

        listAll();
    }

    private void listAll() {
        FarmsRepo repo = new FarmsRepo();
        ArrayList<HashMap<String, String>> farmsList =  repo.getFarmsList();
        if(farmsList.size()!=0) {
            ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tvFarmID = view.findViewById(R.id.farmID);
                    String farmID = tvFarmID.getText().toString();
                    Intent objIndent = new Intent(getApplicationContext(),FarmDetailActivity.class);
                    objIndent.putExtra("farmID", Integer.parseInt(farmID));
                    startActivity(objIndent);
                }
            });
            ListAdapter adapter = new SimpleAdapter( FarmsListActivity.this,farmsList, R.layout.view_farm_list_item, new String[] { "id","farmName", "planterName"}, new int[] {R.id.farmID, R.id.farmName, R.id.farmOwner});
            setListAdapter(adapter);
        }else{
            Toast.makeText(this,"No farm records!",Toast.LENGTH_SHORT).show();
        }
    }

}
