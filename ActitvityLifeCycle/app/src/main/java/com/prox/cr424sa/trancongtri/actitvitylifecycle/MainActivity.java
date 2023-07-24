package com.prox.cr424sa.trancongtri.actitvitylifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("status" , "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("status" , "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("status" , "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("status" , "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("status" , "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("status" , "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("status" , "onResume");
    }
}