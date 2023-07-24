package com.prox.cr424sa.trancongtri.sqlite;

import android.util.Log;

import androidx.annotation.NonNull;

public class Nganh {
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTenNganh() {
        return TenNganh;
    }

    public void setTenNganh(String tenNganh) {
        TenNganh = tenNganh;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    private int ID;

    public Nganh(int ID, String tenNganh, String moTa) {
        this.ID = ID;
        TenNganh = tenNganh;
        MoTa = moTa;
    }

    private String TenNganh;
    private String MoTa;

    @NonNull
    @Override
    public String toString() {
        Log.i("Nganh", String.valueOf(ID) + "-" + TenNganh +"-" +MoTa);
        return this.TenNganh;
    }
}
