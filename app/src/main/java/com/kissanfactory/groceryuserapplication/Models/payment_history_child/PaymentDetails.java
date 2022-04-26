package com.kissanfactory.groceryuserapplication.Models.payment_history_child;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentDetails implements Serializable
{

@SerializedName("paymentMethod")
@Expose
private String paymentMethod;
@SerializedName("paymentStatus")
@Expose
private String paymentStatus;
private final static long serialVersionUID = 2527295046398811269L;

/**
* No args constructor for use in serialization
*
*/
public PaymentDetails() {
}

/**
*
* @param paymentMethod
* @param paymentStatus
*/
public PaymentDetails(String paymentMethod, String paymentStatus) {
super();
this.paymentMethod = paymentMethod;
this.paymentStatus = paymentStatus;
}

public String getPaymentMethod() {
return paymentMethod;
}

public void setPaymentMethod(String paymentMethod) {
this.paymentMethod = paymentMethod;
}

public String getPaymentStatus() {
return paymentStatus;
}

public void setPaymentStatus(String paymentStatus) {
this.paymentStatus = paymentStatus;
}

}