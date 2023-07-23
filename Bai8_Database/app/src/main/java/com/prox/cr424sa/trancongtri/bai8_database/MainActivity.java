package com.prox.cr424sa.trancongtri.bai8_database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvStudent;
    StudentAdapter studentAdapter;
    List<Student> studentList;
    MyDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new MyDB(MainActivity.this);

//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.YEAR, 1988);
//        cal.set(Calendar.MONTH, Calendar.JANUARY);
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//        Date ngaySinh = cal.getTime();
//        db.insertStudent("Prox", ngaySinh, "0123", "QN", "prox@gmail.com", "SE");
//        db.close();

        lvStudent = findViewById(R.id.listViewStudents);
        studentList = db.readStudent();
//        Log.i("getStudent", studentList.get(0).getHoten());
        studentAdapter = new StudentAdapter(MainActivity.this, studentList);
        lvStudent.setAdapter(studentAdapter);
        lvStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Student selectedStudent = studentList.get(i);
                Intent intent = new Intent(MainActivity.this, StudentDetail.class);
                intent.putExtra("student", selectedStudent);
                startActivity(intent);
            }
        });

        Button add = findViewById(R.id.btn_store);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(MainActivity.this, AddStudentActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStudentList();
    }
    private void updateStudentList() {
        studentList.clear();
        studentList.addAll(db.readStudent());
        studentAdapter.notifyDataSetChanged();
    }
}