package com.prox.cr424sa.trancongtri.qr_code;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class MyApplication extends Application {

    private static final String KEY_DARK_THEME = "dark_theme";
    private boolean isDarkTheme = false;

    @Override
    public void onCreate() {
        super.onCreate();
        LanguageManager.init(this);

        loadThemeState();
    }

    public boolean isDarkTheme() {
        return isDarkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        isDarkTheme = darkTheme;
        // Save the theme state to SharedPreferences
        saveThemeState();
    }

    private void loadThemeState() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        isDarkTheme = preferences.getBoolean(KEY_DARK_THEME, false);
    }

    private void saveThemeState() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putBoolean(KEY_DARK_THEME, isDarkTheme).apply();
    }
}
