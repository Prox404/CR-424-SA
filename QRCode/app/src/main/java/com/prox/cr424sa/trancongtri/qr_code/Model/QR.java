package com.prox.cr424sa.trancongtri.qr_code.Model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class QR implements Serializable {

    private int ID;
    private String Text;
    private String URL;
    private String Name;
    private String Org;
    private String Phone;
    private String Address;
    private String Email;
    private String Website;
    private String Image;

    public QR(int ID,  String text, String URL, String name, String org, String phone, String address, String email, String website) {
        this.ID = ID;
        this.Text = text;
        this.URL = URL;
        this.Name = name;
        this.Org = org;
        this.Phone = phone;
        this.Address = address;
        this.Email = email;
        this.Website = website;
    }

    public QR(int ID, String text, String URL, String name, String org, String phone, String address, String email, String website, String image) {
        this.ID = ID;
        Text = text;
        this.URL = URL;
        Name = name;
        Org = org;
        Phone = phone;
        Address = address;
        Email = email;
        Website = website;
        Image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOrg() {
        return Org;
    }

    public void setOrg(String org) {
        Org = org;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }


    @NonNull
    @Override
    public String toString() {
        return "QR{" +
                "Text='" + Text + '\'' +
                ", URL='" + URL + '\'' +
                ", Name='" + Name + '\'' +
                ", Org='" + Org + '\'' +
                ", Phone='" + Phone + '\'' +
                ", Address='" + Address + '\'' +
                ", Email='" + Email + '\'' +
                ", Website='" + Website + '\'' +
                ", Image='" + Image + '\'' +
                '}';
    }
}
