package com.prox.cr424sa.trancongtri.qr_code;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.prox.cr424sa.trancongtri.qr_code.Model.QR;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_QR = "CREATE TABLE QR (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Text TEXT , " +
            "URL TEXT, " +
            "Name TEXT," +
            "Org TEXT," +
            "Phone TEXT," +
            "Address TEXT," +
            "Email TEXT," +
            "Website TEXT )";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TEXT = "Text";
    public static final String COLUMN_URL = "URL";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_ORG = "Org";
    public static final String COLUMN_PHONE = "Phone";
    public static final String COLUMN_ADDRESS = "Address";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_WEBSITE = "Website";

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean insertQR(String Text, String URL, String Name, String Org, String Phone, String Address,  String Email, String Website) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value1  = new ContentValues();
        value1.put("Text", Text);
        value1.put("URL", URL);
        value1.put("Name", Name);
        value1.put("Org", Org);
        value1.put("Phone", Phone);
        value1.put("Address", Address);
        value1.put("Email", Email);
        value1.put("Website", Website);
        long result = db.insert("QR", null, value1);
        db.close();

        return result > 0;
    }

    public List<QR> getAllQR() {
        List<QR> qr = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {
                "Text",
                "URL",
                "Name",
                "Org",
                "Phone",
                "Address",
                "Email",
                "Website"
        };
        Cursor cursor = db.query("QR", columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Log.i("MyDB", DatabaseUtils.dumpCursorToString(cursor));
            do {
                int id = -1;
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                if(idIndex >= 0){
                    id = cursor.getInt(idIndex);
                }

                String Text = null;
                int TextIndex = cursor.getColumnIndex(COLUMN_TEXT);
                if(TextIndex >= 0){
                    Text = cursor.getString(TextIndex);
                }

                String URL = null;
                int URLIndex = cursor.getColumnIndex(COLUMN_URL);
                if(URLIndex >= 0){
                    URL = cursor.getString(URLIndex);
                }

                String Name = null;
                int NameIndex = cursor.getColumnIndex(COLUMN_NAME);
                if(NameIndex >= 0){
                    Name = cursor.getString(NameIndex);
                }

                String Org = null;
                int OrgIndex = cursor.getColumnIndex(COLUMN_ORG);
                if(OrgIndex >= 0){
                    Org = cursor.getString(OrgIndex);
                }

                String Phone = null;
                int PhoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
                if(PhoneIndex >= 0){
                    Phone = cursor.getString(PhoneIndex);
                }

                String Address = null;
                int AddressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
                if(AddressIndex >= 0){
                    Address = cursor.getString(AddressIndex);
                }

                String Email = null;
                int EmailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
                if(EmailIndex >= 0){
                    Email = cursor.getString(EmailIndex);
                }

                String Website = null;
                int WebsiteIndex = cursor.getColumnIndex(COLUMN_WEBSITE);
                if(WebsiteIndex >= 0){
                    Website = cursor.getString(WebsiteIndex);
                }

                QR newQR = new QR(
                        id,
                        Text,
                        URL,
                        Name,
                        Org,
                        Phone,
                        Address,
                        Email,
                        Website
                );
                Log.i("MyDB", newQR.toString());
                qr.add(newQR);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return qr;
    }

}
