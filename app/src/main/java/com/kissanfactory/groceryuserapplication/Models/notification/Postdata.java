package com.kissanfactory.groceryuserapplication.Models.notification;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Postdata implements Serializable
{

@SerializedName("orderId")
@Expose
private String orderId;
private final static long serialVersionUID = 676568247223509961L;

public String getOrderId() {
return orderId;
}

public void setOrderId(String orderId) {
this.orderId = orderId;
}

}