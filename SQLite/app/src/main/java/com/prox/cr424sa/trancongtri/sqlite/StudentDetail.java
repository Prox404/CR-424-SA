package com.prox.cr424sa.trancongtri.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class StudentDetail extends AppCompatActivity {

    private TextView textViewID, textViewName, textViewNgaySinh, textViewSoDT, textViewDiaChi, textViewEmail, textViewNganh;
    MyDB db;

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
        db = new MyDB(this);

        Intent intent = getIntent();
        if (intent.hasExtra("student")) {
            Student student = (Student) intent.getSerializableExtra("student");
            Student newStudent = db.getStudentByID(student.getID());
            if (newStudent != null) {
                Log.i("Student_Detail", newStudent.toString());
                textViewID.setText(String.valueOf(newStudent.getID()));
                textViewName.setText(newStudent.getHoten());
                textViewNgaySinh.setText(newStudent.getNgaySinh().toString());
                textViewSoDT.setText(newStudent.getSoDT());
                textViewDiaChi.setText(newStudent.getDiaChi());
                textViewEmail.setText(newStudent.getEmail());
                textViewNganh.setText(newStudent.getTenNganh());
            }
        }
    }
}