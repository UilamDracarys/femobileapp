package com.scbpfsdgis.femobilebetav20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.scbpfsdgis.femobilebetav20.data.model.Fields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by William on 1/27/2018.
 */

public class FieldDetailActivity extends AppCompatActivity {

    Spinner spnSuit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);

        spnSuit = findViewById(R.id.spnSuit);
        String[] suit = getResources().getStringArray(R.array.suitability);

        List<String> suitList = new ArrayList<String>(Arrays.asList(suit));
        suitList.add(0, "-Choose Suitability-");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, suitList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnSuit.setAdapter(adapter);
    }
}
