package com.sncf.itif.Services.Plan;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rahghul on 23/03/2016.
 */
public class Plan implements Parcelable {

    private long id;
    private String version;
    private String reference;
    private String plan;

    public Plan(long id, String version, String reference) {
        this.id = id;
        this.version = version;
        this.reference = reference;
    }

    public Plan(long id, String version, String reference, String plan) {
        this.id = id;
        this.version = version;
        this.reference = reference;
        this.plan = plan;
    }

    public Plan(Parcel in) {
        readFromParcel(in);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", version='" + version + '\'' +
                ", reference='" + reference + '\'' +
                ", plan='" + "plan" + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(version);
        dest.writeString(reference);
        dest.writeString(plan);
    }

    /**
     * Called from the constructor to create this
     * object from a parcel.
     *
     * @param in parcel from which to re-create object
     */
    private void readFromParcel(Parcel in) {

        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        id = in.readLong();
        version = in.readString();
        reference = in.readString();
        plan = in.readString();
    }


    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
     * <p/>
     * This also means that you can use use the default
     * constructor to create the object and use another
     * method to hyrdate it as necessary.
     * <p/>
     * I just find it easier to use the constructor.
     * It makes sense for the way my brain thinks ;-)
     */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Plan createFromParcel(Parcel in) {
                    return new Plan(in);
                }

                public Plan[] newArray(int size) {
                    return new Plan[size];
                }
            };
}
