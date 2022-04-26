package com.kissanfactory.groceryuserapplication.Models;

public class CustomerData {
    private String name;
    private Long mobile;
    private String emailID;
    private String password;
    private boolean mobileVerified;
    private boolean emailVerified;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
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

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Override
    public String toString() {
        return "CustomerData{" +
                "name='" + name + '\'' +
                ", mobile=" + mobile +
                ", emailID='" + emailID + '\'' +
                ", password='" + password + '\'' +
                ", mobileVerified=" + mobileVerified +
                ", emailVerified=" + emailVerified +
                '}';
    }
}
