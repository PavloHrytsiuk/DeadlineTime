package com.example.pasha.deadlinecount;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DataPref {
    private static final String SAVE_PREF = "Save data";
    private SharedPreferences preferences;
    private Context context;

    public DataPref(Context context) {
        this.context = context;
    }


    public void saveData(Long data) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor shEd = preferences.edit();
        shEd.putLong(SAVE_PREF, data);
        shEd.apply();
    }

    public Long loadData() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(SAVE_PREF, 0);
    }
}
