package com.xujiacheng.avmooviewer.itembean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Av implements Parcelable, Serializable {
    private static final long serialVersionUID = 5800147686381236939L;

    public Av() {
    }

    public String url;
    public String name;
    public String id;
    public String releaseDate;
    public ArrayList<Info> actors;
    public String duration;
    public Info director;
    public Info maker;
    public Info publisher;
    public ArrayList<Info> series;
    public ArrayList<Info> classes;
    public Bitmap coverImage;
    public ArrayList<Bitmap> previewImage;
    public String coverURL;
    public ArrayList<String> previewURL;
    public String bigCoverURL;
    public Bitmap bigCoverImage;


    protected Av(Parcel in) {
        url = in.readString();
        name = in.readString();
        id = in.readString();
        releaseDate = in.readString();
        duration = in.readString();
        coverImage = in.readParcelable(Bitmap.class.getClassLoader());
        previewImage = in.createTypedArrayList(Bitmap.CREATOR);
        coverURL = in.readString();
        previewURL = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Av> CREATOR = new Creator<Av>() {
        @Override
        public Av createFromParcel(Parcel in) {
            return new Av(in);
        }

        @Override
        public Av[] newArray(int size) {
            return new Av[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(releaseDate);
        dest.writeString(duration);
        dest.writeParcelable(coverImage, flags);
        dest.writeTypedList(previewImage);
        dest.writeString(coverURL);
        dest.writeStringList(previewURL);
    }
}
