package com.example.implanthearts;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/5/16.
 */

public class Image implements Parcelable {
    private int id;
    private List<String> imageurl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getImageurl() {
        return imageurl;
    }

    public void setImageurl(List<String> imageurl) {
        this.imageurl = imageurl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeStringList(imageurl);
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.
            Creator<Image>() {


        @Override
        public Image createFromParcel(Parcel source) {
            Image image = new Image();
            image.id = source.readInt();
            if (image.imageurl == null) {
                image.imageurl = new ArrayList<String>();
            }
            source.readStringList(image.imageurl);
            return image;
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

}