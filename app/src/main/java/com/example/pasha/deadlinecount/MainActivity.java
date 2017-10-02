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
    @BindView(R.id.saveDateView)
    TextView savedData;

    DataPref dataPref;
    DateHandler dateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dataPref = new DataPref(this);
        dateHandler = new DateHandler();
        dateHandler.setDateSave(dataPref.loadData());

        daysView.setText(String.valueOf(dateHandler.getDays()));
        hoursView.setText(String.valueOf(dateHandler.getHours()));
        minutesView.setText(String.valueOf(dateHandler.gerMinutes()));

        if (dateHandler.getSpendTime() != null) {
            savedData.setText(dateHandler.getSpendTime());
        } else savedData.setText(R.string.error_load_date);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataPref.saveData(new GregorianCalendar().getTimeInMillis());
    }
}
