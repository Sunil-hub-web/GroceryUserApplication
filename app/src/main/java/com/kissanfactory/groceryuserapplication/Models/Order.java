package com.kissanfactory.groceryuserapplication.Models;

import java.util.List;

public class Order {
    private String orderId;
    private String orderImg;
    private String _id;
    private String vendorID;
    private String shippingName;
    private String paymentStatus;
    private String paymentMethod;
    private List<Product> products;
    private String cartId;
    private String cart_id;
    private String image;
    private String orderStatus;
    private boolean packed;
    private boolean shipped;
    private double totalAmount;
    private String shippindAddressID;
    private long shippingContact;
    private long shippingCharge;
    private double discountPrice;
    private String createdAt;
    private PaymentDetails paymentDetails;
    private ShippingDetails shippingDetails;

    public String getOrderImg() {
        return orderImg;
    }

    public void setOrderImg(String orderImg) {
        this.orderImg = orderImg;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ShippingDetails getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(ShippingDetails shippingDetails) {
        this.shippingDetails = shippingDetails;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isPacked() {
        return packed;
    }

    public void setPacked(boolean packed) {
        this.packed = packed;
    }

    public boolean isShipped() {
        return shipped;
    }

    public void setShipped(boolean shipped) {
        this.shipped = shipped;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippindAddressID() {
        return shippindAddressID;
    }

    public void setShippindAddressID(String shippindAddressID) {
        this.shippindAddressID = shippindAddressID;
    }

    public long getShippingContact() {
        return shippingContact;
    }

    public void setShippingContact(long shippingContact) {
        this.shippingContact = shippingContact;
    }

    public long getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(long shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "_id='" + _id + '\'' +
                ", vendorID='" + vendorID + '\'' +
                ", shippingName='" + shippingName + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", products=" + products +
                ", cartId='" + cartId + '\'' +
                ", cart_id='" + cart_id + '\'' +
                ", image='" + image + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", packed=" + packed +
                ", shipped=" + shipped +
                ", totalAmount=" + totalAmount +
                ", shippindAddressID='" + shippindAddressID + '\'' +
                ", shippingContact=" + shippingContact +
                ", shippingCharge=" + shippingCharge +
                ", discountPrice=" + discountPrice +
                ", createdAt='" + createdAt + '\'' +
                ", paymentDetails=" + paymentDetails +
                ", shippingDetails=" + shippingDetails +
                '}';
    }
}
