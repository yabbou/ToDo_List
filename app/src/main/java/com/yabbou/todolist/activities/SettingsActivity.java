package com.yabbou.todolist.activities;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.yabbou.todolist.R;

public class SettingsActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.settings_activity);
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.settings, new SettingsFragment())
//                .commit();
//
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//            return true;
//        } else {
//            return super.onOptionsItemSelected(item);
//        }
//    }
//
//    public static class SettingsFragment extends PreferenceFragmentCompat {
//        @Override
//        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//            setPreferencesFromResource(R.xml.root_preferences, rootKey);
//            setEditTextPrefsInputTypeToDecimal();
//        }
//
//        private void setEditTextPrefsInputTypeToDecimal() {
//            String taxPrefKey = getString(R.string.defaultTaxPercentageKey);
//            String tipPrefKey = getString(R.string.defaultTipPercentageKey);
//
//            EditTextPreference taxPreference = findPreference(taxPrefKey);
//            EditTextPreference tipPreference = findPreference(tipPrefKey);
//
//            if (taxPreference != null && tipPreference != null) {
//                EditTextPreference.OnBindEditTextListener listener = getNewDecimalListener();
//
//                taxPreference.setOnBindEditTextListener(listener);
//                tipPreference.setOnBindEditTextListener(listener);
//            }
//        }
//
//        private EditTextPreference.OnBindEditTextListener getNewDecimalListener() {
//            return new EditTextPreference.OnBindEditTextListener() {
//                @Override
//                public void onBindEditText(@NonNull EditText editText) {
//                    editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//                }
//            };
//        }
//    }
}