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
import com.google.android.material.snackbar.Snackbar;
import com.yabbou.todolist.R;
import com.yabbou.todolist.classes.Utils;

import java.util.ArrayList;

import static com.yabbou.todolist.R.*;
import static com.yabbou.todolist.classes.Utils.sREQUEST_CODE_SETTINGS;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter model;
    private ArrayList<String> items;

    private EditText etNewItemText;

    private boolean mPrefUseAutoSave, mPrefShowErrors;
    private String mKeyAutoSave, mKeyShowErrors;

    private final String mKeyPrefsName = "PREFS";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        items = new ArrayList<>();
        initVars();
        setupFAB();

        setupPreferences(savedInstanceState);
    }

    private void initVars() {
        model = getStringArrayAdapter();
        ListView lvItems = findViewById(id.list_todo);
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

    private void setupPreferences(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.root_preferences, true);

        setFieldReferencesToResFileValues();
        setFieldReferencesToViewsAndSnackBar();

        restoreAppSettingsFromPrefs();
        doInitialStartGame(savedInstanceState);
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

    private void showSettings() { //todo: full settings pages
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

            Log.d(TAG, "Item added to list: " + itemText);
        }
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView tvItemTitle = parent.findViewById(id.item_title);
        String itemText = String.valueOf(tvItemTitle.getText());

        model.remove(itemText); //todo: remove from items array using index, instead of directly from model
        model.notifyDataSetChanged();
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

    private void setFieldReferencesToResFileValues() {
        mKeyAutoSave = getString(R.string.key_use_auto_save);
        mKeyShowErrors = getString(R.string.key_show_turn_specific_error_messages);
    }

    private void setFieldReferencesToViewsAndSnackBar() {
    }

    private void restoreAppSettingsFromPrefs() {
        SharedPreferences preferences = getSharedPreferences(mKeyPrefsName, MODE_PRIVATE);

        mPrefUseAutoSave = preferences.getBoolean(mKeyAutoSave, true);
        mPrefShowErrors = preferences.getBoolean(mKeyShowErrors, true);
    }

    private void doInitialStartGame(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            restoreSavedStateFromBundle(savedInstanceState);
        } else if (mPrefUseAutoSave && isValidStateInPrefs()) {
            restoreSavedStateFromPrefs();
        } else {
            startNewGame();
        }
    }

    private void restoreSavedStateFromBundle(Bundle savedInstanceState) {

    }

    private void initAdapter(ArrayAdapter adapter, boolean[] checks, int msgID) {
        model = adapter != null ? adapter : getStringArrayAdapter();
    }

    private ArrayAdapter<String> getStringArrayAdapter() {
        return new ArrayAdapter<>(this, layout.todo_item, id.item_title, items);
    }

    private boolean isValidStateInPrefs() {

        return false;
    }

    private void restoreSavedStateFromPrefs() {
    }

    private void startNewGame() {
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences preferences = getSharedPreferences (mKeyPrefsName, MODE_PRIVATE);
// Create an Editor object to write changes to the preferences object above
        SharedPreferences.Editor editor = preferences.edit ();
// clear whatever was set last time
        editor.clear ();
// save "autoSave" preference
        editor.putBoolean (mKeyAutoSave, mPrefUseAutoSave);
// apply the changes to the XML file in the device's storage
        editor.apply ();
    }
}
