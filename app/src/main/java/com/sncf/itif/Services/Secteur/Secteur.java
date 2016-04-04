package com.sncf.itif.Services.Secteur;

/**
 * Created by Rahghul on 22/03/2016.
 */
public class Secteur {
    private String name;
    /*private String ref;
    private String version;
    private String plan;*/
    long id;

    public Secteur(Long id, String name) {
        this.id = id;
        this.name = name;
    }

   /* public Secteur(String name, String ref, String version, String plan) {
        this.name = name;
        this.ref = ref;
        this.version = version;
        this.plan = plan;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /* public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
*/
    /*@Override
    public String toString() {
        return "Secteur{" +
                "name='" + name + '\'' +
                ", ref='" + ref + '\'' +
                ", version='" + version + '\'' +
                ", plan='" + plan + '\'' +
                '}';
    }*/

    @Override
    public String toString() {
        return "Secteur{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
