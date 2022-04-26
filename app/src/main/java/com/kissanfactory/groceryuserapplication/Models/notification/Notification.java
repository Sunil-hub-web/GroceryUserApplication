package com.kissanfactory.groceryuserapplication.Models.notification;

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
@SerializedName("postdata")
@Expose
private Postdata postdata;
private final static long serialVersionUID = -1829171392970809761L;

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

public Postdata getPostdata() {
return postdata;
}

public void setPostdata(Postdata postdata) {
this.postdata = postdata;
}

}