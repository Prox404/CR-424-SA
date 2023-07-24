package com.prox.cr424sa.trancongtri.sqlite;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

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

public class AddStudentActivity extends AppCompatActivity {

    TextView edtHoten, edtNgaySinh, edtSoDT, edtDiaChi, edtEmail, edtLop;
    Spinner spnNganh;
    MyDB db;

    int Nganh_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        db = new MyDB(this);
        edtHoten = findViewById(R.id.edit_text_name);
        edtNgaySinh = findViewById(R.id.edit_text_dob);
        edtSoDT = findViewById(R.id.edit_text_phone);
        edtDiaChi = findViewById(R.id.edit_text_address);
        edtEmail = findViewById(R.id.edit_text_email);
        edtLop = findViewById(R.id.edt_Lop);
        spnNganh = findViewById(R.id.spn_Nganh);

        addDataToSpinner();
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

        Button store = findViewById(R.id.btn_add);
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Hoten = edtHoten.getText().toString();
                String NgaySinh = edtNgaySinh.getText().toString();
                String SoDT = edtSoDT.getText().toString();
                String DiaChi = edtDiaChi.getText().toString();
                String Email = edtEmail.getText().toString();
                int Lop = parseInt(edtLop.getText().toString()) ;
                int Nganh = Nganh_ID;

                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                Date ngaySinhDate = new Date();

                try {
                    ngaySinhDate = formatter.parse(NgaySinh);
                } catch (java.text.ParseException e) {
                    Log.i("Loi date", "loiiiiiiiii");
                }

                boolean result = db.insertStudent(Hoten, ngaySinhDate, SoDT, DiaChi, Email, Lop, Nganh);

                Toast toast;
                if (result) {
                    toast = Toast.makeText(AddStudentActivity.this, "Add student successfuly !", Toast.LENGTH_SHORT);
                    toast.show();
                    edtHoten.setText("");
                    edtNgaySinh.setText("");
                    edtSoDT.setText("");
                    edtDiaChi.setText("");
                    edtEmail.setText("");
                    edtLop.setText("1");
                    spnNganh.setSelection(0);
                } else {
                    toast = Toast.makeText(AddStudentActivity.this, "Add student failed !", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    private void addDataToSpinner() {
        List<Nganh> nganhList = db.readNganh();

        ArrayAdapter<Nganh> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, nganhList);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnNganh.setAdapter(spinnerAdapter);
    }
}