package com.kissanfactory.groceryuserapplication.Models.payment_history_child;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingDetails implements Serializable
{

@SerializedName("name")
@Expose
private String name;
@SerializedName("addressID")
@Expose
private String addressID;
@SerializedName("contacts")
@Expose
private Long contacts;
@SerializedName("address_details")
@Expose
private AddressDetails addressDetails;
private final static long serialVersionUID = 7943550373844426578L;

/**
* No args constructor for use in serialization
*
*/
public ShippingDetails() {
}

/**
*
* @param name
* @param addressDetails
* @param contacts
* @param addressID
*/
public ShippingDetails(String name, String addressID, Long contacts, AddressDetails addressDetails) {
super();
this.name = name;
this.addressID = addressID;
this.contacts = contacts;
this.addressDetails = addressDetails;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getAddressID() {
return addressID;
}

public void setAddressID(String addressID) {
this.addressID = addressID;
}

public Long getContacts() {
return contacts;
}

public void setContacts(Long contacts) {
this.contacts = contacts;
}

public AddressDetails getAddressDetails() {
return addressDetails;
}

public void setAddressDetails(AddressDetails addressDetails) {
this.addressDetails = addressDetails;
}

}