package com.scbpfsdgis.femobilebetav20.app;

import android.app.Application;
import android.content.Context;

import com.scbpfsdgis.femobilebetav20.data.DBHelper;
import com.scbpfsdgis.femobilebetav20.data.DatabaseManager;

/**
 * Created by William on 1/7/2018.
 */

public class App extends Application {
    private static Context context;
    private static DBHelper dbHelper;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this.getApplicationContext();
        dbHelper = new DBHelper();
        DatabaseManager.initializeInstance(dbHelper);

    }


    public static Context getContext(){
        return context;
    }
}
