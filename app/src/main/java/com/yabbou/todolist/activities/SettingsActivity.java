package com.yabbou.todolist.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.yabbou.todolist.R;

public class SettingsActivity extends AppCompatActivity {

    private static boolean mPrefUseAutoSave = true;
    private static final String mKeyPrefsName = "PREFS";
    private static String mKeyAutoSave;

    private CheckBox checkBoxAutoSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        setupSupportFragment();
        setupActionBar();
        setupCheckboxListener();
    }

    private void setupSupportFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupCheckboxListener() {
        checkBoxAutoSave = findViewById(R.id.checkbox_auto_save);
        checkBoxAutoSave.setChecked(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("value", false));

        checkBoxAutoSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPrefUseAutoSave = checkBoxAutoSave.isChecked();
            }
        });
    }



    /* getters */

    public static boolean hasCheckedAutoSave() {
        return mPrefUseAutoSave;
    }

    public static String getmKeyAutoSave() {
        return mKeyAutoSave;
    }

    public static String getmKeyPrefsName() {
        return mKeyPrefsName;
    }

    /* setters */

    public static void setmPrefUseAutoSave(boolean toggleAutoSave) {
        SettingsActivity.mPrefUseAutoSave = toggleAutoSave;
    }

    public static void setmKeyAutoSave(String key) {
        mKeyAutoSave = key;
    }

    /* fragment */

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}