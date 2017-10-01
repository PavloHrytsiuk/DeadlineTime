package com.example.pasha.deadlinecount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.daysView)
    TextView daysView;
    @BindView(R.id.hoursView)
    TextView hoursView;
    @BindView(R.id.minutesView)
    TextView minutesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Calendar dateNow = new GregorianCalendar();
        long timeNow = dateNow.getTimeInMillis();
        Calendar dateDeadline = new GregorianCalendar(2018, 0, 1, 0, 0, 0);
        long timeDeadline = dateDeadline.getTimeInMillis();
        Long days = ((timeDeadline - timeNow) / (3600 * 1000 * 24));
        Long hours = ((timeDeadline - timeNow) / (3600 * 1000));
        Long minutes = ((timeDeadline - timeNow) / (60 * 1000));

        daysView.setText(String.valueOf(days));
        hoursView.setText(String.valueOf(hours));
        minutesView.setText(String.valueOf(minutes));
    }
}
