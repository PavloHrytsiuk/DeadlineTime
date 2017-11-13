package com.example.pasha.deadlinecount.date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
    private int iconWidth;
    private int iconHeight;

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
                if (changeDesc && motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("Tag", "getY " + motionEvent.getY());
                    Log.d("Tag", "getX " + motionEvent.getX());
                    Log.d("Tag", "getHeight " + (view.getHeight() - view.getPaddingBottom()
                            - iconHeight));
                    Log.d("Tag", "getWight " + (view.getWidth() - view.getPaddingRight()
                            - iconWidth));
                    if (motionEvent.getY() > (view.getHeight() - view.getPaddingBottom()
                            - iconHeight)) {
                        if (motionEvent.getX() > (view.getWidth() - view.getPaddingRight()
                                - iconWidth)) {
                            dataPref.saveStringData(String.valueOf(descriptionView.getText()),
                                    SAVE_DESCRIPTION + name);
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
                }
                return false;
            }
        });

        descriptionView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(@NonNull final View view) {

                final Bitmap bitmap = BitmapFactory.decodeResource(descriptionView.getResources(),
                        R.drawable.ic_done_black_18dp);
                Log.d("Tag", "Intrinsic " + bitmap.getWidth() + "--" + bitmap.getHeight());
                loadIconSize(bitmap.getWidth(), bitmap.getHeight());
                TopGravityDrawable icon = new TopGravityDrawable(descriptionView.getResources(),
                        bitmap);
                descriptionView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        icon, null);

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

    private void loadIconSize(final int width, final int height) {
        iconHeight = height;
        iconWidth = width;
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
                final AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Delete this contact")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", null)
                        .setMessage("Are you sure?")
                        .create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(@NonNull final DialogInterface dialogInterface) {
                        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(@NonNull final View view) {
                                Intent intent = new Intent();
                                intent.putExtra(NAME_DEADLINE_COUNTER, name);
                                dateHandler.deleteAllData();
                                Toast.makeText(DateActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                setResult(MainActivity.RESULT_DELETE_CONTACT, intent);
                                finish();
                                dialog.dismiss();
                            }
                        });
                    }
                });
                dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private class TopGravityDrawable extends BitmapDrawable {

        private TopGravityDrawable(@NonNull final Resources res, @NonNull final Bitmap bitmap) {
            super(res, bitmap);
        }

        @Override
        public void draw(@NonNull final Canvas canvas) {
            int halfCanvas = canvas.getHeight() / 2;
            int halfDrawable = getIntrinsicHeight() / 2;
            canvas.save();
            canvas.translate(0, +halfCanvas - halfDrawable * 2);
            super.draw(canvas);
            canvas.restore();
        }
    }

    @Override
    protected void onDestroy() {
        if (changeDesc) {
            dataPref.saveStringData(String.valueOf(descriptionView.getText()), SAVE_DESCRIPTION + name);
        }
        super.onDestroy();
    }
}
