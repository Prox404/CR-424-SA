package com.prox.cr424sa.trancongtri.qr_code;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageManager.getInstance().wrapContext(newBase));
    }

    @Override
    public Resources getResources() {
        return super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return super.getAssets();
    }

    @Override
    public Resources.Theme getTheme() {
        return super.getTheme();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateTheme();

        super.onCreate(savedInstanceState);
        LanguageManager.getInstance().updateAppLanguage();
    }

    protected void updateTheme() {
        MyApplication myApplication = (MyApplication) getApplication();
        boolean isDarkTheme = myApplication.isDarkTheme();

        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}

