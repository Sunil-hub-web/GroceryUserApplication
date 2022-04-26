package com.kissanfactory.groceryuserapplication.Models.paymenthistory;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Serializable
{

@SerializedName("_id")
@Expose
private String id;
@SerializedName("user_id")
@Expose
private String userId;
@SerializedName("cart_id")
@Expose
private String cartId;
@SerializedName("amount")
@Expose
private double amount;
@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("__v")
@Expose
private Integer v;
@SerializedName("payment_status")
@Expose
private PaymentStatus paymentStatus;
private final static long serialVersionUID = 4246967105368007572L;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
}

public String getCartId() {
return cartId;
}

public void setCartId(String cartId) {
this.cartId = cartId;
}

public double getAmount() {
return amount;
}

public void setAmount(double amount) {
this.amount = amount;
}

public Boolean getSuccess() {
return success;
}

public void setSuccess(Boolean success) {
this.success = success;
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

public Integer getV() {
return v;
}

public void setV(Integer v) {
this.v = v;
}

public PaymentStatus getPaymentStatus() {
return paymentStatus;
}

public void setPaymentStatus(PaymentStatus paymentStatus) {
this.paymentStatus = paymentStatus;
}

}