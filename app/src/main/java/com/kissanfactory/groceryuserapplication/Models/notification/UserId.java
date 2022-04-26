package com.kissanfactory.groceryuserapplication.Models.notification;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserId implements Serializable
{

@SerializedName("_id")
@Expose
private String id;
@SerializedName("name")
@Expose
private String name;
@SerializedName("mobile")
@Expose
private String mobile;
@SerializedName("emailID")
@Expose
private String emailID;
@SerializedName("countryCode")
@Expose
private Integer countryCode;
private final static long serialVersionUID = 2536352749384129372L;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getMobile() {
return mobile;
}

public void setMobile(String mobile) {
this.mobile = mobile;
}

public String getEmailID() {
return emailID;
}

public void setEmailID(String emailID) {
this.emailID = emailID;
}

public Integer getCountryCode() {
return countryCode;
}

public void setCountryCode(Integer countryCode) {
this.countryCode = countryCode;
}

}