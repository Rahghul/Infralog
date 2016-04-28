package com.sncf.itif.Services.PlanIDF.Telechargement;

import android.graphics.Bitmap;

/**
 * Created by Rahghul on 15/04/2016.
 */
public class Telechargement {
    private Bitmap image;
    private String title;
    private String gareName;
    private String reference;
    private String version;
    private int remainingDays;

    public Telechargement(Bitmap image, String title, String gareName, String reference, String version, int remainingDays) {
        this.image = image;
        this.title = title;
        this.gareName = gareName;
        this.reference = reference;
        this.version = version;
        //Le temps restant avant l'image telechargé supprime automatiquement
        this.remainingDays = remainingDays;
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

    public int getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(int remainingDays) {
        this.remainingDays = remainingDays;
    }

    @Override
    public String toString() {
        return "Telechargement{" +
                "remainingDays=" + remainingDays +
                ", version='" + version + '\'' +
                ", reference='" + reference + '\'' +
                ", gareName='" + gareName + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
