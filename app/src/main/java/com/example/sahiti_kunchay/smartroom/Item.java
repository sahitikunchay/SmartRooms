package com.example.sahiti_kunchay.smartroom;

import android.graphics.Bitmap;

/**
 * Created by Sahiti_Kunchay on 4/19/2018.
 */

public class Item {
    Bitmap image;
    String title;

    public Item(Bitmap image, String title) {
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
