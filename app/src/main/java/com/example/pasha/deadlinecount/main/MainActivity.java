package com.example.pasha.deadlinecount.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasha.deadlinecount.R;
import com.example.pasha.deadlinecount.date.DataPref;
import com.example.pasha.deadlinecount.date.DateActivity;
import com.example.pasha.deadlinecount.date.DateHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DeadlineCallbacks {

    private static final String DEADLINE_PREF = "Json from deadline names";
    private static final String NAME_DEADLINE_COUNTER = "Name of deadline counter";
    public static final int RESULT_DELETE_CONTACT = 1;
    private static final String DESCRIPTION_DEADLINE_COUNTER = "Description of deadline counter";

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    private ArrayList<String> deadlineNames;
    private DataPref dataPref;
    private Gson gson;
    private DateHandler dateHandler;
    private String editTextName;
    private String editTextDescription;
    private DeadlinesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dataPref = new DataPref(this);
        gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        deadlineNames = gson.fromJson(dataPref.loadStringData(DEADLINE_PREF), type);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        adapter = new DeadlinesAdapter(deadlineNames, this, dataPref);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_Add:

                final EditText editName = new EditText(MainActivity.this);
                final EditText editDescription = new EditText(MainActivity.this);
                editName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
                editName.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                        | android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                editName.setSingleLine(true);
                editDescription.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                        | android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                final TextView textViewMessageOne = new TextView(MainActivity.this);
                textViewMessageOne.setText("(name)");
                final TextView textViewMessageTwo = new TextView(MainActivity.this);
                textViewMessageTwo.setText("(description)");
                textViewMessageOne.setGravity(Gravity.CENTER_HORIZONTAL);
                textViewMessageTwo.setGravity(Gravity.CENTER_HORIZONTAL);

                LinearLayout ll = new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(editName);
                ll.addView(textViewMessageOne);
                ll.addView(editDescription);
                ll.addView(textViewMessageTwo);

                final AlertDialog dialog = new AlertDialog.Builder(this)
                        .setView(ll)
                        .setTitle("Create new deadline counter")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", null)
                        .create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                editTextName = editName.getText().toString().trim();
                                editTextDescription = editDescription.getText().toString().trim();
                                if (editTextName.length() == 0) {
                                    Toast.makeText(MainActivity.this, "Name is empty!" + "\n" +
                                            "Please try again", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (deadlineNames.contains(editTextName)) {
                                        Toast.makeText(MainActivity.this, "Already exist!" + "\n" +
                                                "Please try again", Toast.LENGTH_SHORT).show();
                                    } else {
                                        dateHandler = new DateHandler(dataPref, editTextName);
                                        dateHandler.setCallbacks(MainActivity.this);
                                        dateHandler.dateTimePicker(MainActivity.this);
                                        dialog.dismiss();
                                    }
                                }
                            }
                        });
                    }
                });
                dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == RESULT_DELETE_CONTACT) {
            deadlineNames.remove(data.getStringExtra(NAME_DEADLINE_COUNTER));
            deadlineNames.remove(data.getStringExtra(NAME_DEADLINE_COUNTER));
            adapter.notifyDataSetChanged();
            saveChanges();
        }
    }

    private void saveChanges() {
        String jsonDeadlineNames = gson.toJson(deadlineNames);
        dataPref.saveStringData(jsonDeadlineNames, DEADLINE_PREF);
        Log.d("TAG", "jsonSave = " + jsonDeadlineNames);
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(MainActivity.this, DateActivity.class);
        intent.putExtra(NAME_DEADLINE_COUNTER, deadlineNames.get(position));
        startActivityForResult(intent, 1);
    }

    @Override
    public void onCreateDateActivity() {
        deadlineNames.add(editTextName);
        saveChanges();
        Intent intent = new Intent(MainActivity.this, DateActivity.class);
        intent.putExtra(NAME_DEADLINE_COUNTER, editTextName);
        intent.putExtra(DESCRIPTION_DEADLINE_COUNTER, editTextDescription);
        startActivityForResult(intent, 1);
    }
}
