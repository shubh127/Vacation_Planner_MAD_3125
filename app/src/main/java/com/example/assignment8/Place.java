package com.example.assignment8;

public class Place {
    private String name;
    private double visitCharge;
    private int imgId;
    private String desc;

    public Place(String name, double visitCharge, int imgId, String desc) {
        this.name = name;
        this.visitCharge = visitCharge;
        this.imgId = imgId;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVisitCharge() {
        return visitCharge;
    }

    public void setVisitCharge(double visitCharge) {
        this.visitCharge = visitCharge;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
