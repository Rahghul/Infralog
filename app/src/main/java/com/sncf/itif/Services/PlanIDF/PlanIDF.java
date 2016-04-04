package com.sncf.itif.Services.PlanIDF;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rahghul on 01/04/2016.
 */
public class PlanIDF implements Parcelable {

    private String name;
    private String plan;

    public PlanIDF(String name, String plan) {
        this.name = name;
        this.plan = plan;
    }

    /**
     *
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param in a parcel from which to read this object
     */
    public PlanIDF(Parcel in) {
        readFromParcel(in);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    @Override
    public String toString() {
        return "PlanIDF{" +
                "name='" + name + '\'' +
                ", plan='" + plan + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(plan);
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
        plan = in.readString();
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
                public PlanIDF createFromParcel(Parcel in) {
                    return new PlanIDF(in);
                }

                public PlanIDF[] newArray(int size) {
                    return new PlanIDF[size];
                }
            };
}
