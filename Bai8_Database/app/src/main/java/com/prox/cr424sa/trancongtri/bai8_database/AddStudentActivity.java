package com.prox.cr424sa.trancongtri.bai8_database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddStudentActivity extends AppCompatActivity {

    TextView edtHoten, edtNgaySinh, edtSoDT, edtDiaChi, edtEmail, edtNganh;
    MyDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        edtHoten = findViewById(R.id.edit_text_name);
        edtNgaySinh = findViewById(R.id.edit_text_dob);
        edtSoDT = findViewById(R.id.edit_text_phone);
        edtDiaChi = findViewById(R.id.edit_text_address);
        edtEmail = findViewById(R.id.edit_text_email);
        edtNganh = findViewById(R.id.edit_text_major);
        db = new MyDB(AddStudentActivity.this);

        Button store = findViewById(R.id.btn_add);
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Hoten = edtHoten.getText().toString();
                String NgaySinh = edtNgaySinh.getText().toString();
                String SoDT = edtSoDT.getText().toString();
                String DiaChi = edtDiaChi.getText().toString();
                String Email = edtEmail.getText().toString();
                String Nganh = edtNganh.getText().toString();

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                Date ngaySinhDate = new Date();

                try {
                    ngaySinhDate = formatter.parse(NgaySinh);
                } catch (java.text.ParseException e) {
                    Log.i("Loi date", "loiiiiiiiii");
                }

                boolean result = db.insertStudent(Hoten, ngaySinhDate, SoDT, DiaChi, Email, Nganh);

                Toast toast;
                if (result) {
                    toast = Toast.makeText(AddStudentActivity.this, "Add student successfuly !", Toast.LENGTH_SHORT);
                    toast.show();
                    edtHoten.setText("");
                    edtNgaySinh.setText("");
                    edtSoDT.setText("");
                    edtDiaChi.setText("");
                    edtEmail.setText("");
                    edtNganh.setText("");
                } else {
                    toast = Toast.makeText(AddStudentActivity.this, "Add student failed !", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }
}