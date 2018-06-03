package com.example.haidt.mylivewallpaper.Model;

public class LiveImage {
    private String nameLiveImage;
    private int numberOfSee;
    private int numberOfDowload;
    private String urlLinkImage;
    private String catagoryID;
    private String liveImageID;
    private String urlGifImage;
    private String dateUpload;
    public LiveImage(String catagoryID, String liveImageID,String nameLiveImage,
                     int numberOfSee,int numberOfDowload, String urlLinkImage, String urlGifImage,String dateUpload){
        this.catagoryID=catagoryID;
        this.liveImageID=liveImageID;
        this.nameLiveImage=nameLiveImage;
        this.numberOfSee=numberOfSee;
        this.numberOfDowload=numberOfDowload;
        this.urlLinkImage=urlLinkImage;
        this.urlGifImage=urlGifImage;
        this.dateUpload=dateUpload;
    }

    public String getCatagoryID() {
        return catagoryID;
    }

    public String getLiveImageID() {
        return liveImageID;
    }

    public String getNameLiveImage() {
        return nameLiveImage;
    }

    public int getNumberOfDowload() {
        return numberOfDowload;
    }

    public int getNumberOfSee() {
        return numberOfSee;
    }

    public String getUrlLinkImage() {
        return urlLinkImage;
    }

    public void setCatagoryID(String catagoryID) {
        this.catagoryID = catagoryID;
    }

    public void setLiveImageID(String liveImageID) {
        this.liveImageID = liveImageID;
    }

    public void setNameLiveImage(String nameLiveImage) {
        this.nameLiveImage = nameLiveImage;
    }

    public void setNumberOfDowload(int numberOfDowload) {
        this.numberOfDowload = numberOfDowload;
    }

    public void setNumberOfSee(int numberOfSee) {
        this.numberOfSee = numberOfSee;
    }

    public void setUrlLinkImage(String urlLinkImage) {
        this.urlLinkImage = urlLinkImage;
    }

    public String getUrlGifImage() {
        return urlGifImage;
    }

    public void setUrlGifImage(String urlGifImage) {
        this.urlGifImage = urlGifImage;
    }

    public String getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(String dateUpload) {
        this.dateUpload = dateUpload;
    }
}
