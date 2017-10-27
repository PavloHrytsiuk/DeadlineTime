package com.example.pasha.deadlinecount.date;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.pasha.deadlinecount.main.DeadlineCallbacks;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateHandler {

    static final String SAVE_PREF = "Save data";
    static final String DEADLINE_PREF = "Deadline data";
    static final String START_PREF = "Start data";

    private static final String TAG = "TAG";
    private final Calendar dateNow;
    private long timeSaved;
    private final long timeNow;
    private long timeDeadline;
    private long timeStart;
    private DataPref dataPref;
    private DateActivity view;
    private final String name;
    DeadlineCallbacks callbacks;

    public DateHandler(@NonNull final DataPref dataPref, @NonNull final String name) {
        this.dataPref = dataPref;
        this.name = name;

        dateNow = new GregorianCalendar();
        timeNow = dateNow.getTimeInMillis();

        if ((timeSaved = dataPref.loadLongData(SAVE_PREF + name)) == 0) {
            timeSaved = timeNow;
        }
        if ((timeDeadline = dataPref.loadLongData(DEADLINE_PREF + name)) == 0) {
            timeDeadline = timeNow;
        }
        if ((timeStart = dataPref.loadLongData(START_PREF + name)) == 0) {
            timeStart = timeNow;
        }
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

    public StringBuilder getSpentTimeFromLastVisit() {
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

    public StringBuilder getSpentTime() {
        long daySpend = ((timeNow - timeStart) / (3600 * 1000 * 24));
        long hourSpend = ((timeNow - timeStart) / (1000 * 3600) % 24);
        long minutesSpend = (((timeNow - timeStart) / (60 * 1000)) % 60);
        long secondSpend = (((timeNow - timeStart) / (1000))) % 60;
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
    }

    public StringBuilder getTimeInGeneral() {
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
    }

    public double getProgress() {
        Log.d(TAG, "progress %: " + (timeDeadline - timeNow) * 100.0 / (timeDeadline - timeStart));
        return 100.0 - ((timeDeadline - timeNow) * 100.0 / (timeDeadline - timeStart));
    }

    public void setCallbacks(DeadlineCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public void loadNewDeadlineDate(GregorianCalendar calendar) {
        timeDeadline = calendar.getTimeInMillis();
        timeStart = timeNow;
        timeSaved = timeNow;
        dataPref.saveLongData(timeDeadline, DEADLINE_PREF + name);
        dataPref.saveLongData(timeNow, START_PREF + name);
        if (view != null) {
            view.setDate();
        } else {
            callbacks.onCreateDateActivity();
        }
        Log.d(TAG, "General time: " + calendar.getTime());
    }

    public void dateTimePicker(final Context context) {

        // Get Current Date
        final int mYear = dateNow.get(Calendar.YEAR);
        final int mMonth = dateNow.get(Calendar.MONTH);
        final int mDay = dateNow.get(Calendar.DAY_OF_MONTH);
        final int mHour = dateNow.get(Calendar.HOUR_OF_DAY);
        final int mMinute = dateNow.get(Calendar.MINUTE);
        final GregorianCalendar calendar = new GregorianCalendar();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {

                        Log.d(TAG, "date_time: " + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, final int hourOfDay, final int minute) {

                                        calendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minute, 0);
                                        loadNewDeadlineDate(calendar);
                                        Log.d(TAG, "mHour/mMinute: " + mHour + " : " + mMinute);
                                    }
                                }, mHour, mMinute, true);
                        timePickerDialog.show();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void attachView(final DateActivity view) {
        this.view = view;
    }

    public void deleteAllData() {
        dataPref.deleteStringData(SAVE_PREF + name);
        dataPref.deleteStringData(DEADLINE_PREF + name);
        dataPref.deleteStringData(START_PREF + name);
    }
}
