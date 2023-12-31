package com.prox.cr424sa.trancongtri.bai8_database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditStudentActivity extends AppCompatActivity {

    TextView edtHoten, edtNgaySinh, edtSoDT, edtDiaChi, edtEmail, edtNganh;

    String Hoten = "", NgaySinh = "", SoDT  = "",DiaChi  = "", Email  = "", Nganh  = "";
    MyDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        edtHoten = findViewById(R.id.edit_text_name);
        edtNgaySinh = findViewById(R.id.edit_text_dob);
        edtSoDT = findViewById(R.id.edit_text_phone);
        edtDiaChi = findViewById(R.id.edit_text_address);
        edtEmail = findViewById(R.id.edit_text_email);
        edtNganh = findViewById(R.id.edit_text_major);
        db = new MyDB(EditStudentActivity.this);

        Intent i = getIntent();
        if (i.hasExtra("student")) {
            Student student = (Student) i.getSerializableExtra("student");
            if (student != null) {
                Log.i("Student_Detail", student.toString());

                edtHoten.setText(student.getHoten());
                edtNgaySinh.setText(student.getNgaySinh().toString());
                edtSoDT.setText(student.getSoDT());
                edtDiaChi.setText(student.getDiaChi());
                edtEmail.setText(student.getEmail());
                edtNganh.setText(student.getNganh());

                Button store = findViewById(R.id.btn_save);
                store.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int ID = student.getID();
                        Hoten =  edtHoten.getText().toString();
                        NgaySinh =  edtNgaySinh.getText().toString();
                        SoDT =  edtSoDT.getText().toString();
                        DiaChi =  edtDiaChi.getText().toString();
                        Email =  edtEmail.getText().toString();
                        Nganh =  edtNganh.getText().toString();

                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date ngaySinhDate = new Date();

                        try {
                            ngaySinhDate = formatter.parse(NgaySinh);
                        } catch (java.text.ParseException e) {
                            Log.i("Loi date", "loiiiiiiiii");
                        }

                        boolean result =  db.updateStudent(ID, Hoten, ngaySinhDate, SoDT, DiaChi, Email, Nganh);
                        Toast toast;
                        if (result) {
                            toast = Toast.makeText(EditStudentActivity.this, "Edit student successfuly !", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            toast = Toast.makeText(EditStudentActivity.this, "Edit student failed !", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
            }
        }
    }
}