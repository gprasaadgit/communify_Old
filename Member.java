package com.gap22.community.apartment.Database;

/**
 * Created by Shiva on 4/6/17.
 */

public class Member {
    private String email;
    private String firstname;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getResidencestatus() {
        return residencestatus;
    }

    public void setResidencestatus(String residencestatus) {
        this.residencestatus = residencestatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNumAdults() {
        return numAdults;
    }

    public void setNumAdults(int numAdults) {
        this.numAdults = numAdults;
    }

    public int getNumChildren() {
        return numChildren;
    }

    public void setNumChildren(int numChildren) {
        this.numChildren = numChildren;
    }

    public int getNumInfants() {
        return numInfants;
    }

    public void setNumInfants(int numInfants) {
        this.numInfants = numInfants;
    }

    private String lastname;
    private String occupation;
    private String residencestatus;
    private String title;
    private String unit;
    private int status;
    private int numAdults;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private int numChildren;
    private int numInfants;
}
