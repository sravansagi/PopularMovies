package com.sravan.ad.popularmovies;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by HP on 1/8/2017.
 */

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    private final static String LOG_TAG = SettingsActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_main);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sortby_key)));
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference, PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(),""));
    }

    /**
     * The onPreferenceChange method is called when there is a change in any of the preference
     * value or even when the same preference value is selected again by the User.
     * It is also called when setting activity is started.
     * @param preference
     * @param newValue
     * @return
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();
        if(preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0){
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        return true;
    }
}
