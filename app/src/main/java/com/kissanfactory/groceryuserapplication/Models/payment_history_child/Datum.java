package com.kissanfactory.groceryuserapplication.Models.payment_history_child;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Serializable
{

@SerializedName("paymentDetails")
@Expose
private PaymentDetails paymentDetails;
@SerializedName("trackingDetails")
@Expose
private TrackingDetails trackingDetails;
@SerializedName("shippingDetails")
@Expose
private ShippingDetails shippingDetails;
@SerializedName("expectedDelivery")
@Expose
private String expectedDelivery;
@SerializedName("discountPrice")
@Expose
private Integer discountPrice;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("_id")
@Expose
private String id;
@SerializedName("oderedBy")
@Expose
private String oderedBy;
@SerializedName("cart_id")
@Expose
private String cartId;
@SerializedName("products")
@Expose
private List<Product> products = null;
@SerializedName("orderImg")
@Expose
private String orderImg;
@SerializedName("shippingCharge")
@Expose
private Integer shippingCharge;
@SerializedName("totalAmount")
@Expose
private Integer totalAmount;
@SerializedName("seller")
@Expose
private String seller;
@SerializedName("orderStatus")
@Expose
private String orderStatus;
@SerializedName("__v")
@Expose
private Integer v;
private final static long serialVersionUID = -8514734513703662380L;

/**
* No args constructor for use in serialization
*
*/
public Datum() {
}

/**
*
* @param seller
* @param orderImg
* @param cartId
* @param discountPrice
* @param orderStatus
* @param products
* @param createdAt
* @param totalAmount
* @param shippingDetails
* @param expectedDelivery
* @param v
* @param oderedBy
* @param trackingDetails
* @param id
* @param shippingCharge
* @param paymentDetails
* @param updatedAt
*/
public Datum(PaymentDetails paymentDetails, TrackingDetails trackingDetails, ShippingDetails shippingDetails, String expectedDelivery, Integer discountPrice, String createdAt, String updatedAt, String id, String oderedBy, String cartId, List<Product> products, String orderImg, Integer shippingCharge, Integer totalAmount, String seller, String orderStatus, Integer v) {
super();
this.paymentDetails = paymentDetails;
this.trackingDetails = trackingDetails;
this.shippingDetails = shippingDetails;
this.expectedDelivery = expectedDelivery;
this.discountPrice = discountPrice;
this.createdAt = createdAt;
this.updatedAt = updatedAt;
this.id = id;
this.oderedBy = oderedBy;
this.cartId = cartId;
this.products = products;
this.orderImg = orderImg;
this.shippingCharge = shippingCharge;
this.totalAmount = totalAmount;
this.seller = seller;
this.orderStatus = orderStatus;
this.v = v;
}

public PaymentDetails getPaymentDetails() {
return paymentDetails;
}

public void setPaymentDetails(PaymentDetails paymentDetails) {
this.paymentDetails = paymentDetails;
}

public TrackingDetails getTrackingDetails() {
return trackingDetails;
}

public void setTrackingDetails(TrackingDetails trackingDetails) {
this.trackingDetails = trackingDetails;
}

public ShippingDetails getShippingDetails() {
return shippingDetails;
}

public void setShippingDetails(ShippingDetails shippingDetails) {
this.shippingDetails = shippingDetails;
}

public String getExpectedDelivery() {
return expectedDelivery;
}

public void setExpectedDelivery(String expectedDelivery) {
this.expectedDelivery = expectedDelivery;
}

public Integer getDiscountPrice() {
return discountPrice;
}

public void setDiscountPrice(Integer discountPrice) {
this.discountPrice = discountPrice;
}

public String getCreatedAt() {
return createdAt;
}

public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

public String getUpdatedAt() {
return updatedAt;
}

public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getOderedBy() {
return oderedBy;
}

public void setOderedBy(String oderedBy) {
this.oderedBy = oderedBy;
}

public String getCartId() {
return cartId;
}

public void setCartId(String cartId) {
this.cartId = cartId;
}

public List<Product> getProducts() {
return products;
}

public void setProducts(List<Product> products) {
this.products = products;
}

public String getOrderImg() {
return orderImg;
}

public void setOrderImg(String orderImg) {
this.orderImg = orderImg;
}

public Integer getShippingCharge() {
return shippingCharge;
}

public void setShippingCharge(Integer shippingCharge) {
this.shippingCharge = shippingCharge;
}

public Integer getTotalAmount() {
return totalAmount;
}

public void setTotalAmount(Integer totalAmount) {
this.totalAmount = totalAmount;
}

public String getSeller() {
return seller;
}

public void setSeller(String seller) {
this.seller = seller;
}

public String getOrderStatus() {
return orderStatus;
}

public void setOrderStatus(String orderStatus) {
this.orderStatus = orderStatus;
}

public Integer getV() {
return v;
}

public void setV(Integer v) {
this.v = v;
}

}