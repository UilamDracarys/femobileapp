package com.scbpfsdgis.fdrmobile.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.scbpfsdgis.fdrmobile.data.DBHelper;
import com.scbpfsdgis.fdrmobile.data.DatabaseManager;
import com.scbpfsdgis.fdrmobile.data.repo.UpdateHelper;

import java.util.HashMap;
import java.util.Map;

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

        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        Map<String, Object> defaultValue = new HashMap<>();
        defaultValue.put(UpdateHelper.KEY_UPDATE_ENABLE, false);
        defaultValue.put(UpdateHelper.KEY_UPDATE_VERSION, "1.0");
        defaultValue.put(UpdateHelper.KEY_UPDATE_URL, "https://www.dropbox.com/home/FDRMobile/FDRMobile.apk");

        remoteConfig.setDefaults(defaultValue);
        remoteConfig.fetch(60)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    remoteConfig.activateFetched();
                }
            }
        });


    }


    public static Context getContext(){
        return context;
    }
}
