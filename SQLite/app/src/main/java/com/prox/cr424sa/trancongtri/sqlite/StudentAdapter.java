package com.prox.cr424sa.trancongtri.sqlite;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {

    public StudentAdapter(Context context, List<Student> students) {
        super(context, 0, students);
    }
    MyDB db;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_student, parent, false);
        }

        Student student = getItem(position);
        db = new MyDB(getContext());
        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewID = convertView.findViewById(R.id.textViewID);
        Button edit = convertView.findViewById(R.id.btn_edit);
        Button delete = convertView.findViewById(R.id.btn_delete);
        int id = student.getID();

        textViewName.setText(student.getHoten());
        textViewID.setText(String.valueOf(id));

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(getContext(), EditStudentActivity.class);
                i.putExtra("student", student);
                getContext().startActivity(i);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean delete_ =  db.deleteStudent(id);
                Toast toast;
                if (delete_){
                    toast = Toast.makeText(getContext(), "Delete student successful !", LENGTH_SHORT);
                    List<Student> updatedStudents = db.readStudent();
                    updateStudentList(updatedStudents);
                }else{
                    toast = Toast.makeText(getContext(), "Delete student failed !", LENGTH_SHORT);
                }
                toast.show();
            }
        });

        return convertView;
    }

    public void updateStudentList(List<Student> students) {
        clear();
        addAll(students);
        notifyDataSetChanged();
    }
}
