package com.example.pasha.deadlinecount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.hoursView)
    TextView hoursView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Calendar dateNow = new GregorianCalendar();
        long timeNow = dateNow.getTimeInMillis();
        Calendar dateDeadline = new GregorianCalendar(2018, 0, 1, 0, 0, 0);
        long timeDeadline = dateDeadline.getTimeInMillis();
        Long hours = ((timeDeadline - timeNow) / (3600 * 1000));

        hoursView.setText(hours.toString());
    }
}
