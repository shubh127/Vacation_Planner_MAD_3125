package com.example.assignment8;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {
    private String name;
    private double visitCharge;
    private int imgId1;
    private int imgId2;
    private int imgId3;
    private int imgId4;
    private String desc;

    public Place(String name, double visitCharge, int imgId1, int imgId2, int imgId3, int imgId4, String desc) {
        this.name = name;
        this.visitCharge = visitCharge;
        this.imgId1 = imgId1;
        this.imgId2 = imgId2;
        this.imgId3 = imgId3;
        this.imgId4 = imgId4;
        this.desc = desc;
    }

    protected Place(Parcel in) {
        name = in.readString();
        visitCharge = in.readDouble();
        imgId1 = in.readInt();
        imgId2 = in.readInt();
        imgId3 = in.readInt();
        imgId4 = in.readInt();
        desc = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImgId1() {
        return imgId1;
    }

    public void setImgId1(int imgId1) {
        this.imgId1 = imgId1;
    }

    public int getImgId2() {
        return imgId2;
    }

    public void setImgId2(int imgId2) {
        this.imgId2 = imgId2;
    }

    public int getImgId3() {
        return imgId3;
    }

    public void setImgId3(int imgId3) {
        this.imgId3 = imgId3;
    }

    public int getImgId4() {
        return imgId4;
    }

    public void setImgId4(int imgId4) {
        this.imgId4 = imgId4;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(visitCharge);
        parcel.writeInt(imgId1);
        parcel.writeInt(imgId2);
        parcel.writeInt(imgId3);
        parcel.writeInt(imgId4);
        parcel.writeString(desc);
    }
}
