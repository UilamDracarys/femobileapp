package com.scbpfsdgis.fdrmobile;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.File;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        System.out.println("Path: " + exportDir.getAbsolutePath());
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