package com.sncf.itif.Services.Gare;

/**
 * Created by Rahghul on 21/03/2016.
 */
public class Gare {
    String name;
    long id;

    public Gare(long id, String name) {this.name = name; this.id = id;}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*@Override
    public String toString() {
        return ("name='" + name + '\'');
    }*/

    @Override
    public String toString() {
        return "Gare{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
