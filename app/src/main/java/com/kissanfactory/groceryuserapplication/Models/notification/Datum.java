package com.kissanfactory.groceryuserapplication.Models.notification;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum implements Serializable
{

@SerializedName("_id")
@Expose
private String id;
@SerializedName("UserId")
@Expose
private UserId userId;
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
private final static long serialVersionUID = -8473057537267259140L;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public UserId getUserId() {
return userId;
}

public void setUserId(UserId userId) {
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