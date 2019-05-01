package com.codepolitan.s4cataoguemovie.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static String PREF_NAME = "PrefManager";
    private static String IS_ACTIVE_REMINDER = "isActiveReminder";
    private static String IS_ACTIVE_UPCOMING = "isActiveUpcoming";

    private static String LANGUAGE = "language";
    private static String IS_JOB_SCHEDULER = "isJobScheduler";
    private static String IS_GCM = "isGcm";
    private static String FIRST = "first";

    private static final int PRIVATE_MODE = 0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public int getFirst() {
        return sharedPreferences.getInt(FIRST, 0);
    }

    public void setFirst(int first) {
        editor.putInt(FIRST, first);
        editor.apply();
    }

    public boolean getIsJobScheduler() {
        return sharedPreferences.getBoolean(IS_JOB_SCHEDULER, true);
    }

    public void setIsJobScheduler(boolean isJobScheduler) {
        editor.putBoolean(IS_JOB_SCHEDULER, isJobScheduler);
        editor.apply();
    }

    public boolean getIsGcm() {
        return sharedPreferences.getBoolean(IS_GCM, false);
    }

    public void setIsGcm(boolean isGcm) {
        editor.putBoolean(IS_GCM, isGcm);
        editor.apply();
    }

    public String getLANGUAGE() {
        return sharedPreferences.getString(LANGUAGE, "en");
    }

    public void setLANGUAGE(String language) {
        editor.putString(LANGUAGE, language);
        editor.apply();
    }

    public boolean getIsActiveUpcoming() {
        return sharedPreferences.getBoolean(IS_ACTIVE_UPCOMING, true);
    }

    public void setIsActiveUpcoming(boolean isActiveUpcoming) {
        editor.putBoolean(IS_ACTIVE_UPCOMING, isActiveUpcoming);
        editor.apply();
    }

    public boolean getIsActiveReminder() {
        return sharedPreferences.getBoolean(IS_ACTIVE_REMINDER, true);
    }

    public void setIsActiveReminder(boolean isActiveReminder) {
        editor.putBoolean(IS_ACTIVE_REMINDER, isActiveReminder);
        editor.apply();
    }

    public void clear(){
        editor.clear();
        editor.apply();
    }
}
