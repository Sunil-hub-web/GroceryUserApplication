package com.kissanfactory.groceryuserapplication.Models;

public class ShippingDetails {
    private String name;
    private String addressID;
    private long contacts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressId() {
        return addressID;
    }

    public void setAddressId(String addressId) {
        this.addressID = addressId;
    }

    public long getContacts() {
        return contacts;
    }

    public void setContacts(long contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "ShippingDetails{" +
                "name='" + name + '\'' +
                ", addressId='" + addressID + '\'' +
                ", contacts=" + contacts +
                '}';
    }
}
