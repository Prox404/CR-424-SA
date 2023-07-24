package com.prox.cr424sa.trancongtri.sqlite;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getHoten() {
        return Hoten;
    }

    public void setHoten(String hoten) {
        Hoten = hoten;
    }

    public Date getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        NgaySinh = ngaySinh;
    }

    public String getSoDT() {
        return SoDT;
    }

    public void setSoDT(String soDT) {
        SoDT = soDT;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getID_Nganh() {
        return ID_Nganh;
    }

    public void setID_Nganh(int ID_Nganh) {
        this.ID_Nganh = ID_Nganh;
    }

    public int getID_Lop() {
        return ID_Lop;
    }

    public void setID_Lop(int ID_Lop) {
        this.ID_Lop = ID_Lop;
    }

    public Student(int ID, String hoten, Date ngaySinh, String soDT, String diaChi, String email, int ID_Nganh, int ID_Lop) {
        this.ID = ID;
        Hoten = hoten;
        NgaySinh = ngaySinh;
        SoDT = soDT;
        DiaChi = diaChi;
        Email = email;
        this.ID_Nganh = ID_Nganh;
        this.ID_Lop = ID_Lop;
    }

    private int ID;
    private String Hoten;
    private Date NgaySinh;
    private String SoDT;
    private String DiaChi;
    private String Email;
    private int ID_Nganh;
    private int ID_Lop;

    @NonNull
    @Override
    public String toString() {
        return "ID: " + ID + "\nHoTen: " + Hoten + "\nNgaySinh: " + NgaySinh + "\nSoDT: " + SoDT + "\nDiaChi: " + DiaChi + "\nEmail: " + Email + "\nNganh: " + String.valueOf(ID_Nganh) + "\nLop: " + String.valueOf(ID_Lop) ;
    }
}
