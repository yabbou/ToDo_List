package com.yabbou.todolist.activities;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yabbou.todolist.R;
import com.yabbou.todolist.classes.PreferenceBuilder;
import com.yabbou.todolist.classes.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.yabbou.todolist.R.id;
import static com.yabbou.todolist.R.layout;
import static com.yabbou.todolist.R.string;
import static com.yabbou.todolist.classes.Utils.sREQUEST_CODE_SETTINGS;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<String> model;
    private ArrayList<String> items;
    private EditText etNewItemText;

    private PreferenceBuilder preference;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupPreferences();

        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        readListItems();
        initVars();

        setupFAB();
    }

    private void initVars() {
        model = new ArrayAdapter<>(this, layout.todo_item, id.item_title, items);
        ListView lvItems = findViewById(id.list_todo);

        TextView emptyText = findViewById(R.id.empty);
        lvItems.setEmptyView(emptyText);

        lvItems.setAdapter(model);
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
        preference = new PreferenceBuilder();
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.root_preferences, true);

        setFieldReferencesToResFileValues();
        restorePreferences();
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

    /* preferences */

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == sREQUEST_CODE_SETTINGS) {
            restorePreferences();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setFieldReferencesToResFileValues() {
        preference.setmKeyAutoSave(getString(R.string.key_use_auto_save));
    }

    private void restorePreferences() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String currentKey = getString(string.key_use_auto_save);
        preference.setmPrefUseAutoSave(preferences.getBoolean(currentKey, true));
    }

    /**
     * Note: Apache commons version {@code FileUtils}
     */
    private void readListItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = preference.hasCheckedAutoSave() ?
                    new ArrayList<String>(FileUtils.readLines(todoFile)) :
                    initToDoList();
        } catch (IOException e) {
            items = initToDoList();
        }
    }

    private ArrayList<String> initToDoList() {
        return new ArrayList<>();
    }

    private void writeListItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, preference.hasCheckedAutoSave() ? items : initToDoList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences preferences = getSharedPreferences(preference.getmKeyPrefsName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        editor.putBoolean(preference.getmKeyAutoSave(), preference.hasCheckedAutoSave());
        editor.apply();
    }
}
