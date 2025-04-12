package vn.edu.tlu.cse.btnhom.btap_android;

import java.io.Serializable;

public class Story implements Serializable {
    private String id;
    private String tenTruyen;
    private String tacGia;
    private String moTa;
    private String linkAnh;
    private String idCategory;
    private String tenChap;

    // ⚠️ BẮT BUỘC: Constructor rỗng để Firebase sử dụng
    public Story() {
    }

    // Constructor đầy đủ
    public Story(String id, String tenTruyen, String tacGia, String moTa, String linkAnh, String idCategory, String tenChap) {
        this.id = id;
        this.tenTruyen = tenTruyen;
        this.tacGia = tacGia;
        this.moTa = moTa;
        this.linkAnh = linkAnh;
        this.idCategory = idCategory;
        this.tenChap = tenChap;
    }

    // Getter & Setter (nếu cần)
    public String getId() { return id; }
    public String getTenTruyen() { return tenTruyen; }
    public String getTacGia() { return tacGia; }
    public String getMoTa() { return moTa; }
    public String getLinkAnh() { return linkAnh; }
    public String getIdCategory() { return idCategory; }
    public String getTenChap() { return tenChap; }

    public void setId(String id) { this.id = id; }
    public void setTenTruyen(String tenTruyen) { this.tenTruyen = tenTruyen; }
    public void setTacGia(String tacGia) { this.tacGia = tacGia; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public void setLinkAnh(String linkAnh) { this.linkAnh = linkAnh; }
    public void setIdCategory(String idCategory) { this.idCategory = idCategory; }
    public void setTenChap(String tenChap) { this.tenChap = tenChap; }
}

