package com.example.pasha.deadlinecount.date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasha.deadlinecount.R;
import com.example.pasha.deadlinecount.main.MainActivity;

import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DateActivity extends AppCompatActivity {

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
    @BindView(R.id.textView)
    TextView textView;

    private static final String NAME_DEADLINE_COUNTER = "Name of deadline counter";

    private DataPref dataPref;
    private DateHandler dateHandler;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        ButterKnife.bind(this);
        name = getIntent().getStringExtra(NAME_DEADLINE_COUNTER);
        //setTitle(name);
        textView.setText(name);

        dataPref = new DataPref(this);
        dateHandler = new DateHandler(dataPref, name);
        dateHandler.attachView(this);

        setDate();

        dataPref.saveLongData(new GregorianCalendar().getTimeInMillis(), DateHandler.SAVE_PREF + name);
    }

    public void setDate() {
        if (dateHandler.getTimeInGeneral() != null) {
            generalDateView.setText(dateHandler.getTimeInGeneral());
        } else savedData.setText(R.string.error_load_date);

        daysView.setText(String.valueOf(dateHandler.getDays()));
        hoursView.setText(String.valueOf(dateHandler.getHours()));
        minutesView.setText(String.valueOf(dateHandler.getMinutes()));
        spentTime.setText(dateHandler.getSpentTime());

        if (dateHandler.getSpentTimeFromLastVisit() != null) {
            savedData.setText(dateHandler.getSpentTimeFromLastVisit());
        } else savedData.setText(R.string.error_load_date);

        progressBar.setProgress((int) dateHandler.getProgress());
        String progress = String.format("%.2f", dateHandler.getProgress()) + " %";
        progressTextView.setText(progress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_date_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actions_menu_set_deadline:
                dateHandler.dateTimePicker(this);
                break;
            case R.id.actions_menu_delete:
                AlertDialog.Builder ad = new AlertDialog.Builder(DateActivity.this);
                String title = "Delete this contact";
                String message = "Are you sure?";
                String button1String = "NO";
                String button2String = "YES";
                ad =
                        ad.setTitle(title);
                ad.setMessage(message);
                ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                    }
                });
                ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        Intent intent = new Intent();
                        intent.putExtra(NAME_DEADLINE_COUNTER, name);
                        dateHandler.deleteAllData();
                        Toast.makeText(DateActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        setResult(MainActivity.RESULT_DELETE_CONTACT, intent);
                        finish();
                    }
                });
                ad.setCancelable(true);
                ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                    }
                });
                ad.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
