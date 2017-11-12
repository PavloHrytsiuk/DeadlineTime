package com.example.pasha.deadlinecount.date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasha.deadlinecount.R;
import com.example.pasha.deadlinecount.main.MainActivity;

import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class DateActivity extends AppCompatActivity {

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
    @BindView(R.id.descriptionView)
    EditText descriptionView;

    private static final String NAME_DEADLINE_COUNTER = "Name of deadline counter";
    private static final String DESCRIPTION_DEADLINE_COUNTER = "Description of deadline counter";
    private static final String SAVE_DESCRIPTION = "Save description";

    private DataPref dataPref;
    private DateHandler dateHandler;
    private String name;
    private boolean changeDesc = false;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        ButterKnife.bind(this);
        name = getIntent().getStringExtra(NAME_DEADLINE_COUNTER);
        dataPref = new DataPref(this);
        dateHandler = new DateHandler(dataPref, name);
        dateHandler.attachView(this);
        setDescription();
        setTitle(name);
        setDate();
        dataPref.saveLongData(new GregorianCalendar().getTimeInMillis(), DateHandler.SAVE_PREF + name);
    }

    private void setDescription() {
        String description = getIntent().getStringExtra(DESCRIPTION_DEADLINE_COUNTER);
        if (description != null) {
            descriptionView.setText(description);
            dataPref.saveStringData(description, SAVE_DESCRIPTION + name);
        } else {
            description = dataPref.loadStringData(SAVE_DESCRIPTION + name);
            if (description != null && !description.isEmpty()) {
                descriptionView.setText(description);
            } else descriptionView.setText(name);
        }
        descriptionView.setFocusable(false);
        descriptionView.setClickable(false);
        descriptionView.setFocusableInTouchMode(false);
        descriptionView.clearFocus();

        descriptionView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(@NonNull final View view, @NonNull final MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() > (view.getWidth() - view.getPaddingRight())) {
                        dataPref.saveStringData(String.valueOf(descriptionView.getText()), SAVE_DESCRIPTION + name);
                        Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
                        descriptionView.setFocusable(false);
                        descriptionView.setClickable(false);
                        descriptionView.setFocusableInTouchMode(false);
                        descriptionView.clearFocus();
                        descriptionView.setCompoundDrawables(null, null, null, null);

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        changeDesc = false;
                    }
                }
                return false;
            }
        });

        descriptionView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(@NonNull final View view) {
                Drawable x = ContextCompat.getDrawable(DateActivity.this, R.drawable.ic_done_black_18dp);
                x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
                descriptionView.setCompoundDrawables(null, null, x, null);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                descriptionView.setFocusable(true);
                descriptionView.setEnabled(true);
                descriptionView.setFocusableInTouchMode(true);
                descriptionView.requestFocus();
                changeDesc = true;
                return true;
            }
        });
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
    public boolean onCreateOptionsMenu(@NonNull final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_date_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actions_menu_set_deadline:
                dateHandler.dateTimePicker(this);
                break;
            case R.id.actions_menu_delete:
                final AlertDialog.Builder ad = new AlertDialog.Builder(DateActivity.this);
                ad.setTitle("Delete this contact");
                ad.setMessage("Are you sure?");
                ad.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                    }
                });
                ad.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
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

    @Override
    protected void onDestroy() {
        if (changeDesc) {
            dataPref.saveStringData(String.valueOf(descriptionView.getText()), SAVE_DESCRIPTION + name);
        }
        super.onDestroy();
    }
}
