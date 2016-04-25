package com.sncf.itif.Services.TelechargementsPlan;

import android.graphics.Bitmap;

/**
 * Created by Rahghul on 15/04/2016.
 */
public class Telechargements {
    private Bitmap image;
    private String title;
    private String gareName;
    private String reference;
    private String version;

    public Telechargements(Bitmap image, String title, String gareName, String reference, String version) {
        this.image = image;
        this.title = title;
        this.gareName = gareName;
        this.reference = reference;
        this.version = version;
    }

    public String getGareName() {
        return gareName;
    }

    public void setGareName(String gareName) {
        this.gareName = gareName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
