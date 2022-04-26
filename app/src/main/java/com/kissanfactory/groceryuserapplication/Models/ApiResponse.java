package com.kissanfactory.groceryuserapplication.Models;

import java.util.List;

public class ApiResponse
{
    private int code;

    private boolean error;

    private String msg;
    private String token;
    private String otp;
    private List<Category> data;
    private List<Address> address;
    private List<Product> productDetails;
    private List<Favourite> favourite;
    private List<TopProdViewAll>prodViewAlls;
    private CartItems cartItems;
    private String other;
    private List<UserNotification> notification;


    public List<UserNotification> getNotification() {
        return notification;
    }

    public void setNotification(List<UserNotification> notification) {
        this.notification = notification;
    }

    public boolean isError() {
        return error;
    }

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public List<Favourite> getFavourite() {
        return favourite;
    }

    public void setFavourite(List<Favourite> favourite) {
        this.favourite = favourite;
    }

    public CartItems getCartItems() {
        return cartItems;
    }

    public void setCartItems(CartItems cartItems) {
        this.cartItems = cartItems;
    }

    public List<Product> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<Product> productDetails) {
        this.productDetails = productDetails;
    }


    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }

    private CustomerData userData;

    public CustomerData getUserData() {
        return userData;
    }

    public void setUserData(CustomerData userData) {
        this.userData = userData;
    }

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setError(boolean error){
        this.error = error;
    }
    public boolean getError(){
        return this.error;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }


    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", error=" + error +
                ", msg='" + msg + '\'' +
                ", token='" + token + '\'' +
                ", otp='" + otp + '\'' +
                ", address=" + address +
                ", productDetails=" + productDetails +
                ", favourite=" + favourite +
                ", cartItems=" + cartItems +
                ", other='" + other + '\'' +
                ", userData=" + userData +
                '}';
    }
}
