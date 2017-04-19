package com.fi.botapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.UUID;

public class PrefsHelper {
    private static PrefsHelper instance;

    private SharedPreferences sharedPrefs;
    private String BLANK = "";
    private String SESSION_ID_KEY = "session_id";

    public PrefsHelper(final Context context) {
        this.sharedPrefs = context.getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE);
    }

    public static void init(final Context context) {
        if (context != null) {
            instance = new PrefsHelper(context);
        }
    }

    public static PrefsHelper getInstance() {
        if (instance == null) {
            Logger.D("Prefs is null, call init.");
        }
        return instance;
    }

    public void putBooleanPref(final String key, final boolean value) {
        sharedPrefs.edit().putBoolean(key, value).apply();
    }

    public void putStringPref(final String key, final String value) {
        sharedPrefs.edit().putString(key, value).apply();
    }

    public void putIntPref(final String key, final int value) {
        sharedPrefs.edit().putInt(key, value).apply();
    }

    public void putDouble(final String key, final double value) {
        sharedPrefs.edit().putLong(key, Double.doubleToRawLongBits(value));
    }

    public boolean getBooleanPref(final String key, final boolean defaultValue) {
        return sharedPrefs.getBoolean(key, defaultValue);
    }

    public String getStringPref(final String key, final String defaultValue) {
        return sharedPrefs.getString(key, defaultValue);
    }

    public int getIntPref(final String key, final int defaultValue) {
        return sharedPrefs.getInt(key, defaultValue);
    }

    private void removePref(final String key) {
        sharedPrefs.edit().remove(key).apply();
    }

    public double getDoublePref(final String key, final double defaultValue) {
        return Double.longBitsToDouble(sharedPrefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    public String getSessionId() {
        String sessionId = sharedPrefs.getString(SESSION_ID_KEY, BLANK);
        if(!TextUtils.isEmpty(sessionId)) {
            return sessionId;
        } else {
            String value = UUID.randomUUID().toString();
            sharedPrefs.edit().putString(SESSION_ID_KEY, value).commit();
            return value;
        }
    }

    public void resetSessionId() {
        sharedPrefs.edit().putString(SESSION_ID_KEY, BLANK).commit();
    }
}
