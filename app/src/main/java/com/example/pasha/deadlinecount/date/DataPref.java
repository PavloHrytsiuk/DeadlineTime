package com.example.pasha.deadlinecount.date;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

public final class DataPref {
    private SharedPreferences preferences;
    private final Context context;

    public DataPref(Context context) {
        this.context = context;
    }


    public void saveLongData(@NonNull final Long data, @NonNull final String name) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor shEd = preferences.edit();
        shEd.putLong(name, data);
        shEd.apply();
    }

    public Long loadLongData(@NonNull final String name) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(name, 0);
    }

    public void saveStringData(@NonNull final String data, @NonNull final String name) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor shEd = preferences.edit();
        shEd.putString(name, data);
        shEd.apply();
    }

    public String loadStringData(@NonNull final String name) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(name, "");
    }

    public void deleteStringData(@NonNull final String name) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().remove(name).apply();
    }
}
