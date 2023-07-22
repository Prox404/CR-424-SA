package com.prox.cr424sa.trancongtri.bai8_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "applicationdata.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "CREATE TABLE Student (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "HoTen TEXT NOT NULL, " +
            "NgaySinh TEXT NOT NULL, " +
            "SoDT TEXT NOT NULL," +
            "DiaChi TEXT NOT NULL," +
            "Email TEXT NOT NULL," +
            "Nganh TEXT NOT NULL)";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_HOTEN = "HoTen";
    public static final String COLUMN_NGAYSINH = "NgaySinh";
    public static final String COLUMN_SODT = "SoDT";
    public static final String COLUMN_DIACHI = "DiaChi";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_NGANH = "Nganh";
    public MyDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    public MyDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertStudent(String hoten, Date ngaySinh, String soDT, String diaChi, String email, String nganh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value1  = new ContentValues();
        value1.put("Hoten", hoten);
        value1.put("NgaySinh", ngaySinh.toString());
        value1.put("SoDT", soDT);
        value1.put("DiaChi", diaChi);
        value1.put("Email", email);
        value1.put("Nganh", nganh);
        db.insert("Student", null, value1);
        db.close();
    }

    public List<Student> readStudent() {
        List<Student> students = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"ID", "Hoten", "NgaySinh", "SoDT", "DiaChi", "Email", "Nganh"};
        Cursor cursor = db.query("Student", columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
                Log.i("MyDB", DatabaseUtils.dumpCursorToString(cursor));
            do {
                int student_id = -1;
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                if(idIndex >= 0){
                    student_id = cursor.getInt(idIndex);
                }

                String student_hoTen = "";
                int hoTenIndex = cursor.getColumnIndex(COLUMN_HOTEN);
                Log.i("MyDB_HoTen_Index", String.valueOf(hoTenIndex));
                if (hoTenIndex >= 0 ){
                    student_hoTen = cursor.getString(hoTenIndex);
                }

                Date ngaySinh = null;
                int ngaySinhIndex = cursor.getColumnIndex(COLUMN_NGAYSINH);
                if (ngaySinhIndex >= 0) {
                    ngaySinh = parseDateString(cursor.getString(ngaySinhIndex));
                }

                String soDT = "";
                int soDTIndex = cursor.getColumnIndex(COLUMN_SODT);
                Log.i("MyDB_SDT_Index", String.valueOf(soDTIndex));
                if (soDTIndex >= 0 ){
                    soDT = cursor.getString(soDTIndex);
                }

                String diaChi = "";
                int diaChiIndex = cursor.getColumnIndex(COLUMN_DIACHI);
                if (diaChiIndex >= 0 ){
                    diaChi = cursor.getString(diaChiIndex);
                }

                String email = "";
                int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                if (emailIndex >= 0 ){
                    email = cursor.getString(emailIndex);
                }

                String nganh = "";
                int nganhIndex = cursor.getColumnIndex(COLUMN_NGANH);
                if (nganhIndex >= 0 ){
                    nganh = cursor.getString(nganhIndex);
                }

//                String ngaySinhString = cursor.getString(cursor.getColumnIndex(COLUMN_NGAYSINH));
//                Date ngaySinh = parseDateString(ngaySinhString);
//                String soDT = cursor.getString(cursor.getColumnIndex(COLUMN_SODT));
//                String diaChi = cursor.getString(cursor.getColumnIndex(COLUMN_DIACHI));
//                String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
//                String nganh = cursor.getString(cursor.getColumnIndex(COLUMN_NGANH));

                Student student = new Student(student_id, student_hoTen, ngaySinh, soDT, diaChi, email, nganh);
                Log.i("MyDB", student.getHoten());
                students.add(student);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return students;
    }

    private Date parseDateString(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
