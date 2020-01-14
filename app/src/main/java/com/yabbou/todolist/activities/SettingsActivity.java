package com.yabbou.todolist.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.yabbou.todolist.R;

public class SettingsActivity extends AppCompatActivity {

    private static boolean mPrefUseAutoSave; //rename
    private static String mKeyAutoSave;
    private static final String mKeyPrefsName = "PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

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
        switch (item.getItemId()) {
            case R.id.menu_toggle_auto_save: {

                boolean isChecked = item.isChecked();
                mPrefUseAutoSave = isChecked;
                item.setChecked(!isChecked);

                mPrefUseAutoSave = item.isChecked();
                return true;
            }
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static boolean hasCheckedAutoSave() {
        return mPrefUseAutoSave;
    }

    public static String getmKeyPrefsName() {
        return mKeyPrefsName;
    }

    public static String getmKeyAutoSave() {
        return mKeyAutoSave;
    }

    public static void setmPrefUseAutoSave(boolean mPrefUseAutoSave) {
        SettingsActivity.mPrefUseAutoSave = mPrefUseAutoSave;
    }

    public static void setmKeyAutoSave(String key) {
        mKeyAutoSave = key;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}