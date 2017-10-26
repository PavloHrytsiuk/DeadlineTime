package com.example.pasha.deadlinecount.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.pasha.deadlinecount.R;
import com.example.pasha.deadlinecount.date.DataPref;
import com.example.pasha.deadlinecount.date.DateActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DeadlineCallbacks {

    private static final String DEADLINE_PREF = "Json from deadline names";
    private static final String NAME_DEADLINE_COUNTER = "Name of deadline counter";
    private static final String NEW_DEADLINE_COUNTER = "New deadline counter";

    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    private ArrayList<String> deadlineNames;
    private DataPref dataPref;
    private Gson gson;


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
        Log.d("TAG", "ArrayListLoad = " + Arrays.asList(deadlineNames));
        Log.d("TAG", "jsonLoad = " + dataPref.loadStringData(DEADLINE_PREF));

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        DeadlinesAdapter adapter = new DeadlinesAdapter(deadlineNames, this);
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

                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                final EditText edittext = new EditText(MainActivity.this);
                alert.setMessage("Please press name:");
                alert.setTitle("New Deadline Counter");

                alert.setView(edittext);

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String editTextValue = edittext.getText().toString();
                        deadlineNames.add(editTextValue);
                        saveChanges();
                        Log.d("TAG", "edTextInDialog = " + editTextValue);
                        Intent intent = new Intent(MainActivity.this, DateActivity.class);
                        intent.putExtra(NAME_DEADLINE_COUNTER, editTextValue);
                        intent.putExtra(NEW_DEADLINE_COUNTER, true);
                        startActivity(intent);
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // action ON
                    }
                });

                alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveChanges() {
        String jsonDeadlineNames = gson.toJson(deadlineNames);
        dataPref.saveSringData(jsonDeadlineNames, DEADLINE_PREF);
        Log.d("TAG", "jsonSave = " + jsonDeadlineNames);
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(MainActivity.this, DateActivity.class);
        intent.putExtra(NAME_DEADLINE_COUNTER, deadlineNames.get(position));
        intent.putExtra(NEW_DEADLINE_COUNTER, false);
        startActivity(intent);
    }
}
