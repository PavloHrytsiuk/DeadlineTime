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
    private static final String DEADLINE_PREF = "Deadline data";
    private static final String START_PREF = "Start data";

    private final Calendar dateNow;
    private long timeSaved;
    private final long timeNow;
    private long timeDeadline;
    private long timeStart;
    private DataPref dataPref;
    private DateActivity view;
    private final String name;
    private DeadlineCallbacks callbacks;

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
    }

    public int getMinutes() {
        if (timeDeadline < timeNow) return 0;
        else return (int) ((timeDeadline - timeNow) / (60 * 1000));
    }

    public int getHours() {
        if (timeDeadline < timeNow) return 0;
        else return (int) ((timeDeadline - timeNow) / (3600 * 1000));
    }

    public int getDays() {
        if (timeDeadline < timeNow) return 0;
        else return (int) ((timeDeadline - timeNow) / (3600 * 1000 * 24));
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
        StringBuilder show = new StringBuilder();
        if (timeDeadline < timeStart) {
            show.append("You set passed date");
            return show;
        }
        long daySpend = ((timeNow - timeStart) / (3600 * 1000 * 24));
        long hourSpend = ((timeNow - timeStart) / (1000 * 3600) % 24);
        long minutesSpend = (((timeNow - timeStart) / (60 * 1000)) % 60);
        long secondSpend = (((timeNow - timeStart) / (1000))) % 60;
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
        StringBuilder show = new StringBuilder();
        long daySpend = ((timeDeadline - timeNow) / (3600 * 1000 * 24));
        long hourSpend = (((timeDeadline - timeNow) / (1000 * 3600)) % 24);
        long minutesSpend = (((timeDeadline - timeNow) / (60 * 1000)) % 60);
        long secondSpend = (((timeDeadline - timeNow) / (1000))) % 60;
        if (timeDeadline < timeNow) {
            show.append("Time is exceeded by:" + "\n");
            show.append(Math.abs(daySpend));
            show.append("d : ");
            show.append(Math.abs(hourSpend));
            show.append("h : ");
            show.append(Math.abs(minutesSpend));
            show.append("m : ");
            show.append(Math.abs(secondSpend));
            show.append("s");
            return show;
        }
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
        if (timeDeadline < timeNow) {
            return 100.0;
        }
        return 100.0 - ((timeDeadline - timeNow) * 100.0 / (timeDeadline - timeStart));
    }

    public void setCallbacks(@NonNull final DeadlineCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    private void loadNewDeadlineDate(@NonNull final GregorianCalendar calendar) {
        timeDeadline = calendar.getTimeInMillis();
        timeSaved = timeNow;
        dataPref.saveLongData(timeDeadline, DEADLINE_PREF + name);
        if (view != null) {
            view.setDate();
        } else {
            dataPref.saveLongData(timeNow, START_PREF + name);
            callbacks.onCreateDateActivity();
        }
    }

    public void dateTimePicker(@NonNull final Context context) {

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
                    public void onDateSet(@NonNull final DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(@NonNull final TimePicker view, final int hourOfDay, final int minute) {

                                        calendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minute, 0);
                                        loadNewDeadlineDate(calendar);
                                    }
                                }, mHour, mMinute, true);
                        timePickerDialog.show();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void attachView(@NonNull final DateActivity view) {
        this.view = view;
    }

    public void deleteAllData() {
        dataPref.deleteStringData(SAVE_PREF + name);
        dataPref.deleteStringData(DEADLINE_PREF + name);
        dataPref.deleteStringData(START_PREF + name);
    }
}
