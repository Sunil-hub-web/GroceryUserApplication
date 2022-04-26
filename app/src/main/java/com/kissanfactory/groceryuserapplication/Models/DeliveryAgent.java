package com.kissanfactory.groceryuserapplication.Models;

public class DeliveryAgent {
    private String _id;
    private String name;
    private String drivingLisence;
    private int countryCode;
    private long mobile;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDrivingLisence() {
        return drivingLisence;
    }

    public void setDrivingLisence(String drivingLisence) {
        this.drivingLisence = drivingLisence;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "DeliveryAgent{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", drivingLisence='" + drivingLisence + '\'' +
                ", countryCode=" + countryCode +
                ", mobile=" + mobile +
                '}';
    }
}
