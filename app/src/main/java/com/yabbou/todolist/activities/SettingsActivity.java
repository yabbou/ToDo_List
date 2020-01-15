package com.yabbou.todolist.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.yabbou.todolist.R;

public class SettingsActivity extends AppCompatActivity {

    private static boolean mPrefUseAutoSave = true;
    private static final String mKeyPrefsName = "PREFS";
    private static String mKeyAutoSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        setupSupportFragment();
        setupActionBar();
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

    public static void setmPrefUseAutoSave(boolean preference) {
        mPrefUseAutoSave = preference;
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