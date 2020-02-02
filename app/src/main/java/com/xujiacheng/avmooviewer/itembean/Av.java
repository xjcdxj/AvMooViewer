package com.xujiacheng.avmooviewer.itembean;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Av implements Serializable {
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
}
