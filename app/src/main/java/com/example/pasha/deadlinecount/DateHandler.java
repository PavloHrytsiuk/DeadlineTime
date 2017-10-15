package com.example.pasha.deadlinecount;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

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

    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    int mHour;
    int mMinute;

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

    public void dateTimePicker(final Context context) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        Log.d(TAG, "date_time: " + date_time);
                        timePicker(context);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void timePicker(Context context) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        Log.d(TAG, "mHour/mMinute: " + mHour + " : " + mMinute);

                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }
}
