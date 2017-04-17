package com.fi.botapp.utils;

import android.util.Log;

public class Logger {

    private static final String LOG_TAG = "DEBUG_LOG";

    public static void D(String message) {
        if (Constants.LOG_ENABLED) {
            Log.d(LOG_TAG, message);
        }
    }

    public static void D(String tag, String message) {
        if (Constants.LOG_ENABLED) {
            Log.d(LOG_TAG + "_" + tag, message);
        }
    }
}
