package com.example.pasha.deadlinecount;

import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateHandler {
    private static final String TAG = "TAG";
    private Calendar dateNow;
    private Calendar dateDeadline;
    private Calendar dateStart;
    private long timeSaved;
    private long timeNow;
    private long timeDeadline;
    private long timeStart;

    public DateHandler() {
        dateNow = new GregorianCalendar();
        dateDeadline = new GregorianCalendar(2018, 0, 1, 0, 0, 0);
        dateStart = new GregorianCalendar(2017, 8, 23, 0, 0, 0);
        timeNow = dateNow.getTimeInMillis();
        timeDeadline = dateDeadline.getTimeInMillis();
        timeStart = dateStart.getTimeInMillis();
        Log.d(TAG, "timeNow: " + timeNow / 1000);
        Log.d(TAG, "timeDeadline: " + timeDeadline / 1000);
    }

    public int gerMinutes() {
        return (int) ((timeDeadline - timeNow) / (60 * 1000));
    }

    public int getHours() {
        return (int) ((timeDeadline - timeNow) / (3600 * 1000));
    }

    public int getDays() {
        return (int) ((timeDeadline - timeNow) / (3600 * 1000 * 24));
    }

    public void setDateSave(long time) {
        timeSaved = time;
    }

    public StringBuilder getSpendTime() {
        if (timeSaved != 0) {
            long hourSpend = ((timeNow - timeSaved) / (1000 * 3600));
            long minutesSpend = (((timeNow - timeSaved) / (60 * 1000)) % 60);
            long secondSpend = (((timeNow - timeSaved) / (1000))) % 60;
            StringBuilder show = new StringBuilder();
            show.append(hourSpend);
            show.append("h : ");
            show.append(minutesSpend);
            show.append("m : ");
            show.append(secondSpend);
            show.append("s");
            return show;
        } else return null;
    }

    public StringBuilder getTimeInGeneral() {
        if (timeSaved != 0) {
            long daySpend = ((timeDeadline - timeNow) / (3600 * 1000 * 24));
            long hourSpend = (((timeDeadline - timeNow) / (1000 * 3600)) % 24);
            long minutesSpend = (((timeDeadline - timeNow) / (60 * 1000)) % 60);
            long secondSpend = (((timeDeadline - timeNow) / (1000))) % 60;
            StringBuilder show = new StringBuilder();
            show.append(daySpend);
            show.append("d : ");
            show.append(hourSpend);
            show.append("h : ");
            show.append(minutesSpend);
            show.append("m : ");
            show.append(secondSpend);
            show.append("s");
            return show;
        } else return null;
    }

    public double getProgress() {
        Log.d(TAG, "progress %: " + (timeDeadline - timeNow) * 100.0 / (timeDeadline - timeStart));
        return 100.0 - ((timeDeadline - timeNow) * 100.0 / (timeDeadline - timeStart));
    }
}
