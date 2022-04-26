package com.kissanfactory.groceryuserapplication.Models.paymet_check_status;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification implements Serializable
{

@SerializedName("title")
@Expose
private String title;
@SerializedName("body")
@Expose
private String body;
private final static long serialVersionUID = -7037158608646857973L;

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getBody() {
return body;
}

public void setBody(String body) {
this.body = body;
}

}