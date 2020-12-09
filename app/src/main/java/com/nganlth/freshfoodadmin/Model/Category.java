package com.nganlth.freshfoodadmin.Model;

import androidx.annotation.NonNull;

public class Category {
    private String maLoai;
    private String tenLoai;
    private String hinhLoai;

    public Category() {
    }

    public Category(String maLoai, String tenLoai, String hinhLoai) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.hinhLoai = hinhLoai;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getHinhLoai() {
        return hinhLoai;
    }

    public void setHinhLoai(String hinhLoai) {
        this.hinhLoai = hinhLoai;
    }

    // Lấy tên của thể loại thay vì mã
    @NonNull
    @Override
    public String toString() {
        return " - "+getTenLoai();
    }
}
