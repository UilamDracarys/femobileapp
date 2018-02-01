package com.scbpfsdgis.femobilebetav20;

import android.content.Intent;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.scbpfsdgis.femobilebetav20.data.DBHelper;
import com.scbpfsdgis.femobilebetav20.data.DatabaseManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

    }


    public void showFarms(View view) {
        Intent intent = new Intent(this, FarmsListActivity.class);
        intent.putExtra("action", "Farm Details");
        startActivity(intent);
    }

    public void showFieldManager(View view) {
        Intent intent = new Intent(this, FarmsListActivity.class);
        intent.putExtra("action", "Manage Fields");
        startActivity(intent);
    }

    public void showPeople(View view) {
        Intent intent = new Intent(this, PeopleListActivity.class);
        startActivity(intent);
    }
}
