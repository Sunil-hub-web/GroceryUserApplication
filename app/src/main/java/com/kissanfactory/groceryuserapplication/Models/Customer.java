package com.kissanfactory.groceryuserapplication.Models;

public class Customer {
    private String name;
    private String emailID;
    private String password;
    private int countryCode;
    private long mobile;
    private int otp;
    private String method;
    private String fcm_id;
    public Customer(String name, String emailID, String password, int countryCode, long mobile) {
        this.name = name;
        this.emailID = emailID;
        this.password = password;
        this.countryCode = countryCode;
        this.mobile = mobile;
    }

    public Customer() {
    }

    public String getFcm_id() {
        return fcm_id;
    }

    public void setFcm_id(String fcm_id) {
        this.fcm_id = fcm_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", emailID='" + emailID + '\'' +
                ", password='" + password + '\'' +
                ", countryCode=" + countryCode +
                ", mobile=" + mobile +
                ", otp=" + otp +
                '}';
    }
}
