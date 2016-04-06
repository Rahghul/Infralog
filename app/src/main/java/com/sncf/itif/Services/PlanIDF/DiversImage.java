package com.sncf.itif.Services.PlanIDF;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rahghul on 01/04/2016.
 */
public class DiversImage implements Parcelable {

    private String name;
    private String image;

    public DiversImage(String name, String plan) {
        this.name = name;
        this.image = plan;
    }

    /**
     *
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param in a parcel from which to read this object
     */
    public DiversImage(Parcel in) {
        readFromParcel(in);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
    }

    /**
     *
     * Called from the constructor to create this
     * object from a parcel.
     *
     * @param in parcel from which to re-create object
     */
    private void readFromParcel(Parcel in) {

        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        name = in.readString();
        image = in.readString();
    }

    /**
     *
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
     *
     * This also means that you can use use the default
     * constructor to create the object and use another
     * method to hyrdate it as necessary.
     *
     * I just find it easier to use the constructor.
     * It makes sense for the way my brain thinks ;-)
     *
     */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public DiversImage createFromParcel(Parcel in) {
                    return new DiversImage(in);
                }

                public DiversImage[] newArray(int size) {
                    return new DiversImage[size];
                }
            };
}
