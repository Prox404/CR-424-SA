package com.prox.cr424sa.trancongtri.qr_code;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.preference.PreferenceManager;

import java.util.Locale;

public class LanguageManager {
    private static final String KEY_LANGUAGE = "app_language";
    private static final String DEFAULT_LANGUAGE = "en"; // Mã ngôn ngữ mặc định là Tiếng Anh

    private static LanguageManager instance;
    private SharedPreferences preferences;
    private Context context;

    private LanguageManager(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new LanguageManager(context);
        }
    }

    public static LanguageManager getInstance() {
        return instance;
    }

    public void setAppLanguage(String languageCode) {
        preferences.edit().putString(KEY_LANGUAGE, languageCode).apply();
    }

    public String getAppLanguage() {
        return preferences.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE);
    }

    public void updateAppLanguage() {
        String languageCode = getAppLanguage();
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }

    public ContextWrapper wrapContext(Context context) {
        String languageCode = getAppLanguage();
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        return new ContextWrapper(context.createConfigurationContext(configuration));
    }
}
