package com.sncf.itif.Services.Info;

import java.sql.Date;

/**
 * Created by Rahghul on 07/04/2016.
 */
public class Info {

    private long id;
    private String title;
    private String context;
    private String degree;
    private Date dateTime;


    public Info(long id, String title, String context, String degree, Date dateTime) {
        this.id = id;
        this.title = title;
        this.context = context;
        this.degree = degree;
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Info{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", context='" + context + '\'' +
                ", degree='" + degree + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
