package com.kissanfactory.groceryuserapplication.Models.payment_add_order_pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notes implements Serializable
{

@SerializedName("address")
@Expose
private String address;
private final static long serialVersionUID = 1042978366809775233L;

public String getAddress() {
return address;
}

public void setAddress(String address) {
this.address = address;
}

}