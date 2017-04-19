package com.fi.botapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.UUID;

public class SessionIdStorage {
    private static final String PREF_NAME = "APIAI_preferences";
    private static final String SESSION_ID = "sessionId";

    public SessionIdStorage() {
    }

    public static synchronized String getSessionId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("APIAI_preferences", 0);
        String sessionId = sharedPreferences.getString("sessionId", "");
        if(!TextUtils.isEmpty(sessionId)) {
            return sessionId;
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String value = UUID.randomUUID().toString();
            editor.putString("sessionId", value);
            editor.commit();
            return value;
        }
    }

    public static synchronized void resetSessionId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("APIAI_preferences", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sessionId", "");
        editor.commit();
    }
}
