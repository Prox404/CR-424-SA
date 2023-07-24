package com.prox.cr424sa.trancongtri.sqlite;

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
            "ID_Lop INTEGER NOT NULL," +
            "ID_Nganh INTEGER NOT NULL)";

    private static final String DATABASE_CREATE_NGANH = "CREATE TABLE Nganh (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "TenNganh TEXT NOT NULL, " +
            "MoTa TEXT NOT NULL)";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_HOTEN = "HoTen";
    public static final String COLUMN_NGAYSINH = "NgaySinh";
    public static final String COLUMN_SODT = "SoDT";
    public static final String COLUMN_DIACHI = "DiaChi";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_LOP = "ID_Lop";
    public static final String COLUMN_NGANH = "ID_Nganh";
    public static final String COLUMN_TENNGANH = "TenNganh";
    public static final String COLUMN_MOTA = "MoTa";



    public MyDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    public MyDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
        sqLiteDatabase.execSQL(DATABASE_CREATE_NGANH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertStudent(String hoten, Date ngaySinh, String soDT, String diaChi, String email, int lop,  int nganh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value1  = new ContentValues();
        value1.put("Hoten", hoten);
        value1.put("NgaySinh", ngaySinh.toString());
        value1.put("SoDT", soDT);
        value1.put("DiaChi", diaChi);
        value1.put("Email", email);
        value1.put("ID_Lop", lop);
        value1.put("ID_Nganh", nganh);
        long result = db.insert("Student", null, value1);
        db.close();

        return result > 0;
    }

    public boolean insertNganh(String TenNganh, String MoTa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value1  = new ContentValues();
        value1.put(COLUMN_TENNGANH, TenNganh);
        value1.put(COLUMN_MOTA, MoTa);
        long result = db.insert("Nganh", null, value1);
        db.close();

        return result > 0;
    }

    public List<Nganh> readNganh() {
        List<Nganh> nganh = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"ID", "TenNganh", "MoTa"};
            Cursor cursor = db.query("Nganh", columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Log.i("MyDB", DatabaseUtils.dumpCursorToString(cursor));
            do {
                int id = -1;
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                if(idIndex >= 0){
                    id = cursor.getInt(idIndex);
                }

                String tenNganh = "";
                int tenNganhIndex = cursor.getColumnIndex(COLUMN_TENNGANH);
                Log.i("MyDB_tenNganh_Index", String.valueOf(tenNganhIndex));
                if (tenNganhIndex >= 0 ){
                    tenNganh = cursor.getString(tenNganhIndex);
                }

                String moTa = "";
                int moTaIndex = cursor.getColumnIndex(COLUMN_MOTA);
                Log.i("MyDB_moTa_Index", String.valueOf(moTaIndex));
                if (moTaIndex >= 0 ){
                    moTa = cursor.getString(moTaIndex);
                }

                Nganh newNganh = new Nganh(id, tenNganh, moTa);
                Log.i("MyDB", newNganh.getTenNganh());
                nganh.add(newNganh);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return nganh;
    }
    public List<Student> readStudent() {
        List<Student> students = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"ID", "Hoten", "NgaySinh", "SoDT", "DiaChi", "Email", "ID_Lop" ,  "ID_Nganh"};
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

                int lop = 1;
                int lopIndex = cursor.getColumnIndex(COLUMN_LOP);
                if (lopIndex >= 0 ){
                    lop = cursor.getInt(lopIndex);
                }

                int nganh = 1;
                int nganhIndex = cursor.getColumnIndex(COLUMN_NGANH);
//                Log.i("MyDB_Nganh_Index", String.valueOf(nganhIndex));
                if (nganhIndex >= 0 ){
                    nganh = cursor.getInt(nganhIndex);
                }

//                String ngaySinhString = cursor.getString(cursor.getColumnIndex(COLUMN_NGAYSINH));
//                Date ngaySinh = parseDateString(ngaySinhString);
//                String soDT = cursor.getString(cursor.getColumnIndex(COLUMN_SODT));
//                String diaChi = cursor.getString(cursor.getColumnIndex(COLUMN_DIACHI));
//                String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
//                String nganh = cursor.getString(cursor.getColumnIndex(COLUMN_NGANH));

                Student student = new Student(student_id, student_hoTen, ngaySinh, soDT, diaChi, email, nganh,  lop);
//                Log.i("MyDB", student.getID_Nganh());
                students.add(student);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return students;
    }

    public boolean updateStudent(int studentId, String hoten, Date ngaySinh, String soDT, String diaChi, String email, int lop, int nganh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOTEN, hoten);
        values.put(COLUMN_NGAYSINH, ngaySinh.toString());
        values.put(COLUMN_SODT, soDT);
        values.put(COLUMN_DIACHI, diaChi);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_LOP, lop);
        values.put(COLUMN_NGANH, nganh);

        int rowsAffected = db.update("Student", values, COLUMN_ID + " = ?", new String[]{String.valueOf(studentId)});
        db.close();
        return rowsAffected > 0;
    }

    public Student getStudentByID(int studentId) {
        Student student = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlQuery = "SELECT s.ID, s.HoTen, s.NgaySinh, s.SoDT, s.DiaChi, s.Email, s.ID_Lop, s.ID_Nganh, n.TenNganh " +
                "FROM Student s " +
                "LEFT JOIN Nganh n ON s.ID_Nganh = n.ID " +
                "WHERE s.ID = ?";
        String[] selectionArgs = {String.valueOf(studentId)};

        Cursor cursor = db.rawQuery(sqlQuery, selectionArgs);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int hoTenIndex = cursor.getColumnIndex(COLUMN_HOTEN);
            int ngaySinhIndex = cursor.getColumnIndex(COLUMN_NGAYSINH);
            int soDTIndex = cursor.getColumnIndex(COLUMN_SODT);
            int diaChiIndex = cursor.getColumnIndex(COLUMN_DIACHI);
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            int lopIndex = cursor.getColumnIndex(COLUMN_LOP);
            int nganhIndex = cursor.getColumnIndex(COLUMN_NGANH);
            int tenNganhIndex = cursor.getColumnIndex(COLUMN_TENNGANH);

            if (idIndex >= 0 && hoTenIndex >= 0 && ngaySinhIndex >= 0 && soDTIndex >= 0 && diaChiIndex >= 0 && emailIndex >= 0 && lopIndex >= 0 && nganhIndex >= 0 && tenNganhIndex >= 0) {
                int id = cursor.getInt(idIndex);
                String hoTen = cursor.getString(hoTenIndex);
                Date ngaySinh = parseDateString(cursor.getString(ngaySinhIndex));
                String soDT = cursor.getString(soDTIndex);
                String diaChi = cursor.getString(diaChiIndex);
                String email = cursor.getString(emailIndex);
                int lop = cursor.getInt(lopIndex);
                int nganh = cursor.getInt(nganhIndex);
                String tenNganh = cursor.getString(tenNganhIndex);

                student = new Student(id, hoTen, ngaySinh, soDT, diaChi, email, lop, nganh, tenNganh);
            } else {
                Log.e("MyDB", "One or more columns not found in the cursor.");
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return student;
    }
    public boolean deleteStudent(int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete("Student", COLUMN_ID + " = ?", new String[]{String.valueOf(studentId)});
        db.close();
        return rowsAffected > 0;
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
