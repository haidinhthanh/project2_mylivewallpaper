package com.example.haidt.mylivewallpaper.Model;

public class CatagoryItem {
    private String idCatagory;
    private String nameCatagory;
    private String urlImageCatagory;
    public CatagoryItem( String idCatagory,String nameCatagory,String urlImageCatagory){
        this.idCatagory=idCatagory;
        this.nameCatagory=nameCatagory;
        this.urlImageCatagory=urlImageCatagory;
    }

    public String getIdCatagory() {
        return idCatagory;
    }

    public void setIdCatagory(String idCatagory) {
        this.idCatagory = idCatagory;
    }

    public void setUrlImageCatagory(String urlImageCatagory) {
        this.urlImageCatagory = urlImageCatagory;
    }

    public String getUrlImageCatagory() {
        return urlImageCatagory;
    }

    public void setNameCatagory(String nameCatagory) {
        this.nameCatagory = nameCatagory;
    }

    public String getNameCatagory() {
        return nameCatagory;
    }
}

