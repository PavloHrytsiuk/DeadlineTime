package com.example.pasha.deadlinecount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    @BindView(R.id.generalDateView)
    TextView generalDateView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressTextView)
    TextView progressTextView;
    @BindView(R.id.spendTimeView)
    TextView spentTime;

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

        if (dateHandler.getTimeInGeneral() != null) {
            generalDateView.setText(dateHandler.getTimeInGeneral());
        } else savedData.setText(R.string.error_load_date);

        daysView.setText(String.valueOf(dateHandler.getDays()));
        hoursView.setText(String.valueOf(dateHandler.getHours()));
        minutesView.setText(String.valueOf(dateHandler.gerMinutes()));

        spentTime.setText(dateHandler.getSpentTime());

        if (dateHandler.getSpentTimeFromLastVisit() != null) {
            savedData.setText(dateHandler.getSpentTimeFromLastVisit());
        } else savedData.setText(R.string.error_load_date);

        progressBar.setProgress((int) dateHandler.getProgress());
        String progress = String.format("%.2f", dateHandler.getProgress()) + " %";
        progressTextView.setText(progress);

        dataPref.saveData(new GregorianCalendar().getTimeInMillis());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actions_menu_set_deadline:
                dateHandler.dateTimePicker(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
