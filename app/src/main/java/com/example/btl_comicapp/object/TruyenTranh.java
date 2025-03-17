package com.example.btl_comicapp.object;
import android.content.Context;
import com.example.btl_comicapp.database.DbHelper;

public class TruyenTranh {
    private String id;
    private String tenTruyen;
    private String tacGia;
    private String moTa;
    private String linkAnh;
    private String idCategory;
    private String tenChap;
    private DbHelper dbHelper; // Thêm DbHelper

    public TruyenTranh() {
    }

    public TruyenTranh(String id, String tenTruyen, String tacGia, String moTa, String linkAnh, String idCategory) {
        this.id = id;
        this.tenTruyen = tenTruyen;
        this.tacGia = tacGia;
        this.moTa = moTa;
        this.linkAnh = linkAnh;
        this.idCategory = idCategory;
    }
    public String getCategoryName(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        return dbHelper.getCategoryNameById(this.idCategory);
    }

    public String getId() {
        return id;
    }

    public String getTenTruyen() {
        return tenTruyen;
    }

    public void setTenTruyen(String tenTruyen) {
        this.tenTruyen = tenTruyen;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getTenChap() {
        return tenChap;
    }

    public void setTenChap(String tenChap) {
        this.tenChap = tenChap;
    }

}
