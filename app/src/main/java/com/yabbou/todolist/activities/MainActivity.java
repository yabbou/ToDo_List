package com.yabbou.todolist.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yabbou.todolist.R;
import com.yabbou.todolist.classes.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.yabbou.todolist.R.*;
import static com.yabbou.todolist.classes.Utils.sREQUEST_CODE_SETTINGS;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<String> model;
    private ArrayList<String> items;
    private EditText etNewItemText;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        readListItems();
        initVars();

        setupFAB();
        setupPreferences();
    }

    private void initVars() {
        model = getStringArrayAdapter();
        ListView lvItems = findViewById(id.list_todo);
        lvItems.setAdapter(model);
    }

    private ArrayAdapter<String> getStringArrayAdapter() {
        return new ArrayAdapter<>(this, layout.todo_item, id.item_title, items);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                etNewItemText = new EditText(MainActivity.this);

                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add: New Task")
                        .setView(etNewItemText)

                        .setPositiveButton("Add", new OnAddClickListener())
                        .setNegativeButton("Cancel", null)
                        .create();

                dialog.show();

                Log.d(TAG, "Added a new task");
            }
        });
    }

    private void setupPreferences() {
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.root_preferences, true);

        setFieldReferencesToResFileValues();
        restoreAppSettingsFromPrefs();
    }

    /* menu */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_settings:
                showSettings();
                return true;
            case R.id.menu_about:
                showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSettings() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivityForResult(intent, sREQUEST_CODE_SETTINGS);
    }

    public void showAbout() {
        Utils.showInfoDialog(MainActivity.this, R.string.about_dialog_title,
                R.string.about_dialog_banner);
    }

    /* add & delete list elements */

    private class OnAddClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            String itemText = etNewItemText.getText().toString();
            items.add(itemText);
            model.notifyDataSetChanged();
            writeListItems();

            Log.d(TAG, "Item added to list: " + itemText);
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView tvItemTitle = parent.findViewById(id.item_title);
        String itemText = String.valueOf(tvItemTitle.getText());

        model.remove(itemText); //todo: remove from items array using index, instead of directly from model
        model.notifyDataSetChanged();
        writeListItems();
    }

    /* save state on rotation */

    @Override
    protected void onSaveInstanceState(@NonNull Bundle out) {
        super.onSaveInstanceState(out);
        out.putStringArrayList(getString(R.string.todo_string_list), items);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        items = savedInstanceState.getStringArrayList(getString(string.todo_string_list));

        initVars();
        model.notifyDataSetChanged();
    }

    /* preferences */

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private void setFieldReferencesToResFileValues() {
        SettingsActivity.setmKeyAutoSave(getString(R.string.key_use_auto_save));
    }

    private void restoreAppSettingsFromPrefs() {
        SharedPreferences preferences = getSharedPreferences(SettingsActivity.getmKeyPrefsName(), MODE_PRIVATE);
        SettingsActivity.setmPrefUseAutoSave(preferences.getBoolean(SettingsActivity.getmKeyAutoSave(), true));
    }

    /**
     * Note: Apache commons version {@code FileUtils}
     */
    private void readListItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = SettingsActivity.hasCheckedAutoSave() ?
                    new ArrayList<String>(FileUtils.readLines(todoFile)) :
                    initToDoList();
        } catch (IOException e) {
            items = initToDoList();
        }
    }

    private ArrayList<String> initToDoList() {
        return new ArrayList<>();
    }

    private void writeListItems() { //todo: make encrypted
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, SettingsActivity.hasCheckedAutoSave() ? items : initToDoList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences preferences = getSharedPreferences(SettingsActivity.getmKeyPrefsName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        editor.putBoolean(SettingsActivity.getmKeyAutoSave(), SettingsActivity.hasCheckedAutoSave());
        editor.apply();
    }
}
