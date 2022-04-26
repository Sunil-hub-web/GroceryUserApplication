package com.kissanfactory.groceryuserapplication.Models;


public class AddressResponse {
    private Address address;
    private String msg;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
