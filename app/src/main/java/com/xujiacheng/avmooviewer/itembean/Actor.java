package com.xujiacheng.avmooviewer.itembean;

import android.graphics.Bitmap;

public class Actor {
    public String name;
    public String url;
    public String imageURL;
    public Bitmap image;

    public Actor() {
    }

    public Actor(String url, String name, String imagURL) {
        this.name = name;
        this.url = url;
        this.imageURL = imagURL;
    }
}
