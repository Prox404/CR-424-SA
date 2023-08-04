package com.prox.cr424sa.trancongtri.qr_code;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Locale;

public class SettingsActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            SwitchPreferenceCompat languageSwitch = findPreference("language");
            if (languageSwitch != null) {
                languageSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        boolean isChecked = (boolean) newValue;
                        if (isChecked) {
                            LanguageManager.getInstance().setAppLanguage("vi");
                        } else {
                            LanguageManager.getInstance().setAppLanguage("en");
                        }

                        // Update language for all activities
                        LanguageManager.getInstance().updateAppLanguage();

                        return true;
                    }
                });
            }

            SwitchPreferenceCompat themeSwitch = findPreference("theme");
            if (themeSwitch != null) {
                themeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        boolean isChecked = (boolean) newValue;
                        MyApplication myApplication = (MyApplication) getActivity().getApplication();
                        myApplication.setDarkTheme(isChecked);

                        return true;
                    }
                });
            }
        }

    }
}