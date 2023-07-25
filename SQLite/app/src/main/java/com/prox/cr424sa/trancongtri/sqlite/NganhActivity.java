package com.prox.cr424sa.trancongtri.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class NganhActivity extends AppCompatActivity {

    ListView lvNganh;

    NganhAdapter nganhAdapter;
    MyDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nganh);

        db = new MyDB(this);
        List<Nganh> listNganh = db.readNganh();
        lvNganh = findViewById(R.id.lv_nganh);
        nganhAdapter = new NganhAdapter(this, listNganh);
        lvNganh.setAdapter(nganhAdapter);
    }
}