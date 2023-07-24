package com.prox.cr424sa.trancongtri.sqlite;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditStudentActivity extends AppCompatActivity {

    TextView edtHoten, edtNgaySinh, edtSoDT, edtDiaChi, edtEmail, edtLop;
    Spinner spnNganh;
    MyDB db;
    Student student;

    String Hoten = "", NgaySinh = "", SoDT  = "",DiaChi  = "", Email  = "";

    int ID_Lop = 0;
    int Nganh_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        db = new MyDB(this);
        edtHoten = findViewById(R.id.edit_text_name);
        edtNgaySinh = findViewById(R.id.edit_text_dob);
        edtSoDT = findViewById(R.id.edit_text_phone);
        edtDiaChi = findViewById(R.id.edit_text_address);
        edtEmail = findViewById(R.id.edit_text_email);
        edtLop = findViewById(R.id.edt_Lop);
        spnNganh = findViewById(R.id.spn_Nganh);


        Intent i = getIntent();
        if (i.hasExtra("student")) {
            student = (Student) i.getSerializableExtra("student");
            if (student != null) {
                // TODO: Set student data here
                edtHoten.setText(student.getHoten());
                edtNgaySinh.setText(student.getNgaySinh().toString());
                edtSoDT.setText(student.getSoDT());
                edtDiaChi.setText(student.getDiaChi());
                edtEmail.setText(student.getEmail());
                edtLop.setText(String.valueOf(student.getID_Lop()));
                Nganh_ID = student.getID_Nganh();

                        Button save = findViewById(R.id.btn_save);
//                Log.i("ID_NGANH", String.valueOf(student.getID_Nganh()));
                addDataToSpinner();

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int ID = student.getID();
                        Hoten =  edtHoten.getText().toString();
                        NgaySinh =  edtNgaySinh.getText().toString();
                        SoDT =  edtSoDT.getText().toString();
                        DiaChi =  edtDiaChi.getText().toString();
                        Email =  edtEmail.getText().toString();
                        ID_Lop = parseInt(edtLop.getText().toString());

                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date ngaySinhDate = new Date();

                        try {
                            ngaySinhDate = formatter.parse(NgaySinh);
                        } catch (java.text.ParseException e) {
                            Log.i("Loi date", "loiiiiiiiii");
                        }

                        boolean result =  db.updateStudent(ID, Hoten, ngaySinhDate, SoDT, DiaChi, Email,ID_Lop , Nganh_ID);
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

        spnNganh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Nganh selectedNganh = (Nganh) adapterView.getItemAtPosition(i);
                if (selectedNganh != null) {
                    Nganh_ID = selectedNganh.getID();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void addDataToSpinner() {
        List<Nganh> nganhList = db.readNganh();

        ArrayAdapter<Nganh> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, nganhList);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnNganh.setAdapter(spinnerAdapter);

        int selectedNganhID = student.getID_Nganh();
        for (int i = 0; i < nganhList.size(); i++) {
            Nganh nganh = nganhList.get(i);
            Log.i("LOAD_SPINNER",  String.valueOf(nganh.getID() ) + " - " + String.valueOf(selectedNganhID));
            if (nganh.getID() == selectedNganhID) {
                spnNganh.setSelection(i);
                break;
            }
        }
    }
}