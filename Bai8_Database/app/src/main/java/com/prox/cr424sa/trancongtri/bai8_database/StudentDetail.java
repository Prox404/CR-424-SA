package com.prox.cr424sa.trancongtri.bai8_database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class StudentDetail extends AppCompatActivity {

    private TextView textViewID, textViewName, textViewNgaySinh, textViewSoDT, textViewDiaChi, textViewEmail, textViewNganh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        textViewName = findViewById(R.id.textViewName);
        textViewID = findViewById(R.id.textViewID);
        textViewNgaySinh = findViewById(R.id.textViewNgaySinh);
        textViewSoDT = findViewById(R.id.textViewSoDT);
        textViewDiaChi = findViewById(R.id.textViewDiaChi);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewNganh = findViewById(R.id.textViewNganh);

        // Lấy thông tin student từ Intent
        Intent intent = getIntent();
        if (intent.hasExtra("student")) {
            Student student = (Student) intent.getSerializableExtra("student");
            if (student != null) {
                Log.i("Student_Detail", student.toString());
                textViewID.setText(String.valueOf(student.getID()));
                textViewName.setText(student.getHoten());
                textViewNgaySinh.setText(student.getNgaySinh().toString());
                textViewSoDT.setText(student.getSoDT());
                textViewDiaChi.setText(student.getDiaChi());
                textViewEmail.setText(student.getEmail());
                textViewNganh.setText(student.getNganh());
            }
        }
    }
}