package com.fi.botapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.fi.botapp.network.VolleySingleton;

public class BotApp extends Application {

    private String TAG = BotApp.class.getSimpleName();

    private static Context appContext;
    private static BotApp botAppInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "******** run ********");
        botAppInstance = this;
        initSingletons();
    }

    private void initSingletons() {
        VolleySingleton.getInstance();
        VolleySingleton.nuke();
    }

    public Context getAppContext() {
        return botAppInstance.getApplicationContext();
    }

    public static BotApp getInstance() {
        return botAppInstance;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
