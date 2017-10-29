package com.example.pasha.deadlinecount.date;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

public class DataPref {
    private SharedPreferences preferences;
    private Context context;

    public DataPref(Context context) {
        this.context = context;
    }


    public void saveLongData(@NonNull Long data, @NonNull String name) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor shEd = preferences.edit();
        shEd.putLong(name, data);
        shEd.apply();
    }

    public Long loadLongData(@NonNull String name) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(name, 0);
    }

    public void saveStringData(@NonNull String data, @NonNull String name) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor shEd = preferences.edit();
        shEd.putString(name, data);
        shEd.apply();
    }

    public String loadStringData(@NonNull String name) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(name, "");
    }

    public void deleteStringData(@NonNull String name) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().remove(name).apply();
    }
}
