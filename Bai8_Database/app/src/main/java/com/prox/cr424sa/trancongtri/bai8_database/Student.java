package com.prox.cr424sa.trancongtri.bai8_database;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {


    private int ID;
    private String Hoten;
    private Date NgaySinh;
    private String SoDT;
    private String DiaChi;
    private String Email;
    private String Nganh;

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

    public String getNganh() {
        return Nganh;
    }

    public void setNganh(String nganh) {
        Nganh = nganh;
    }

    public Student(int iD, String hoten, Date ngaySinh, String soDT, String diaChi, String email, String nganh) {
        ID = iD;
        Hoten = hoten;
        NgaySinh = ngaySinh;
        SoDT = soDT;
        DiaChi = diaChi;
        Email = email;
        Nganh = nganh;
    }

    @NonNull
    @Override
    public String toString() {
        return "ID: " + ID + "\nHoTen: " + Hoten + "\nNgaySinh: " + NgaySinh + "\nSoDT: " + SoDT + "\nDiaChi: " + DiaChi + "\nEmail: " + Email + "\nNganh: " + Nganh;
    }
}
