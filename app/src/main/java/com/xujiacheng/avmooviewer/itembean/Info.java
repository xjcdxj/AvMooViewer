package com.xujiacheng.avmooviewer.itembean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Info implements Serializable {
    private static final long serialVersionUID = -7162831456798418545L;
    public String name;
    public String url;
    public String imageURL;
    public Bitmap image;
    public Info() {
    }

    public Info(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Info(String name, String url, String imageURL) {
        this.name = name;
        this.url = url;
        this.imageURL = imageURL;
    }
}
