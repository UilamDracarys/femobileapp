package com.scbpfsdgis.femobilebetav20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.guna.libmultispinner.MultiSelectionSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by William on 1/27/2018.
 */

public class FieldDetailActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {

    Spinner spnMechMeth, spnTract;
    String limits = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);

        String[] limits = getResources().getStringArray(R.array.limitations);
        List<String> limitList = new ArrayList<String>(Arrays.asList(limits));

        MultiSelectionSpinner multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.spnMainLim);
        multiSelectionSpinner.setItems(limitList);
        multiSelectionSpinner.setListener(this);

        spnMechMeth = findViewById(R.id.spnMechMeth);
        spnTract = findViewById(R.id.spnTract);

        System.out.println("Selected Strings: " + multiSelectionSpinner.getSelectedItemsAsString());

    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        Toast.makeText(this, strings.toString(), Toast.LENGTH_LONG).show();
        System.out.println("Toast " + strings.toString());
    }
}
