package com.yabbou.todolist.classes;

public class PreferenceBuilder {

    private boolean mPrefUseAutoSave = true;
    private String mKeyAutoSave;

    /* getters */

    public boolean hasCheckedAutoSave() {
        return mPrefUseAutoSave;
    }

    public String getmKeyAutoSave() {
        return mKeyAutoSave;
    }

    public String getmKeyPrefsName() {
        return "PREFS";
    }

    /* setters */

    public void setmPrefUseAutoSave(boolean preference) {
        mPrefUseAutoSave = preference;
    }

    public void setmKeyAutoSave(String key) {
        mKeyAutoSave = key;
    }


}