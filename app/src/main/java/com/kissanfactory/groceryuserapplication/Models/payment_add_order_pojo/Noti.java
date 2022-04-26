package com.kissanfactory.groceryuserapplication.Models.payment_add_order_pojo;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Noti implements Serializable
{

@SerializedName("_id")
@Expose
private String id;
@SerializedName("UserId")
@Expose
private String userId;
@SerializedName("notification")
@Expose
private Notification notification;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("updatedAt")
@Expose
private String updatedAt;
@SerializedName("__v")
@Expose
private Integer v;
private final static long serialVersionUID = 729877479567765176L;

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

public Notification getNotification() {
return notification;
}

public void setNotification(Notification notification) {
this.notification = notification;
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

}