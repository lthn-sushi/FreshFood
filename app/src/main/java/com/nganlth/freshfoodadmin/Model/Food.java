package com.nganlth.freshfoodadmin.Model;

public class Food {
    private String food_id;
    private String foodName;
    private String foodImage;
    private int soLuong;
    private double gia;
    private String nsx;
    private String hsd;
    private String categories_id;

    public Food() {
    }
    // ID, Tên, Ảnh, Số lượng, Giá, NSX, HSD, ID loại

    public Food(String food_id, String foodName, String foodImage, int soLuong, double gia, String nsx, String hsd, String categories_id) {
        this.food_id = food_id;
        this.foodName = foodName;
        this.foodImage = foodImage;
        this.soLuong = soLuong;
        this.gia = gia;
        this.nsx = nsx;
        this.hsd = hsd;
        this.categories_id = categories_id;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public String getNsx() {
        return nsx;
    }

    public void setNsx(String nsx) {
        this.nsx = nsx;
    }

    public String getHsd() {
        return hsd;
    }

    public void setHsd(String hsd) {
        this.hsd = hsd;
    }

    public String getCategories_id() {
        return categories_id;
    }

    public void setCategories_id(String categories_id) {
        this.categories_id = categories_id;
    }
}
