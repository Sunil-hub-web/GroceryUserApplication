package com.kissanfactory.groceryuserapplication.fcm;

/**
 * Created by rohits on 15/4/19.
 */

public class NotificationDTO {
    String typeid = "";
    String id = "";
    String title = "";
    String message = "";


    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
